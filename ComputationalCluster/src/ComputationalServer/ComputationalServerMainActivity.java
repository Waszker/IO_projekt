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

			ComputationalServerWindow window = new ComputationalServerWindow(
					flagsMap.get("isBackup") != null,
					(Integer) flagsMap.get("port"),
					(Integer) flagsMap.get("timeout"));

			if (flagsMap.get("isGui") != null)
			{
				window.setVisible(true);
			}
			else
			{
				window.startWork();
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
