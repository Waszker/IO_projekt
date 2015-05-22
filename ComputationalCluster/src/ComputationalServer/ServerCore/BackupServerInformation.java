package ComputationalServer.ServerCore;

import java.math.BigInteger;

/**
 * <p>
 * Struct-like class used to store information about connected backup server.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 *
 */
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
	public BackupServerInformation(final BigInteger id, final Integer port,
			final String address)
	{
		super();
		this.id = id;
		this.port = port;
		this.address = address.substring(address.indexOf('/') + 1);
		this.indexOfLastUnSynchronizedMessage = 0;
	}
}
