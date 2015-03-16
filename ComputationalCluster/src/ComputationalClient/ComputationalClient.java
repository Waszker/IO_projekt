package ComputationalClient;

import XMLMessages.RegisterMessage;
import GenericCommonClasses.GenericComponent;

public class ComputationalClient extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClient(String address, Integer port,
			boolean isGuiEnabled)
	{
		super(address, port, isGuiEnabled, ComponentType.ComputationalClient);
	}

	@Override
	protected RegisterMessage getComponentRegisterMessage()
	{
		// TODO: Change that!
		return new RegisterMessage(-1, getType(), false, null, 1);
	}
}
