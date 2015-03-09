package ComputationalServer;

import java.net.Socket;

import GenericCommonClasses.GenericMessage;

/**
 * <p>
 * Client message differs from generic one in one thing: it has additional
 * Socket field that enables server to send the response to connection module.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class ClientMessage extends GenericMessage
{
	/******************/
	/* VARIABLES */
	/******************/
	private Socket socket;

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
		super(content);
		socket = clientSocket;
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

}
