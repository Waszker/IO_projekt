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
import XMLMessages.NoOperation.BackupCommunicationServers.BackupCommunicationServer;
import XMLMessages.RegisterResponse;
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
	NoOperation getNoOperationMessage()
	{
		NoOperation noOperation = new NoOperation();
		BackupCommunicationServers backupServers = new BackupCommunicationServers();

		if (null != core.backupServer)
		{
			backupServers
					.setBackupCommunicationServer(new BackupCommunicationServer());
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
	 * Returns RegisterResponse message for target component.
	 * </p>
	 * 
	 * @param id
	 * @param backupServers
	 * @return
	 */
	RegisterResponse getRegisterResponseMessage(
			BigInteger id,
			XMLMessages.RegisterResponse.BackupCommunicationServers backupServers)
	{
		RegisterResponse registerResponse = new RegisterResponse();
		registerResponse.setId(id);
		registerResponse.setTimeout(core.timeout);
		registerResponse.setBackupCommunicationServers(backupServers);

		return registerResponse;
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

	/**
	 * <p>
	 * Returns message informing about problems to solve.
	 * </p>
	 * 
	 * @param problem
	 * @param partialProblem
	 * @return
	 */
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
		// TODO: Test this method!!!!!
		List<IMessage> messageList = new ArrayList<>();

		int freeThreads = taskManager.getFreeThreads();
		List<BigInteger> removedId = new ArrayList<>();
		Logger.log("Looks like TaskManager id: " + taskManager.id + " has "
				+ freeThreads + " free threads\n");

		for (Map.Entry<BigInteger, ProblemInfo> entry : core.problemsToSolve
				.entrySet())
		{
			ProblemInfo problem = entry.getValue();

			if (problem.isProblemCurrentlyDelegated
					|| (problem.isProblemDivided && !problem.isProblemReadyToSolve)
					|| (core.computationalNodes.size() <= 0 && !problem.isProblemReadyToSolve)
					|| !taskManager.supportedProblems.contains(problem.problemType))
				continue;

			if (freeThreads == 0)
				break;

			sendProblemToTaskManager(problem, messageList, taskManager);
			assignProblemForTaskManager(taskManager, problem);

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

		// Get free threads
		int freeThreads = computationalNode.getFreeThreads();
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
				assignPartialProblemForComputationaNode(computationalNode,
						problem, pproblem);

				// Send information
				SolvePartialProblems messagePartialProblems = getPartialProblem(
						problem, pproblem);
				messageList.add(messagePartialProblems);

				// Relay info with BS
				core.listOfMessagesForBackupServer.add(messagePartialProblems);

				Logger.log("Sent partial problem " + problem.id
						+ " for solution\n");

				freeThreads--;
			}
		}

		return messageList;

	}

	/**
	 * <p>
	 * Puts information about assigned problem in task manager info object.
	 * </p>
	 * 
	 * @param taskManager
	 * @param problem
	 */
	static void assignProblemForTaskManager(TaskManagerInfo taskManager,
			ProblemInfo problem)
	{
		taskManager.assignedProblems.add(problem.id);
		problem.isProblemCurrentlyDelegated = true;
	}

	/**
	 * <p>
	 * Delegates partial problems to computational node information object.
	 * </p>
	 * 
	 * @param computationalNode
	 * @param problem
	 * @param pproblem
	 */
	static void assignPartialProblemForComputationaNode(
			ComputationalNodeInfo computationalNode, ProblemInfo problem,
			PartialProblem pproblem)
	{
		pproblem.setNodeID(computationalNode.id);

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
	}

	private void sendProblemToTaskManager(ProblemInfo problem,
			List<IMessage> messageList, TaskManagerInfo taskManager) throws IOException
	{
		// For problems ready to solve
		if (problem.isProblemDivided && problem.isProblemReadyToSolve)
		{
			Logger.log("Sent problem " + problem.id + " for final solution\n");
			Solutiones messageSolutiones = getSolutionRequest(core.problemsToSolve
					.get(problem.id));
			messageList.add(messageSolutiones);

			// Relay info with BS
			// Solution has TaskId == TaskManager id!!!
			// TODO: Maybe change that?
			messageSolutiones.getSolutions().getSolution().get(0)
					.setTaskId(taskManager.id);
			core.listOfMessagesForBackupServer.add(messageSolutiones);
		} else if (!problem.isProblemDivided
				&& core.computationalNodes.size() > 0)
		// problem needs division
		{
			Logger.log("Sent problem " + problem.id + " for division\n");
			DivideProblem messageDivideProblem = getDivideProblemRequest(
					core.problemsToSolve.get(problem.id), taskManager.id);
			messageList.add(messageDivideProblem);

			// Relay info with BS
			core.listOfMessagesForBackupServer.add(messageDivideProblem);
		}
	}
}
