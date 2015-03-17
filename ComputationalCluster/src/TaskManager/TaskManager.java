package TaskManager;

import XMLMessages.Register;
import XMLMessages.RegisterMessage;
import GenericCommonClasses.GenericComponent;

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
		// TODO: Change that!
		return new Register();
	}
}
