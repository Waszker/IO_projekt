package ComputationalClient;

import java.net.InetAddress;
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

			ComputationalClientWindow window = new ComputationalClientWindow(
					(InetAddress) flagsMap.get("address"),
					(Integer) flagsMap.get("port"));

			if (flagsMap.get("isGui") != null)
			{
				window.setVisible(true);
			} else
			{
				window.startWork();
			}

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
