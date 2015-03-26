package ComputationalClient;

import java.math.BigInteger;
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
			String fileName = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_FILE);
			Integer timeout = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_TIMEOUT);
			
			ComputationalClient client = new ComputationalClient(serverIp,
					serverPort, isGuiEnabled, fileName, timeout);

			if (isGuiEnabled)
			{
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			}
			else
			{
				client.connectToServer();
				client.sendSolveRequestMessage();
			}
		}
		catch (NumberFormatException | UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
