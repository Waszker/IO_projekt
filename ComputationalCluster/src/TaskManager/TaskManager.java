package TaskManager;

import XMLMessages.Register;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * TaskManager is a class providing the TaskManager logic.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.0
 */
public final class TaskManager extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/

	public TaskManager(String address, Integer port, boolean isGuiEnabled)
	{
		super(address, port, isGuiEnabled, ComponentType.TaskManager);
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register r = new Register();
		r.setType("TM");
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		showMessage(message.toString());
		if ( message.getMessageType() == MessageType.REGISTER_RESPONSE )
		{
			Register r = (Register)message;
			showMessage("Your id is: " + r.getId().toString());
		}
	}
}
