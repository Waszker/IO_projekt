package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.IServerProtocol;
import XMLMessages.Error;
import XMLMessages.Error.ErrorMessage;
import XMLMessages.Register;
import XMLMessages.RegisterResponse.BackupCommunicationServers;
import XMLMessages.RegisterResponse.BackupCommunicationServers.BackupCommunicationServer;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import XMLMessages.Status;

/**
 * <p>
 * Class used by ComputationalServerCore to send messages and problems to
 * components.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class CommunicationThread implements IServerProtocol
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;
	private MessageGeneratorThread messageGenerator;

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
		this.core = core;
		messageGenerator = new MessageGeneratorThread(core);
	}

	/**
	 * <p>
	 * Reaction to Register message involves getting free id from server core
	 * and (if component is permitted to connect) registering it. This will also
	 * begin countdown procedure for component. and
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToRegisterMessage(Register message, Socket socket)
			throws IOException
	{
		BigInteger id = new BigInteger("-1");
		Integer remotePort = socket.getPort();
		String remoteAddress = socket.getInetAddress().toString();
		IMessage response = null;
		BackupCommunicationServers backupServers = getBackupServer();

		// Get the id for component from server core
		if (null != ((Register) message).getType())
		{
			id = core.registerComponent((Register) message, remotePort,
					remoteAddress);
		}

		// If component is invalid
		if (-1 == id.intValue()
				&& !message.getType().contentEquals(
						ComponentType.ComputationalServer.name))
		{
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorType(ErrorMessage.UnknownSender);
			response = errorMessage;
		} else
		// Get component RegisterResponse message
		{
			response = messageGenerator.getRegisterResponseMessage(id,
					backupServers);

			if (null == core.backupServer)
				core.componentMonitorThread.informaAboutConnectedComponent(id);

			// Add message for BS
			if (!message.getType().contentEquals(
					ComponentType.ComputationalServer.name))
			{
				message.setDeregister(false);
				message.setId(id);
				core.listOfMessagesForBackupServer.add(message);
			}
		}

		GenericProtocol.sendMessages(socket, response);
	}

	@Override
	public BigInteger registerComponent(
			GenericComponent.ComponentType componentType,
			List<String> solvableProblems, Integer remotePort,
			String remoteAddress)
	{
		return core.registerComponent(componentType, solvableProblems,
				remotePort, remoteAddress);
	}

	/**
	 * <p>
	 * Reaction to Status message involves reseting lease time for component and
	 * updation information about it. It is also moment when component can get
	 * new work assigned to it.
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToStatusMessage(Status message, Socket socket) throws IOException
	{
		BigInteger id = message.getId();

		// Check if component is still valid in system
		if (false == core.componentMonitorThread
				.informaAboutConnectedComponent(id) && !core.isInBackupMode)
		{
			Logger.log("Component not registered\n");
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorType(ErrorMessage.UnknownSender);
			GenericProtocol.sendMessages(socket, errorMessage);
		} else
		// Check if component needs some work
		{
			List<IMessage> messagesList = new ArrayList<>();
			messagesList.add(messageGenerator.getNoOperationMessage());

			if (core.taskManagers.containsKey(id))
			{
				messagesList.addAll(messageGenerator.getTaskManagerQuests(
						socket, core.taskManagers.get(id)));
			} else if (core.computationalNodes.containsKey(id))
			{
				messagesList.addAll(messageGenerator
						.makeComputationalNodeWorkHard(socket,
								core.computationalNodes.get(id)));
			} else if (null != core.backupServer
					&& core.backupServer.id.equals(message.getId()))
			{
				messagesList.clear();
				if (core.backupServer.indexOfLastUnSynchronizedMessage < core.listOfMessagesForBackupServer
						.size())
					messagesList
							.addAll(core.listOfMessagesForBackupServer
									.subList(
											core.backupServer.indexOfLastUnSynchronizedMessage,
											core.listOfMessagesForBackupServer
													.size()));
				core.backupServer.indexOfLastUnSynchronizedMessage = core.listOfMessagesForBackupServer
						.size();

			}

			GenericProtocol.sendMessages(socket,
					messagesList.toArray(new IMessage[messagesList.size()]));
		}
	}

	/**
	 * <p>
	 * Reacting to SolutionRequest message involves sending information about
	 * status of specified problem.
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToSolutionRequest(SolutionRequest message, Socket socket)
			throws IOException
	{
		List<IMessage> messages = new ArrayList<>(2);
		BigInteger id = message.getId();
		ProblemInfo problem = core.problemsToSolve.get(id);
		Solutiones result = new Solutiones();
		result.setId(id);
		result.setSolutions(new Solutions());

		if (null != problem.finalSolution)
		{
			result.getSolutions().getSolution().add(problem.finalSolution);
			result.getSolutions().getSolution().get(0).setType("Final");
		} else
		{
			Solution sol = new Solution();
			sol.setType("Ongoing");
			result.getSolutions().getSolution().add(sol);
		}
		messages.add(messageGenerator.getNoOperationMessage());
		messages.add(result);

		GenericProtocol.sendMessages(socket,
				messages.toArray(new IMessage[messages.size()]));
	}

	/**
	 * <p>
	 * Reacting to SolverRequest message is pretty straightforward - just save
	 * problem inside ProblemInfo instance and return new problem ID to CC.
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToSolveRequest(SolveRequest message, Socket socket)
			throws IOException
	{
		// Set problem data
		List<IMessage> messages = new ArrayList<>(2);
		BigInteger id = core.getCurrentFreeProblemId();
		ProblemInfo problem = new ProblemInfo(id, message);
		core.problemsToSolve.put(id, problem);

		// Inform CC about problem id
		SolveRequestResponse response = new SolveRequestResponse();
		response.setId(id);
		messages.add(messageGenerator.getNoOperationMessage());
		messages.add(response);
		GenericProtocol.sendMessages(socket,
				messages.toArray(new IMessage[messages.size()]));

		// Relay information for BS
		message.setId(id);
		core.listOfMessagesForBackupServer.add(message);
	}

	/**
	 * <p>
	 * Reacting to PartialProblems message involves saving partial problems data
	 * and sending NoOperation message.
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToPartialProblems(SolvePartialProblems message, Socket socket)
			throws IOException
	{
		ProblemInfo problem = core.problemsToSolve.get(message.getId());
		problem.data = message.getCommonData();
		problem.parts = message.getPartialProblems().getPartialProblem().size();

		// Set ProblemInfo about partial problems
		for (PartialProblem p : message.getPartialProblems()
				.getPartialProblem())
		{
			problem.partialProblems.add(p);
		}

		// Remove problem from specific taskManager
		for (Map.Entry<BigInteger, TaskManagerInfo> entry : core.taskManagers
				.entrySet())
		{
			TaskManagerInfo tm = entry.getValue();
			if (tm.assignedProblems.contains(message.getId()))
			{
				tm.assignedProblems.remove(message.getId());
				break;
			}
		}

		// Set problem information
		problem.isProblemDivided = true;
		problem.isProblemCurrentlyDelegated = false;

		if (!core.isInBackupMode)
		{
			GenericProtocol.sendMessages(socket,
					messageGenerator.getNoOperationMessage());
			// Relay information for BS
			core.listOfMessagesForBackupServer.add(message);
		}
	}

	/**
	 * <p>
	 * Solutiones message can be obtained by server if sent from TaskManager
	 * (with final solution) or ComputationalNode (if sending partial
	 * solutions).
	 * </p>
	 * 
	 * @param message
	 * @param socket
	 * @throws IOException
	 */
	void reactToSolution(Solutiones message, Socket socket) throws IOException
	{
		BigInteger problemId = message.getId();
		ProblemInfo problem = core.problemsToSolve.get(problemId);

		// Received partial solution?
		if (problem.isProblemReadyToSolve == false)
		{
			receivePartialSolution(problem, message);
		} else
		// received final solution
		{
			problem.finalSolution = message.getSolutions().getSolution().get(0);

			for (Map.Entry<BigInteger, TaskManagerInfo> entry : core.taskManagers
					.entrySet())
			{
				if (entry.getValue().assignedProblems.contains(problemId))
				{
					entry.getValue().assignedProblems.remove(problemId);
				}
			}

		}

		// Relay information with BS
		core.listOfMessagesForBackupServer.add(message);

		GenericProtocol.sendMessages(socket,
				messageGenerator.getNoOperationMessage());
	}

	/**
	 * <p>
	 * Receives partial solution from ComputationalNode.
	 * </p>
	 * 
	 * @param problem
	 * @param message
	 */
	void receivePartialSolution(ProblemInfo problem, Solutiones message)
	{
		BigInteger problemId = message.getId();

		problem.partialSolutions.addAll(message.getSolutions().getSolution());
		problem.parts -= message.getSolutions().getSolution().size();

		if (problem.parts == 0)
		{
			problem.isProblemReadyToSolve = true;
		}

		// Remove problems from CN
		for (Map.Entry<BigInteger, ComputationalNodeInfo> entry : core.computationalNodes
				.entrySet())
		{
			ComputationalNodeInfo computationalNode = entry.getValue();
			if (computationalNode.assignedPartialProblems
					.containsKey(problemId))
			{
				List<PartialProblem> partialProblems = computationalNode.assignedPartialProblems
						.get(problemId);
				for (int i = 0; i < partialProblems.size(); i++)
				{
					if (partialProblems
							.get(i)
							.getTaskId()
							.equals(message.getSolutions().getSolution().get(0)
									.getTaskId()))
					{
						partialProblems.remove(i);
					}
				}
			}
		}
	}

	@Override
	public int getServerTimeout()
	{
		return core.timeout;
	}

	@Override
	public BackupCommunicationServers getBackupServer()
	{
		BackupCommunicationServers backupServers = new BackupCommunicationServers();
		if (null != core.backupServer)
		{
			backupServers
					.setBackupCommunicationServer(new BackupCommunicationServer());
			backupServers.getBackupCommunicationServer().setAddress(
					core.backupServer.address);
			backupServers.getBackupCommunicationServer().setPort(
					core.backupServer.port);
		} else
			backupServers = null;

		return backupServers;
	}
}
