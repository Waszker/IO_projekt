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
					flagsMap.get("isBackup") == null);

			if (flagsMap.get("isGui") != null)
			{
				window.setVisible(true);
			}

		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
