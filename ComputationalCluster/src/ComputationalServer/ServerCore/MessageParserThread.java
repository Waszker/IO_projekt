package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
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

			default:
				Logger.log("Unsupported message " + message.getString()
						+ "\n\n");
				// TODO: Send Error message
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

		RegisterResponse registerResponse = new RegisterResponse();
		registerResponse.setId(id);
		registerResponse.setTimeout(core.timeout);
		GenericProtocol.sendMessages(socket, registerResponse);
	}

	private void reactToStatusMessage(Status message, Socket socket)
			throws IOException
	{
		if (false == core.componentMonitorThread
				.informaAboutConnectedComponent(message.getId()))
		{
			// TODO: Send error message
		}
		else
		{
			// TODO: Add other reactions
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

			GenericProtocol.sendMessages(socket, noOperation);

		}
	}
}
