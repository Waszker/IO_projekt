package TaskManager;

import java.net.UnknownHostException;
import java.util.Map;
import GenericCommonClasses.GenericFlagInterpreter;


/**
 * @author Filip Turkot
 * @version 1.0
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
			if (flagsMap.get("isGui") != null)
			{
				TaskManagerWindow mainWindow = new TaskManagerWindow();
				mainWindow.setVisible(true);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
