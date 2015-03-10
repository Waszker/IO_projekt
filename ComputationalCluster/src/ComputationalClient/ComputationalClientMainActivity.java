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
			ComputationalClient client = new ComputationalClient(
					(InetAddress) flagsMap.get("address"),
					(Integer) flagsMap.get("port"));

			if (flagsMap.get("isGui") != null)
			{
				client.setGuiEnabled(true);
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			} else
			{
				client.setGuiEnabled(false);
				client.startWork();
			}

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
