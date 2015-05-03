package GenericCommonClasses;

import java.io.IOException;
import java.math.BigInteger;
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
	 * Returns id of the problem that is associated with this message.
	 * Otherwise, returns null.
	 * </p>
	 * 
	 * @return
	 */
	public BigInteger getProblemId();

	/**
	 * <p>
	 * Generates response messages and sends responses. It can also return some
	 * "delayed" responses for computational server to save.
	 * </p>
	 * 
	 * @param serverProtocol
	 * @param socket
	 * @return delayedResponses - messages that need specific situation to be
	 *         sent
	 */
	public List<IMessage> prepareResponse(IServerProtocol serverProtocol,
			Socket socket) throws IOException;
}
