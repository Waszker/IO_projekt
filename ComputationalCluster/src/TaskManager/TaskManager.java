package TaskManager;

import ComputationalServer.ComputationalServer;
import GenericCommonClasses.GenericComponent;

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
	
	public TaskManager(String address, Integer port,
			boolean isGuiEnabled)
	{
		super(isGuiEnabled);
		this.setServerIp(address);
		this.setServerPort(port != null ? port : ComputationalServer.DEFAULT_PORT);
	}
	
	public TaskManager(String address, Integer port)
	{
		super(false);
	}
}
