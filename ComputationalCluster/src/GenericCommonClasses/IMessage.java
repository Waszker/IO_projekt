package GenericCommonClasses;

import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Interface providing basic functionality for every message send within
 * Computational Cluster. Every message which is send or received from each node
 * should implement this interface.
 * </p>
 * 
 * @author Monika ¯urkowska
 * @version 1.0
 * 
 */
public interface IMessage
{
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
	public String toString();

	/**
	 * <p>
	 * Gets the type of message.
	 * </p>
	 * 
	 * @param messageContent
	 *            - message which type we want to check
	 * @return type of message
	 */
	public MessageType getType(String messageContent);
}
