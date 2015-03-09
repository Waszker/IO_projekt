package ComputationalClient;

import java.io.IOException;
import java.net.InetAddress;

import ComputationalServer.ComputationalServerCore;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericConnector;

public class ComputationalClient extends GenericComponent
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
	/**
	 * <p>
	 *   ComputationalClient is a class providing the Computational Client logic.
	 * </p>
	 * 
	 * @param address
	 *            server IP address
	 * @param port
	 *            on which server should listen to connection (default is
	 *            '47777')
	 * @param isGuiEnabled
	 *            determines if gui window will be displayed
	 */
	public ComputationalClient(InetAddress address, Integer port,
			boolean isGuiEnabled)
	{
		this.address = address;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.isGuiEnabled = true;
	}
	
	public ComputationalClient(InetAddress address, Integer port)
	{
		this.address = address;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.isGuiEnabled = false;
	}

	public void startWork()
	{		
		String IP = address.getHostName();
		this.connectToServer(IP, port, isGuiEnabled);	
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public void setAddress(InetAddress address)
	{
		this.address = address;
	}

	public boolean isGuiEnabled()
	{
		return isGuiEnabled;
	}

	public void setGuiEnabled(boolean isGuiEnabled)
	{
		this.isGuiEnabled = isGuiEnabled;
	}

}
