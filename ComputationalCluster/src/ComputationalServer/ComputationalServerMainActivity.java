package ComputationalServer;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

public class ComputationalServerMainActivity
{
	public static void main(String[] args)
	{
		Map<String, Object> flagsMap;
		try
		{
			flagsMap = GenericFlagInterpreter.interpretFlags(args);

			ComputationalServer server = new ComputationalServer(
					flagsMap.get(GenericFlagInterpreter.FLAG_IS_BACKUP) != null,
					(Integer) flagsMap.get(GenericFlagInterpreter.FLAG_PORT),
					(Integer) flagsMap.get(GenericFlagInterpreter.FLAG_TIMEOUT),
					(String) flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS),
					flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);

			if (flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null)
			{
				ComputationalServerWindow window = new ComputationalServerWindow(
						server);
				window.setVisible(true);
			}
			else
			{
				server.startWork();
			}
		}
		catch (NumberFormatException | UnknownHostException e)
		{
			e.printStackTrace();
			System.err
					.println("Incorrectly formatted flags.\nProgram exiting.");
		}
	}
}
