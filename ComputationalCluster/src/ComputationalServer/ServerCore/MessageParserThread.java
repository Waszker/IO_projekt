package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;

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

			Logger.log("Client [" + socket.getInetAddress()
					+ "] connected and sent message:\n"
					+ message.getMessageContent());
			IMessage receivedMessage = Parser
					.parse(message.getMessageContent());

			if (receivedMessage != null)
			{
				reactToMessage(receivedMessage, socket);
			}

			socket.close();
		}
		catch (IOException | JAXBException e)
		{
		}
	}

	private void reactToMessage(IMessage message, Socket socket) throws JAXBException, IOException
	{
		switch (message.getMessageType())
		{
			case REGISTER:
				RegisterResponse registerResponse = new RegisterResponse();
				BigInteger id = new BigInteger(core.getCurrentFreeId()
						.toString());
				Integer remotePort = socket.getPort();
				String remoteAddress = socket.getInetAddress().toString();

				// TODO: Remove that in the future
				if (null != ((Register) message).getType())
					registerClient((Register) message, id, remotePort,
							remoteAddress);
				registerResponse.setId(id);
				registerResponse.setTimeout(core.timeout);
				GenericProtocol.sendMessages(socket, registerResponse);
				break;

			case STATUS:
				reactToStatusMessage(socket);
				break;

			default:
				Logger.log("Unsupported message " + message.getString()
						+ "\n\n");
				// TODO: Send Error message
				break;
		}
	}

	private void registerClient(Register message, BigInteger id, Integer port,
			String address)
	{
		if (message.getType().contentEquals(
				GenericComponent.ComponentType.TaskManager.name))
		{
			Logger.log("TM Connected\n");
			core.taskManagers.add(id);
		} else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalNode.name))
		{
			Logger.log("CN Connected\n");
			core.computationalNodes.add(id);
		} else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalServer.name))
		{
			Logger.log("CS Connected\n");
			if (null != core.backupServer)
			{
				// TODO: Send error if `if` is bad
				core.backupServer = (new BackupServerInformation(id, port,
						address));
			}
		} else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalClient.name))
		{
			Logger.log("CC Connected\n");
			// Send error because CC cannot register
		}
	}

	private void reactToStatusMessage(Socket socket) throws IOException
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
