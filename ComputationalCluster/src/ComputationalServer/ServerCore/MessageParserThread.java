package ComputationalServer.ServerCore;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser;
import XMLMessages.NoOperation;
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
				reactToMessage(receivedMessage, new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())),
						socket);
			}

			socket.close();
		}
		catch (IOException | JAXBException e)
		{
		}
	}

	private void reactToMessage(IMessage message, BufferedWriter out,
			Socket client) throws JAXBException, IOException
	{
		switch (message.getMessageType())
		{
			case REGISTER:
				RegisterResponse registerResponse = new RegisterResponse();
				registerResponse.setId(new BigInteger(core.getCurrentFreeId()
						.toString()));
				registerResponse.setTimeout(core.timeout);
				sendMessages(out, registerResponse);
				break;

			case STATUS:
				NoOperation noOperation = new NoOperation();
				noOperation.setBackupCommunicationServers(null);
				sendMessages(out, noOperation);
				break;

			default:
				Logger.log("Unsupported message " + message.getString()
						+ "\n\n");
				break;
		}
	}

	private void sendMessages(BufferedWriter out, IMessage... messages)
			throws IOException
	{
		try
		{
			for (IMessage m : messages)
				out.write(m.getString() + IMessage.ETB);
		}
		catch (JAXBException e)
		{
		}
		out.write(IMessage.ETX);
		out.flush();
	}
}