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
	int indexOfLastUnSynchronizedMessage;

	/******************/
	/* FUNCTIONS */
	/******************/
	public BackupServerInformation(BigInteger id, Integer port, String address)
	{
		super();
		this.id = id;
		this.port = port;
		this.address = address.substring(address.indexOf('/') + 1);
		this.indexOfLastUnSynchronizedMessage = 0;
	}
}
