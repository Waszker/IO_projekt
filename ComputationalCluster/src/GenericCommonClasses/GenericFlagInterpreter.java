package GenericCommonClasses;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * GenericClassInterpreter class performs flag validation checks and returns
 * them inside Map object.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class GenericFlagInterpreter
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final String CONFIGURATION_FILE = ".ccluster.ini";
	public static final String FLAG_PORT = "port";
	public static final String FLAG_ADDRESS = "address";
	public static final String FLAG_TIMEOUT = "timeout";
	public static final String FLAG_IS_BACKUP = "isBackup";
	public static final String FLAG_BACKUP_PORT = "portBackup";
	public static final String FLAG_IS_GUI = "isGui";
	public static final String FLAG_FILE = "file";

	/******************/
	/* FUNCTIONS */
	/******************/

	/**
	 * <p>
	 * Function interpret flags that were given to program at start. It returns
	 * map containing information provided by user during startup.
	 * </p>
	 * <p>
	 * In case of many same flags specified, only the last one has the effect.
	 * </p>
	 * 
	 * @param args
	 *            array of arguments provided during startup
	 * @return map containing information in proper way
	 * @throws IOException
	 *             if no flags and no configuration file was provided
	 */
	public static Map<String, Object> interpretFlags(String[] args)
			throws NumberFormatException, IOException
	{
		Map<String, Object> flagMap = new TreeMap<>();

		// TODO: Add error handling!
		for (int i = 0; i < args.length; i += 2)
		{
			switch (args[i])
			{
				case "-port":
					flagMap.put(FLAG_PORT, Integer.parseInt(args[i + 1]));
					break;

				case "-backup-port":
					flagMap.put(FLAG_BACKUP_PORT, Integer.parseInt(args[i + 1]));
					break;

				case "-address":
					InetAddress.getByName(args[i + 1]);
					flagMap.put(FLAG_ADDRESS, args[i + 1]);
					break;

				case "-t":
					flagMap.put(FLAG_TIMEOUT, Integer.parseInt(args[i + 1]));
					break;

				case "-backup":
					flagMap.put(FLAG_IS_BACKUP, true);
					i--;
					break;

				case "-gui":
					flagMap.put(FLAG_IS_GUI, true);
					i--;
					break;

				case "-file":
					flagMap.put(FLAG_FILE, args[i + 1]);
					i--;
					break;
					
				case "-help":
					GenericComponent.showUsage();
					System.exit(0);
					break;

				default:
					throw new IOException();
			}
		}

		return (args.length == 0 ? loadFromConfig() : flagMap);
	}

	private static Map<String, Object> loadFromConfig() throws IOException
	{
		StringBuilder stringBuilder = new StringBuilder();
		Files.lines(FileSystems.getDefault().getPath(CONFIGURATION_FILE))
				.forEach(s -> stringBuilder.append(s));

		return interpretFlags(stringBuilder.toString().split(" "));
	}
}
