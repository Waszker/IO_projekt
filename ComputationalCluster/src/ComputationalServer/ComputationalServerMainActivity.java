package ComputationalServer;

import java.io.IOException;
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
							.get(GenericFlagInterpreter.FLAG_MPORT),
					(String) flagsMap.get(GenericFlagInterpreter.FLAG_MADDRESS),
					flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);

			if (flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI) != null)
			{
				ComputationalServerWindow window = new ComputationalServerWindow(
						server);
				window.setVisible(true);
			}
			else
			{
				// we don't have window here
				server.startWork(null);
			}
		}
		catch (NumberFormatException | IndexOutOfBoundsException | IOException e)
		{
			Logger.log("Incorrectly formatted flags.\n");
			Logger.log("Usage: java -jar ComponentName.jar "
					+ "[-port [port number on which server will listen]] "
					+ "[-backup -maddress [address of master server]"
					+ "-mport [port number of master server]]\n");
			Logger.log("Optional configuration file should be named "
					+ GenericFlagInterpreter.CONFIGURATION_FILE
					+ " and should be placed in root directory of .jar file.\n");
		}
	}
}
