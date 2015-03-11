package ComputationalNode;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

/**
 * 
 * @author Monika �urkowska
 * 
 *         <p>
 *         Main class of Computational Node
 *         </p>
 */
public class ComputationalNodeMainActivity
{
	public static void main(String[] args)
	{
		Map<String, Object> flagsMap;
		try
		{
			flagsMap = GenericFlagInterpreter.interpretFlags(args);

			boolean isGuiEnabled = (flagsMap
					.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);
			String serverIp = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_ADDRESS);
			Integer serverPort = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_PORT);

			ComputationalNode computationalNode = new ComputationalNode(
					serverIp, serverPort, isGuiEnabled);
			if (isGuiEnabled)
			{
				ComputationalNodeWindow mainWindow = new ComputationalNodeWindow(
						computationalNode);
				mainWindow.setVisible(true);
			}
		}
		catch (NumberFormatException | UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
