package GenericCommonClasses;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <p>
 * GenericComponent class is a base class for every ComputationalClient,
 * TaskManager and ComputationalNode class. It holds fields common for all three
 * classes and gathers common methods usable in them.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	protected GenericConnector connector;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Connects to server at given ip and port.
	 * </p>
	 * 
	 * @param serverIp
	 * @param port
	 * @param isGuiEnabled
	 */
	public void connectToServer(final String serverIp, final Integer port,
			boolean isGuiEnabled)
	{
		if (connector != null)
		{
			connector.connectToServer(serverIp, port, isGuiEnabled);
		}
		else
		{
			if (isGuiEnabled)
			{
				JOptionPane
						.showMessageDialog(
								new JFrame(),
								"No connector specified\nAttach connector variable in your class!",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				System.err
						.println("No connector specified\nAttach connector variable in your class!");
			}
		}
	}
}
