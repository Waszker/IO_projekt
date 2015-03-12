package ComputationalNode;

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

}
