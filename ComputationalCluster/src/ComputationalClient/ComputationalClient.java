package ComputationalClient;

import GenericCommonClasses.GenericComponent;
import XMLMessages.Register;

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
	protected Register getComponentRegisterMessage()
	{
		// TODO: Change that!
		return new Register();
	}
}
