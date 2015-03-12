package ComputationalClient;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

public class ComputationalClientMainActivity
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
			ComputationalClient client = new ComputationalClient(serverIp,
					serverPort, isGuiEnabled);

			if (flagsMap.get("isGui") != null)
			{
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			}
			else
			{
				client.connectToServer();
			}
		}
		catch (NumberFormatException | UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
