package GenericCommonClasses;

import javax.xml.bind.JAXBException;

import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Interface providing basic functionality for every message send within
 * Computational Cluster. Every message which is send or received from each node
 * should implement this interface.
 * </p>
 * 
 * @author Monika ���urkowska
 * @version 1.0
 * 
 */
public interface IMessage
{
	/******************/
	/* VARIABLES */
	/******************/
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
	public MessageType getType();
}
