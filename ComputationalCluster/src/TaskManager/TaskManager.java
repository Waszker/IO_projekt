package TaskManager;

import java.net.InetAddress;

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
	
	public static final int DEFAULT_PORT = 47777;

	private int port;
	private InetAddress address;
	private boolean isGuiEnabled;
	
	
	/******************/
	/* FUNCTIONS */
	/******************/
	
	public TaskManager(InetAddress address, Integer port,
			boolean isGuiEnabled)
	{
		this.address = address;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.isGuiEnabled = true;
	}
	
	public TaskManager(InetAddress address, Integer port)
	{
		this.address = address;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.isGuiEnabled = false;
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
		this.connectToServer(serverIp, port, isGuiEnabled);
		return false;
	}
}
