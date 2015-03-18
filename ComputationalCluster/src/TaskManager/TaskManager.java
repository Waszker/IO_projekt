package TaskManager;

import XMLMessages.Register;
import DebugTools.Logger;
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
		r.setType(ComponentType.TaskManager.name);
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		if ( message.getMessageType() == MessageType.NO_OPERATION )
			Logger.log("Your id is: "+id+"\n");
	}
}
