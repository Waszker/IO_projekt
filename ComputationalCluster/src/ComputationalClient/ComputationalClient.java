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
	private String address;
	private boolean isGuiEnabled;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param address
	 *            server IP address
	 * @param port
	 *            on which server should listen to connection (default is
	 *            '47777')
	 */
	public ComputationalClient(String address, Integer port)
	{
		this.address = address;
		this.port = (null == port ? DEFAULT_PORT : port);
	}

	public ComputationalClient(InetAddress address, Integer port)
	{
		String iP = address.getHostAddress();
		this.address = iP;
		this.port = (null == port ? DEFAULT_PORT : port);
	}

	public void startWork()
	{
		
		connector=new ComputationalClientConnector();
		connector.connectToServer(this.address,this.port, false );
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
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
