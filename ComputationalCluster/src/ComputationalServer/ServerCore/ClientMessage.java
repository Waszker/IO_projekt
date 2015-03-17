package ComputationalServer.ServerCore;

import java.net.Socket;

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
	private String messageContent;

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
	public ClientMessage(String content, Socket clientSocket)
	{
		socket = clientSocket;
		messageContent = content;
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
	 * Returns content of message
	 * </p>
	 * 
	 * @return copy of message
	 */
	public String getMessageContent()
	{
		return messageContent;
	}
}
