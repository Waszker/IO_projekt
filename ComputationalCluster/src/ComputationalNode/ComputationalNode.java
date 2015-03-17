package ComputationalNode;

import XMLMessages.Register;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;

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
	protected Register getComponentRegisterMessage()
	{
		// TODO: Change that!
		return new Register();
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		// TODO Auto-generated method stub
		
	}

}
