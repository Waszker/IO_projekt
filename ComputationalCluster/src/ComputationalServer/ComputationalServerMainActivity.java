package ComputationalServer;

import java.net.UnknownHostException;
import java.util.Map;

import DebugTools.Logger;
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
					(Integer) flagsMap
							.get(GenericFlagInterpreter.FLAG_BACKUP_PORT),
					(String) flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS),
					flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);

			if (flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null)
			{
				ComputationalServerWindow window = new ComputationalServerWindow(
						server);
				window.setVisible(true);
			} else
			{
				// we don't have window here
				server.startWork(null);
			}
		}
		catch (NumberFormatException | UnknownHostException | IndexOutOfBoundsException e)
		{
			//e.printStackTrace();
			Logger.log("Incorrectly formatted flags.\n"
					+ "Usage: java -jar ComputationalServer.jar "
					+ "[-port [port number]] [-backup] "
					+ "[-t [time in seconds]] "
					+ "[-backup-port [port number for backup server]]\n");
		}
	}
}
