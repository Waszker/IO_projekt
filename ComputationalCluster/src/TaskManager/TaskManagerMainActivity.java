package TaskManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;


/**
 * @author Filip Turkot
 * 
 * <p>
 * Main class of Task Manager.
 * </p>
 */
public class TaskManagerMainActivity
{
	public static void main(String[] args)
	{
		try
		{
			Map<String, Object> flagsMap = GenericFlagInterpreter.interpretFlags(args);
			boolean isGuiEnabled = (flagsMap.get("isGui") != null);
			String serverIp = (String)flagsMap.get("address");
			Integer serverPort = (Integer)flagsMap.get("port");
			
			TaskManager taskManaager = new TaskManager(serverIp, serverPort, isGuiEnabled);
			if (isGuiEnabled)
			{
				TaskManagerWindow mainWindow = new TaskManagerWindow(/*taskManager*/);
				mainWindow.setVisible(true);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
