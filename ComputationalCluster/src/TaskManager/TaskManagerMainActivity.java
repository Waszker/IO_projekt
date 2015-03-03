package TaskManager;

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
			TaskManagerWindow mainWindow = new TaskManagerWindow();
			Map<String, Object> flagsMap = GenericFlagInterpreter.interpretFlags(args);
			if (flagsMap.get("isGui") != null)
			{
				mainWindow.setVisible(true);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}
