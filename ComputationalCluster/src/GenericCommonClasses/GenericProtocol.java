package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
		if (null != messages)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connectionSocket.getOutputStream()));
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

	/**
	 * <p>
	 * Receives message from connection socket.
	 * </p>
	 * 
	 * @return received message
	 * @throws IOException
	 */
	public static IMessage receiveMessage(Socket connectionSocket)
			throws IOException
	{
		int readChar;
		StringBuilder messageBuilder = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));

		while ((readChar = in.read()) != -1)
		{
			if (readChar == IMessage.ETB)
				break;
			messageBuilder.append((char) readChar);
		}

		return Parser.parse(messageBuilder.toString());
	}

	private GenericProtocol()
	{
	}
}
