package TaskManager;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

/**
 * @author Filip Turkot
 * 
 *         <p>
 *         Main class of Task Manager.
 *         </p>
 */
public class TaskManagerMainActivity
{
	static void usage()
	{
		System.err.println("usage: $java -jar TM.jar -port <port_number> -addres <ip_address>");
		System.exit(-1);
	}
	
	public static void main(String[] args)
	{
		try
		{
			Map<String, Object> flagsMap = GenericFlagInterpreter
					.interpretFlags(args);
			boolean isGuiEnabled = (flagsMap
					.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);
			String serverIp = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_ADDRESS);
			Integer serverPort = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_PORT);
			
			if ( serverIp == null )usage();
			if ( serverPort == null )usage();

			TaskManager taskManager = new TaskManager(serverIp, serverPort,
					isGuiEnabled);
			if (isGuiEnabled)
			{
				TaskManagerWindow mainWindow = new TaskManagerWindow(
						taskManager);
				mainWindow.setVisible(true);
			}
			
			else
			{
				taskManager.connectToServer();
			}
		}
		catch (NumberFormatException|UnknownHostException e)
		{
			usage();
		}
	}
}
