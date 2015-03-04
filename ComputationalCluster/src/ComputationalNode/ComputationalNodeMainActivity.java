package ComputationalNode;

import java.net.UnknownHostException;
import java.util.Map;

import GenericCommonClasses.GenericFlagInterpreter;

/**
 * 
 * @author Monika ¯urkowska
 * 
 *         <p>
 *         Main class of Computational Node
 *         </p>
 */
public class ComputationalNodeMainActivity
{
	public static void main(String[] args)
	{
		try
		{
			ComputationalNodeWindow mainWindow = new ComputationalNodeWindow();
			Map<String, Object> flagsMap = GenericFlagInterpreter
					.interpretFlags(args);
			if (flagsMap.get("isGui") != null)
			{
				mainWindow.setVisible(true);
			}
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
