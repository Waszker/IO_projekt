package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DebugTools.Logger;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.Error;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones;
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
class CommunicationThread
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

		// Get the id for component from server core
		if (null != ((Register) message).getType())
		{
			id = core.registerComponent((Register) message, remotePort,
					remoteAddress);
		}

		// If component is invalid
		if (-1 == id.intValue())
		{
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorDetails("Component not registered");
			response = errorMessage;
		}
		else
		// Get component RegisterResponse message
		{
			RegisterResponse registerResponse = new RegisterResponse();
			registerResponse.setId(id);
			registerResponse.setTimeout(core.timeout);
			response = registerResponse;
			core.componentMonitorThread.informaAboutConnectedComponent(id);
		}

		GenericProtocol.sendMessages(socket, response);
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
				.informaAboutConnectedComponent(id))
		{
			Logger.log("Component not registered\n");
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorDetails("Component not registered");
			GenericProtocol.sendMessages(socket, errorMessage);
		}
		else
		// Check if component needs some work
		{
			List<IMessage> messagesList = new ArrayList<>();
			messagesList.add(messageGenerator.getNoOperationMessage());

			if (core.taskManagers.containsKey(id))
			{
				messagesList.addAll(messageGenerator.getTaskManagerQuests(
						socket, core.taskManagers.get(id)));
			}
			else if (core.computationalNodes.containsKey(id))
			{
				messagesList.addAll(messageGenerator
						.makeComputationalNodeWorkHard(socket,
								core.computationalNodes.get(id)));
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
		BigInteger id = message.getId();
		ProblemInfo problem = core.problemsToSolve.get(id);
		Solutiones result = new Solutiones();
		result.setId(id);
		// TODO: Fill information about computation

		GenericProtocol.sendMessages(socket, message);
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
		// TODO: Add timeout for problem
		BigInteger id = core.getCurrentFreeProblemId();
		ProblemInfo problem = new ProblemInfo(id, message);
		core.problemsToSolve.put(id, problem);

		SolveRequestResponse response = new SolveRequestResponse();
		response.setId(id);
		GenericProtocol.sendMessages(socket, response);
	}

	/**
	 * <p>
	 * Reacting to PartialProblems message involves saving partial problems data
	 * and sendiong NoOperation message.
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
		GenericProtocol.sendMessages(socket,
				messageGenerator.getNoOperationMessage());
	}

	void reactToSolution(Solutiones message, Socket socket) throws IOException
	{
		BigInteger problemId = message.getId();
		ProblemInfo problem = core.problemsToSolve.get(problemId);
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

		GenericProtocol.sendMessages(socket,
				messageGenerator.getNoOperationMessage());
	}

}