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

			TaskManager taskManager = new TaskManager(serverIp, serverPort,
					isGuiEnabled);
			if (isGuiEnabled)
			{
				TaskManagerWindow mainWindow = new TaskManagerWindow(
						taskManager);
				mainWindow.setVisible(true);
			}
		}
		catch (NumberFormatException | UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
