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
					flagsMap.get("isBackup") != null,
					(Integer) flagsMap.get("port"),
					(Integer) flagsMap.get("timeout"));

			if (flagsMap.get("isGui") != null)
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
		catch (UnknownHostException | NumberFormatException e)
		{
			e.printStackTrace();
			System.err
					.println("Incorrectly formatted flags.\nProgram exiting.");
		}
	}
}
