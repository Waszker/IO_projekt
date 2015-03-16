package ComputationalNode;

import XMLMessages.RegisterMessage;
import GenericCommonClasses.GenericComponent;

public class ComputationalNode extends GenericComponent
{

	public ComputationalNode(String serverIpAddress, Integer serverPort,
			boolean isGui)
	{
		super(serverIpAddress, serverPort, isGui,
				ComponentType.ComputationalNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RegisterMessage getComponentRegisterMessage()
	{
		// TODO: Change that!
		return new RegisterMessage(-1, getType(), false, null, 1);
	}

}
