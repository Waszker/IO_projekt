package ComputationalClient;

import java.net.UnknownHostException;
import java.util.Map;

import DebugTools.Logger;
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

			if (fileName == null && isGuiEnabled==false)
			{
				Logger.log("You need to specify a file\n");
				return;
			}

			ComputationalClient client = new ComputationalClient(serverIp,
					serverPort, isGuiEnabled, fileName, timeout);

			if (isGuiEnabled)
			{
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			} else
			{
				client.sendSolveRequestMessage();
				client.sendSolutionRequestMessage();
			}
		} catch (NumberFormatException | UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
