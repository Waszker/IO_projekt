package ComputationalServer.ServerCore;

import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.Error;
import XMLMessages.Register;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolveRequest;
import XMLMessages.Status;
import XMLMessages.Error.ErrorMessage;

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
	private CommunicationThread communicationThread;

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
		communicationThread = new CommunicationThread(core);
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
				communicationThread.reactToRegisterMessage((Register) message,
						socket);
				break;

			case STATUS:
				communicationThread.reactToStatusMessage((Status) message,
						socket);
				break;

			case SOLVE_REQUEST:
				communicationThread.reactToSolveRequest((SolveRequest) message,
						socket);
				break;

			case SOLUTION_REQUEST:
				communicationThread.reactToSolutionRequest(
						(SolutionRequest) message, socket);
				break;

			case PARTIAL_PROBLEM:
				communicationThread.reactToPartialProblems(
						(SolvePartialProblems) message, socket);
				break;

			case SOLUTION:
				communicationThread.reactToSolution((Solutiones) message,
						socket);
				break;

			default:
				Logger.log("Unsupported message " + message.getString()
						+ "\n\n");
				XMLMessages.Error errorMessage = new Error();
				errorMessage.setErrorType(ErrorMessage.InvalidOperation);
				GenericProtocol.sendMessages(socket, errorMessage);
				break;
		}
		core.informAboutComponentChanges();
	}
}
