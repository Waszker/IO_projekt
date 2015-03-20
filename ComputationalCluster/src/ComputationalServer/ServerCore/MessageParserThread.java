package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.DivideProblem;
import XMLMessages.Error;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import XMLMessages.Status;

/**
 * <p>
 * MessageParserThread is responsible for parsing retrieved and placed in
 * message queue messages. It also takes action according to received messages.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class MessageParserThread extends Thread
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
	 * Create thread with reference to server's core.
	 * </p>
	 * 
	 * @param core
	 */
	MessageParserThread(ComputationalServerCore core)
	{
		this.core = core;
	}

	@Override
	public void run()
	{
		while (true)
		{
			// semaphore will not be acquired if queue is empty
			try
			{
				core.queueSemaphore.acquire();
				ClientMessage message = core.messageQueue.remove();
				processMessage(message);
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	private void processMessage(ClientMessage message)
	{
		// Look at:
		// http://www.codeproject.com/Articles/11602/Java-and-Net-interop-using-Sockets
		try
		{
			Socket socket = message.getClientSocket();

			for (IMessage receivedMessage : message.getMessageContents())
			{
				if (null != receivedMessage)
				{
					Logger.log("Client [" + socket.getInetAddress() + "] \n"
							+ receivedMessage.getString());
					reactToMessage(receivedMessage, socket);
				}
			}

			socket.close();
		}
		catch (IOException | JAXBException e)
		{
		}
	}

	private void reactToMessage(IMessage message, Socket socket)
			throws JAXBException, IOException
	{
		switch (message.getMessageType())
		{
			case REGISTER:
				reactToRegisterMessage((Register) message, socket);
				break;

			case STATUS:
				reactToStatusMessage((Status) message, socket);
				break;

			case SOLVE_REQUEST:
				reactToSolveRequest((SolveRequest) message, socket);
				break;

			case PARTIAL_PROBLEM:
				reactToPartialProblems((SolvePartialProblems) message, socket);
				break;

			default:
				Logger.log("Unsupported message " + message.getString()
						+ "\n\n");
				XMLMessages.Error errorMessage = new Error();
				errorMessage.setErrorDetails("Unsupported message");
				GenericProtocol.sendMessages(socket, errorMessage);
				break;
		}
	}

	private void reactToRegisterMessage(Register message, Socket socket)
			throws IOException
	{
		BigInteger id = new BigInteger("-1");
		Integer remotePort = socket.getPort();
		String remoteAddress = socket.getInetAddress().toString();

		if (null != ((Register) message).getType())
		{
			id = core.registerComponent((Register) message, remotePort,
					remoteAddress);
		}

		if (-1 == id.intValue())
		{
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorDetails("Component not registered");
			GenericProtocol.sendMessages(socket, errorMessage);
		}
		else
		{
			RegisterResponse registerResponse = new RegisterResponse();
			registerResponse.setId(id);
			registerResponse.setTimeout(core.timeout);
			GenericProtocol.sendMessages(socket, registerResponse);
		}
	}

	private void reactToStatusMessage(Status message, Socket socket)
			throws IOException
	{
		BigInteger id = message.getId();

		if (false == core.componentMonitorThread
				.informaAboutConnectedComponent(id))
		{
			Logger.log("Component not registered\n");
			XMLMessages.Error errorMessage = new Error();
			errorMessage.setErrorDetails("Component not registered");
			GenericProtocol.sendMessages(socket, errorMessage);
		}
		else
		{
			sendNoOperationMessage(new NoOperation(), socket);

			if (core.taskManagers.containsKey(id))
			{
				makeTaskManagerWorkHard(socket, core.taskManagers.get(id));
			}
			else if (core.computationalNodes.containsKey(id))
			{
				// TODO:
			}
		}
	}

	private void reactToSolveRequest(SolveRequest message, Socket socket)
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

	private void reactToPartialProblems(SolvePartialProblems message,
			Socket socket) throws IOException
	{
		ProblemInfo problem = core.problemsToSolve.get(message.getId());
		problem.isProblemDivided = true;

		for (PartialProblem p : message.getPartialProblems()
				.getPartialProblem())
		{
			problem.partialProblems.put(p.getTaskId(), p);
		}
		problem.isProblemCurrentlyDelegated = false;
	}

	private void sendNoOperationMessage(NoOperation noOperation, Socket socket)
			throws IOException
	{
		BackupCommunicationServers backupServers = new BackupCommunicationServers();

		if (null != core.backupServer)
		{
			backupServers.getBackupCommunicationServer().setAddress(
					core.backupServer.address);
			backupServers.getBackupCommunicationServer().setPort(
					core.backupServer.port);
		}
		noOperation.setBackupCommunicationServers(backupServers);

		GenericProtocol.sendMessages(socket, noOperation);
	}

	private synchronized void makeTaskManagerWorkHard(Socket socket,
			TaskManagerInfo taskManager) throws IOException
	{
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

			if (freeThreads == 0)
				break;

			if (problem.isProblemDivided && problem.isProblemReadyToSolve)
			{
				Logger.log("Sent problem " + problem.id
						+ " for final solution\n");
				sendSolutionRequest(socket,
						core.problemsToSolve.get(problem.id));
			}
			else
			{
				Logger.log("Sent problem " + problem.id + " for division\n");
				sendDivideProblemRequest(socket,
						core.problemsToSolve.get(problem.id));
			}
			taskManager.assignedProblems.add(problem.id);
			problem.isProblemCurrentlyDelegated = true;

			freeThreads--;
			removedId.add(problem.id);
		}

		for (BigInteger id : removedId)
			core.problemsToSolve.remove(id);
	}

	private void sendDivideProblemRequest(Socket socket, ProblemInfo problem)
			throws IOException
	{
		if (null != problem)
		{
			DivideProblem message = new DivideProblem();
			message.setComputationalNodes(new BigInteger(String
					.valueOf(core.computationalNodes.size())));
			message.setProblemType(problem.problemType);
			message.setId(problem.id);
			message.setData(problem.data);

			GenericProtocol.sendMessages(socket, message);
		}
	}

	private void sendSolutionRequest(Socket socket, ProblemInfo problem)
			throws IOException
	{
		if (null != problem)
		{
			Solutiones message = new Solutiones();
			message.setCommonData(problem.data);
			message.setId(problem.id);
			message.setProblemType(problem.problemType);
			// TODO: Set solutions data

			GenericProtocol.sendMessages(socket, message);
		}
	}
}
