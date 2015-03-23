package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DebugTools.Logger;
import GenericCommonClasses.IMessage;
import XMLMessages.DivideProblem;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

public class MessageGeneratorThread
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;

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
	public MessageGeneratorThread(ComputationalServerCore core)
	{
		this.core = core;
	}

	/**
	 * <p>
	 * Returns NoOperation message with filled backup servers' information.
	 * </p>
	 * 
	 * @return
	 * @throws IOException
	 */
	NoOperation getNoOperationMessage() throws IOException
	{
		NoOperation noOperation = new NoOperation();
		BackupCommunicationServers backupServers = new BackupCommunicationServers();

		if (null != core.backupServer)
		{
			backupServers.getBackupCommunicationServer().setAddress(
					core.backupServer.address);
			backupServers.getBackupCommunicationServer().setPort(
					core.backupServer.port);
		}
		noOperation.setBackupCommunicationServers(backupServers);

		return noOperation;
	}

	/**
	 * <p>
	 * Returns message asking for problem division. This message is sent to
	 * TaskManager.
	 * </p>
	 * 
	 * @param problem
	 * @return
	 * @throws IOException
	 */
	private DivideProblem getDivideProblemRequest(ProblemInfo problem,
			BigInteger nodeId) throws IOException
	{
		DivideProblem message = null;
		if (null != problem)
		{
			message = new DivideProblem();
			message.setComputationalNodes(new BigInteger(String
					.valueOf(core.computationalNodes.size())));
			message.setProblemType(problem.problemType);
			message.setId(problem.id);
			message.setNodeID(nodeId);
			message.setData(problem.data);
		}

		return message;
	}

	/**
	 * <p>
	 * Returns message informing about problem status.
	 * </p>
	 * 
	 * @param problem
	 * @return
	 * @throws IOException
	 */
	Solutiones getSolutionRequest(ProblemInfo problem) throws IOException
	{
		Solutiones message = null;
		if (null != problem)
		{
			message = new Solutiones();
			message.setCommonData(problem.data);
			message.setId(problem.id);
			message.setProblemType(problem.problemType);
			message.setSolutions(new Solutions());
			message.getSolutions().getSolution()
					.addAll(problem.partialSolutions);
		}

		return message;
	}

	SolvePartialProblems getPartialProblem(ProblemInfo problem,
			PartialProblem partialProblem)
	{
		SolvePartialProblems message = null;
		if (null != problem)
		{
			message = new SolvePartialProblems();
			message.setCommonData(problem.data);
			message.setId(problem.id);
			message.setProblemType(problem.problemType);
			message.setSolvingTimeout(problem.timeout);
			message.setPartialProblems(new PartialProblems());
			message.getPartialProblems().getPartialProblem()
					.add(partialProblem);
		}

		return message;
	}

	/**
	 * <p>
	 * Returns list of messages that need to be processed by TaskManager. Those
	 * messages can hold dividing or solving problem instance requests.
	 * </p>
	 * 
	 * @param socket
	 * @param taskManager
	 * @return
	 * @throws IOException
	 */
	synchronized List<IMessage> getTaskManagerQuests(Socket socket,
			TaskManagerInfo taskManager) throws IOException
	{
		// TODO: Refactore this method
		List<IMessage> messageList = new ArrayList<>();

		int freeThreads = taskManager.info.getParallelThreads()
				- taskManager.assignedProblems.size();
		List<BigInteger> removedId = new ArrayList<>();
		Logger.log("Looks like TaskManager id: " + taskManager.id + " has "
				+ freeThreads + " free threads\n");

		for (Map.Entry<BigInteger, ProblemInfo> entry : core.problemsToSolve
				.entrySet())
		{
			ProblemInfo problem = entry.getValue();

			if (problem.isProblemCurrentlyDelegated)
				continue;

			if (freeThreads == 0
					|| !taskManager.supportedProblems
							.contains(problem.problemType))
			{
				break;
			}

			// For problems ready to solve
			if (problem.isProblemDivided && problem.isProblemReadyToSolve)
			{
				Logger.log("Sent problem " + problem.id
						+ " for final solution\n");
				messageList.add(getSolutionRequest(core.problemsToSolve
						.get(problem.id)));
			}
			else if (!problem.isProblemDivided
					&& core.computationalNodes.size() > 0)
			// problem needs division
			{
				Logger.log("Sent problem " + problem.id + " for division\n");
				messageList.add(getDivideProblemRequest(
						core.problemsToSolve.get(problem.id), taskManager.id));
			}
			else if (problem.isProblemDivided
					|| core.computationalNodes.size() <= 0)
				continue;
			taskManager.assignedProblems.add(problem.id);
			problem.isProblemCurrentlyDelegated = true;

			freeThreads--;
			removedId.add(problem.id);
		}

		return messageList;
	}

	/**
	 * <p>
	 * Returns list of messages to be sent to ComputationalNode. Those messages
	 * will hold requests for solving partial problems.
	 * </p>
	 * 
	 * @param socket
	 * @param computationalNode
	 * @throws IOException
	 */
	synchronized List<IMessage> makeComputationalNodeWorkHard(Socket socket,
			ComputationalNodeInfo computationalNode) throws IOException
	{
		// TODO: Refactore this method
		// Get number of partialProblems currently working
		List<IMessage> messageList = new ArrayList<>();
		int partialProblemsCount = 0;
		for (Map.Entry<BigInteger, List<PartialProblem>> entry : computationalNode.assignedPartialProblems
				.entrySet())
		{
			partialProblemsCount += entry.getValue().size();
		}

		// Get free threads
		int freeThreads = computationalNode.info.getParallelThreads()
				- partialProblemsCount;
		Logger.log("Looks like ComputationalNode " + computationalNode.id
				+ " has " + freeThreads + " free threads\n");

		// Assign partial problems
		for (Map.Entry<BigInteger, ProblemInfo> entry : core.problemsToSolve
				.entrySet())
		{
			ProblemInfo problem = entry.getValue();

			if (freeThreads == 0
					|| !computationalNode.supportedProblems
							.contains(problem.problemType))
				break;

			// For partial problems
			if (problem.isProblemDivided && !problem.isProblemReadyToSolve
					&& !problem.partialProblems.isEmpty())
			{
				PartialProblem pproblem = problem.partialProblems.remove(0);
				Logger.log("Sent partial problem " + problem.id
						+ " for solution\n");

				if (freeThreads == 0)
					break;

				// Add partial problem to solve
				messageList.add(getPartialProblem(problem, pproblem));

				List<PartialProblem> delegatedProblems = computationalNode.assignedPartialProblems
						.get(problem.id);

				// if there were some problems assigned earlier
				if (null != delegatedProblems)
					delegatedProblems.add(pproblem);
				else
				{
					delegatedProblems = new ArrayList<>();
					delegatedProblems.add(pproblem);
					computationalNode.assignedPartialProblems.put(problem.id,
							delegatedProblems);
				}

				freeThreads--;
			}
		}

		return messageList;

	}
}
