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

			ComputationalClientWindow window = new ComputationalClientWindow();

			if (flagsMap.get("isGui") != null)
			{
				window.setVisible(true);
			}

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
