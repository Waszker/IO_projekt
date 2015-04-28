package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.DivideProblem;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;
import XMLMessages.Solutiones.Solutions.Solution;

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
			boolean deregister, GenericComponent.ComponentType componentType,
			List<String> solvableProblems, Integer remotePort,
			String remoteAddress)
	{
		return core.registerComponent(null, componentType, solvableProblems,
				remotePort, remoteAddress);
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
		Iterator<IMessage> it = core.delayedMessages.iterator();

		switch (getComponentTypeFromId(id))
		{
			case TaskManager:
				TaskManagerInfo taskManager = core.taskManagers.get(id);

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
							message.setNodeID(id);
							message.setComputationalNodes(new BigInteger(String
									.valueOf(core.computationalNodes.size())));
						}
						else
						{
							Solutiones message = (Solutiones) m;
							message.getSolutions().getSolution().get(0)
									.setTaskId(id);
						}
						messages.add(m);
					}

				}
				taskManager.assignedMessages.addAll(messages);

				break;

			case ComputationalNode:
				ComputationalNodeInfo computationalNode = core.computationalNodes
						.get(id);

				while ((freeThreads--) > 0 && it.hasNext())
				{
					IMessage m = it.next();
					if (computationalNode.isProblemSupported(m))
					{
						SolvePartialProblems message = (SolvePartialProblems) m;
						message.getPartialProblems().getPartialProblem().get(0)
								.setNodeID(id);
						messages.add(m);
					}
				}
				computationalNode.assignedMessages.addAll(messages);

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
	public List<Solution> informAboutProblemSolution(BigInteger problemId,
			BigInteger taskManagerId, Solution solution)
	{
		ProblemInfo problem = core.problemsToSolve.get(problemId);

		// If it was final solution
		if (problem.parts == 0)
		{
			problem.parts = -1;
			problem.finalSolution = solution;
			removeTaskManagerSpecificMessage(problemId);
		}
		else
		// Received partial solution
		{
			problem.partialSolutions.add(solution);
			problem.parts--;
			removeComputationalNodeSpecificMessage(problemId,
					solution.getTaskId());
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
}
