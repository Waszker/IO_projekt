package GenericCommonClasses;

/**
 * <p>
 * Base class for every message sent within Computational Cluster. It provides
 * basic functionality required by every message received and sent between
 * components.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericMessage
{
	/******************/
	/* VARIABLES */
	/******************/
	private String messageContent;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates message with string content ready to be sent through network.
	 * </p>
	 * 
	 * @param content
	 */
	public GenericMessage(String content)
	{
		messageContent = content;
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
		return new String(messageContent);
	}

}
