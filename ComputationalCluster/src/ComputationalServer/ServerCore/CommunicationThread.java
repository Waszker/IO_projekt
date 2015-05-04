package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.DivideProblem;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;

/**
 * <p>
 * Class used by IMessages to get and set information associated with
 * computational server.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * @version 1.1 complete refactoring for the more object-oriented approach
 * 
 */
class CommunicationThread extends AbstractServerCoreProtocol
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates object with reference to main server core.
	 * </p>
	 * 
	 * @param core
	 */
	public CommunicationThread(ComputationalServerCore core)
	{
		super(core);
	}

	@Override
	public BigInteger registerComponent(BigInteger receivedId,
			int numberOfThreads, boolean deregister,
			GenericComponent.ComponentType componentType,
			List<String> solvableProblems, Integer remotePort,
			String remoteAddress)
	{
		return core.registerComponent(null, numberOfThreads, componentType,
				solvableProblems, remotePort, remoteAddress);
	}

	@Override
	public BigInteger registerProblem(BigInteger sentId, byte[] data,
			String problemType, BigInteger solvingTimeout)
	{
		BigInteger id = core.getCurrentFreeProblemId(null);
		core.problemsToSolve.put(id, new ProblemInfo(id, data, problemType,
				solvingTimeout));

		return id;
	}

	@Override
	public List<IMessage> getStatusResponseMessages(BigInteger id,
			int freeThreads)
	{
		List<IMessage> messages = new ArrayList<>(freeThreads);

		switch (getComponentTypeFromId(id))
		{
			case TaskManager:
				TaskManagerInfo taskManager = core.taskManagers.get(id);
				// If there is some problem with number of threads (consistency
				// loss probably!)
				if (taskManager.numberOfThreads != taskManager.assignedMessages
						.size() + freeThreads)
				{
					Logger.log("Consistency loss! Number of free threads is wrong! Dropping component id "
							+ taskManager.id + "\n");
					core.componentMonitorThread.dropComponent(taskManager.id);
				}
				else
					addTaskManagerMessages(taskManager, freeThreads, messages);
				break;

			case ComputationalNode:
				ComputationalNodeInfo computationalNode = core.computationalNodes
						.get(id);
				// If there is some problem with number of threads (consistency
				// loss probably!)
				if (computationalNode.numberOfThreads != computationalNode.assignedMessages
						.size() + freeThreads)
				{
					Logger.log("Consistency loss! Number of free threads is wrong! Dropping component id "
							+ computationalNode.id + "\n");
					core.componentMonitorThread
							.dropComponent(computationalNode.id);
				}
				else
					addComputationalNodeMessages(computationalNode,
							freeThreads, messages);
				break;

			default:
				break;
		}
		core.delayedMessages.removeAll(messages);

		return messages;
	}

	@Override
	public List<IMessage> getBackupServerSynchronizationMessages()
	{
		List<IMessage> messages = new ArrayList<>();

		if (core.backupServer.indexOfLastUnSynchronizedMessage < core.listOfMessagesForBackupServer
				.size())
			messages.addAll(core.listOfMessagesForBackupServer.subList(
					core.backupServer.indexOfLastUnSynchronizedMessage,
					core.listOfMessagesForBackupServer.size()));
		core.backupServer.indexOfLastUnSynchronizedMessage = core.listOfMessagesForBackupServer
				.size();

		return messages;
	}

	@Override
	public Solution getProblemSolution(BigInteger id)
	{
		Solution solution = new Solution();
		solution.setType(ProblemInfo.ProblemStatus.Ongoing.text);

		if (core.problemsToSolve.get(id) != null
				&& core.problemsToSolve.get(id).finalSolution != null)
		{
			solution = core.problemsToSolve.get(id).finalSolution;
			solution.setType(ProblemInfo.ProblemStatus.Final.text);
		}

		return solution;
	}

	@Override
	public void addBackupServerMessage(IMessage message)
	{
		core.listOfMessagesForBackupServer.add(message);
	}

	@Override
	public boolean setProblemPartsInfo(
			BigInteger id,
			byte[] commonData,
			List<SolvePartialProblems.PartialProblems.PartialProblem> partialProblems)
	{
		ProblemInfo problem = core.problemsToSolve.get(id);
		int numberOfParts = partialProblems.size();
		problem.data = commonData.clone();
		problem.parts = numberOfParts;
		problem.isProblemDivided = true;
		problem.isProblemCurrentlyDelegated = false;

		return false;
	}

	@Override
	public List<Solution> informAboutProblemSolutions(BigInteger problemId,
			BigInteger taskManagerId, List<Solution> solution)
	{
		ProblemInfo problem = core.problemsToSolve.get(problemId);

		// If it was final solution
		if (problem.parts == 0)
		{
			problem.parts = -1;
			problem.finalSolution = solution.get(0);
			removeTaskManagerSpecificMessage(problemId);
		}
		else
		// Received partial solution
		{
			problem.partialSolutions.addAll(solution);

			for (Solution s : solution)
				removeComputationalNodeSpecificMessage(problemId, s.getTaskId());
			if (problem.parts == problem.partialSolutions.size())
				problem.parts = 0;
		}

		return (problem.parts == 0 ? problem.partialSolutions : null);
	}

	@Override
	public void informAboutComponentStatusMessage(BigInteger componentId)
	{
		core.componentMonitorThread.informaAboutConnectedComponent(componentId);
	}

	@Override
	public void reactToDivideProblem(BigInteger taskManagerId,
			BigInteger problemId)
	{
		// we should never receive this message!
	}

	private void addTaskManagerMessages(TaskManagerInfo taskManager,
			int numFreeThreads, List<IMessage> messages)
	{
		Iterator<IMessage> it = core.delayedMessages.iterator();
		int freeThreads = numFreeThreads;
		while ((freeThreads--) > 0 && it.hasNext())
		{
			IMessage m = it.next();
			if (taskManager.isProblemSupported(m))
			{
				// For DIVIDE_PROBLEM we have to set some specific
				// options
				if (m.getMessageType() == MessageType.DIVIDE_PROBLEM
						&& core.computationalNodes.size() > 0)
				{
					DivideProblem message = (DivideProblem) m;
					message.setNodeID(taskManager.id);
					message.setComputationalNodes(new BigInteger(String
							.valueOf(core.computationalNodes.size())));
				}
				else
				{
					Solutiones message = (Solutiones) m;
					// TODO: Here is my secret hack!!!!
					message.getSolutions().getSolution().get(0)
							.setTaskId(taskManager.id);
				}
				messages.add(m);
			}

		}
		taskManager.assignedMessages.addAll(messages);
	}

	private void addComputationalNodeMessages(
			ComputationalNodeInfo computationalNode, int numFreeThreads,
			List<IMessage> messages)
	{
		Iterator<IMessage> it = core.delayedMessages.iterator();
		int freeThreads = numFreeThreads;
		while ((freeThreads--) > 0 && it.hasNext())
		{
			IMessage m = it.next();
			if (computationalNode.isProblemSupported(m))
			{
				SolvePartialProblems message = (SolvePartialProblems) m;
				message.getPartialProblems().getPartialProblem().get(0)
						.setNodeID(computationalNode.id);
				messages.add(m);
			}
		}
		computationalNode.assignedMessages.addAll(messages);
	}
}
