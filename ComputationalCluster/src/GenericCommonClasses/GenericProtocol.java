package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

/**
 * <p>
 * GenericProtocol class cannot be instantiated and provides two methods: send
 * and receive Messages.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public final class GenericProtocol
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Sends the messages to server. This method creates connection before
	 * sending messages.
	 * </p>
	 * 
	 * @param message
	 * @throws IOException
	 */
	public static void sendMessages(Socket connectionSocket,
			IMessage... messages) throws IOException
	{
		if (null != messages && connectionSocket != null)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connectionSocket.getOutputStream()));

			for (IMessage m : messages)
			{
				try
				{
					out.write(m.getString() + IMessage.ETB);
				}
				catch (JAXBException e)
				{
				}
			}
			out.write(IMessage.ETX);
			out.flush();
		}
	}

	/**
	 * <p>
	 * Receives message from connection socket.
	 * </p>
	 * 
	 * @return received message
	 * @throws IOException
	 */
	public static List<IMessage> receiveMessage(Socket connectionSocket)
			throws IOException
	{
		int readChar;
		List<IMessage> messages = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));
		StringBuilder messageBuilder;

		do
		{
			messageBuilder = new StringBuilder();
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

			if (null != messageBuilder)
			{
				messages.add(Parser.parse(messageBuilder.toString()));
			}
		} while (null != messageBuilder);

		return messages;
	}

	private GenericProtocol()
	{
	}
}
