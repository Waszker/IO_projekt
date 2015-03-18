package ComputationalServer.ServerCore;

import java.math.BigInteger;

class BackupServerInformation
{
	/******************/
	/* VARIABLES */
	/******************/
	BigInteger id;
	Integer port;
	String address;
	
	/******************/
	/* FUNCTIONS */
	/******************/
	public BackupServerInformation(BigInteger id, Integer port, String address)
	{
		super();
		this.id = id;
		this.port = port;
		this.address = address;
	}		
}
