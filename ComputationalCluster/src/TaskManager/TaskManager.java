package TaskManager;

import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericConnector;

/**
 * <p>
 * TaskManager is a class providing the TaskManager logic.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.0
 */
public final class TaskManager extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	
	
	
	/******************/
	/* FUNCTIONS */
	/******************/
	
	public TaskManager()
	{
		super();
		// connector = new Connector();
		//TODO: Add connector class
	}
	
	
	/**
	 * <p> Establishes connection to specified server using GenericConnector class <p>
	 * @see GenericConnector
	 * @param serverIp
	 * 			ip address of target server
	 * @param port
	 * 			target port; if null the method uses default value: 47777
	 * @return
	 * 			true if connection is properly established; otherwise returns false
	 */
	public boolean connectToServer(final String serverIp, final Integer port)
	{
		connector.connectToServer(serverIp, port);
		return false;
	}
}
