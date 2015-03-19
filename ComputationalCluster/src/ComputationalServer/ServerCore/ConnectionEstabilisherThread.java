package ComputationalServer.ServerCore;

import java.io.IOException;
import java.net.Socket;

import GenericCommonClasses.GenericProtocol;

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
				Socket clientSocket = core.serverSocket.accept();
				retrieveClientMessage(clientSocket);
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
					core.messageQueue.add(new ClientMessage(GenericProtocol
							.receiveMessage(clientSocket), clientSocket));
					core.queueSemaphore.release();
				}
				catch (IOException e)
				{
				}
				// do not close input stream here
				// it should be done in message parsing routine!

			}
		}).start();
	}
}
