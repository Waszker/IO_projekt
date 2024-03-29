package ComputationalServer.ServerCore;

import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.Error;
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
		try
		{
			core.delayedMessages.addAll(message.prepareResponse(
					communicationThread, socket));
			core.informAboutComponentChanges();
		}
		catch (IOException e)
		{
		}
		catch (NullPointerException e)
		{
			Logger.log(e.getMessage() + "\n");
			sendErrorMessage(socket);
		}
	}

	private void sendErrorMessage(Socket socket) throws IOException
	{
		XMLMessages.Error errorMessage = new Error();
		errorMessage.setErrorType(ErrorMessage.InvalidOperation);
		GenericProtocol.sendMessages(socket, errorMessage);
	}
}
