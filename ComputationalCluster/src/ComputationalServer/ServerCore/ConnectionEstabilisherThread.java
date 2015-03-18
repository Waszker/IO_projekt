package ComputationalServer.ServerCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import GenericCommonClasses.IMessage;

/**
 * <p>
 * ConnectionEstabilisherThread is responsible for asynchronous connection
 * estabilishment and asynchronous message retrieval.
 * </p>
 * <p>
 * Retrieved message is put into message queue and processes by other
 * ComputationalServer threads.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class ConnectionEstabilisherThread extends Thread
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
	 * Create ConnectionEstabilisherThread with reference to server core.
	 * </p>
	 * 
	 * @param core
	 */
	ConnectionEstabilisherThread(ComputationalServerCore core)
	{
		super();
		this.core = core;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				core.clientSocket = core.serverSocket.accept();
				retrieveClientMessage(core.clientSocket);
			}
			catch (IOException e)
			{
			}
		}
	}

	private void retrieveClientMessage(final Socket clientSocket)
	{
		// Start procedure in new thread to not block other connections
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					String message;
					while (null != (message = receiveMessage(clientSocket)))
					{
						core.messageQueue.add(new ClientMessage(message,
								clientSocket));
						core.queueSemaphore.release();
					}
				}
				catch (IOException e)
				{
				}
				// do not close input stream here
				// it should be done in message parsing routine!

			}
		}).start();
	}

	private String receiveMessage(Socket connectionSocket) throws IOException
	{
		// TODO: This will be problem because we share socket inside many messages...
		int readChar;
		StringBuilder messageBuilder = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));

		while ((readChar = in.read()) != -1)
		{
			if (readChar == IMessage.ETB)
				break;
			if (readChar == IMessage.ETX)
			{
				messageBuilder = null;
				break;
			}
			messageBuilder.append((char) readChar);
		}

		return (null == messageBuilder ? null : messageBuilder.toString());
	}
}
