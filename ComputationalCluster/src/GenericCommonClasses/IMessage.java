package GenericCommonClasses;

import java.net.Socket;
import java.util.List;

import javax.xml.bind.JAXBException;

import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Interface providing basic functionality for every message send within
 * Computational Cluster. Every message which is send or received from each node
 * should implement this interface.
 * </p>
 * 
 * @author Monika Żurkowska
 * @version 1.0
 * @version 1.1 > added prepareResponse() method > coder: Piotr Waszkiewcz
 * 
 */
public interface IMessage
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final char ETX = 3;
	public static final char ETB = 23;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Converts message to String format.
	 * </p>
	 * 
	 * @returns string message
	 */
	public String getString() throws JAXBException;

	/**
	 * <p>
	 * Gets the type of message.
	 * </p>
	 * 
	 * @return type of message
	 */
	public MessageType getMessageType();

	/**
	 * <p>
	 * Generates response messages and informs server about changes.
	 * </p>
	 * 
	 * @param serverProtocol
	 * @param socket
	 * @return delayedResponses
	 *            - messages that need specific situation to be sent
	 */
	public List<IMessage> prepareResponse(IServerProtocol serverProtocol,
			Socket socket);
}
