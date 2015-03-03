package GenericCommonClasses;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

public abstract class GenericFlagInterpreter
{

	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/

	/**
	 * <p>
	 * Function interpret flags that were given to program at start. It returns
	 * map containing information provided by user during startup.
	 * </p>
	 * 
	 * @param args array of arguments provided during startup
	 * @return map containing information in proper way
	 * @throws UnknownHostException
	 */
	public static Map<String, Object> interpretFlags(String[] args)
			throws UnknownHostException
	{
		Map<String, Object> flagMap = new TreeMap<>();

		// TODO: Add error handling!
		for (int i = 0; i < args.length; i += 2)
		{
			switch (args[i])
			{
				case "-port":
					flagMap.put("port", Integer.parseInt(args[i + 1]));
					break;

				case "-address":
					flagMap.put("address", InetAddress.getByName(args[i + 1]));
					break;

				case "-t":
					flagMap.put("timeout", Integer.parseInt(args[i + 1]));
					break;

				case "-backup":
					flagMap.put("isBackup", true);
					i--;
					break;

				case "-gui":
					flagMap.put("isGui", true);
					i--;
					break;
			}
		}

		return flagMap;
	}
}
