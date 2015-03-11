package XMLMessages;

import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Register message is sent when component wants to connect to the server.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class RegisterMessage implements IMessage
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	@Override
	public MessageType getType(String messageContent)
	{
		return MessageType.REGISTER;
	}

	@Override
	public String getString()
	{
		// TODO: Change that!
		return "Hello";
	}

}
