package ComputationalServer.ServerCore;

import java.util.ArrayList;
import java.util.List;
import java.net.Socket;

import GenericCommonClasses.IMessage;

/**
 * <p>
 * Client message differs from generic one in one thing: it has additional
 * Socket field that enables server to send the response to connected client.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class ClientMessage
{
	/******************/
	/* VARIABLES */
	/******************/
	private Socket socket;
	private List<IMessage> messages;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates client message with speicified client socket used to send
	 * response.
	 * </p>
	 * 
	 * @param content
	 * @param clientSocket
	 */
	public ClientMessage(List<IMessage> content, Socket clientSocket)
	{
		socket = clientSocket;
		messages = new ArrayList<>(content);
	}

	/**
	 * <p>
	 * Returns client socket.
	 * </p>
	 * 
	 * @return client socket
	 */
	public Socket getClientSocket()
	{
		return socket;
	}
	
	/**
	 * <p>
	 * Returns contents of messages.
	 * </p>
	 * 
	 * @return copy of messages
	 */
	public List<IMessage> getMessageContents()
	{
		return new ArrayList<IMessage>(messages);
	}
}
