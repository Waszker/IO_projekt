package ComputationalClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import ComputationalServer.ComputationalServer;
import ComputationalServer.ComputationalServerWindow;
import GenericCommonClasses.GenericFlagInterpreter;

public class ComputationalClientMainActivity
{

	public static void main(String[] args)
	{
		Map<String, Object> flagsMap;
		try
		{
			flagsMap = GenericFlagInterpreter.interpretFlags(args);
			ComputationalClient client = new ComputationalClient(
					(InetAddress) flagsMap.get("address"),
					(Integer) flagsMap.get("port"));/*,
					(boolean) flagsMap.get("isGui"));*/

			if (flagsMap.get("isGui") != null)
			{
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			} else
			{
				client.startWork();
			}

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
