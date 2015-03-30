package ComputationalNode;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

/**
 * 
 * @author Monika �urkowska
 * 
 *         <p>
 *         Main class of Computational Node
 *         </p>
 */
public class ComputationalNodeMainActivity
{
	public static void main(String[] args)
	{
		Map<String, Object> flagsMap;
		try
		{
			flagsMap = GenericFlagInterpreter.interpretFlags(args);

			boolean isGuiEnabled = (flagsMap
					.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);
			String serverIp = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_ADDRESS);
			 //String serverIp = "192.168.143.94";

			Integer serverPort = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_PORT);
			 //Integer serverPort = 50000;

			ComputationalNode computationalNode = new ComputationalNode(
					serverIp, serverPort, isGuiEnabled);
			if (flagsMap.get("isGui") != null)
			{
				ComputationalNodeWindow window = new ComputationalNodeWindow(
						computationalNode);
				window.setVisible(true);
			}
			else if (serverIp == null || serverPort == null)
			{
				usage();
			}
			else
			{
				computationalNode.connectToServer();
			}
		} catch (NumberFormatException | UnknownHostException | IndexOutOfBoundsException e)
		{
			// TODO Auto-generated catch block
			usage();
			e.printStackTrace();
		}
	}
	
	static void usage()
	{
		System.err.println("USAGE:");
		System.err.println("$java -jar CN.jar -port <port_number> -address <ip_addresss>");
		System.exit(-1);
	}

}
