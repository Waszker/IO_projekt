package ComputationalServer;

import java.io.IOException;

/**
 * <p>
 * ComputationalServer class is responsible for wrapping all server
 * responsibilities in one place. It consists of communication module ( @see
 * ComputationalServerCore ).
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class ComputationalServer
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final int DEFAULT_PORT = 47777;
	public static final int DEFAULT_TIMEOUT = 30;

	private boolean isBackup;
	private int port, timeout;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * ComputationalServer starts listening for connections from clients on
	 * certain port after calling startWork() method. It can run in primary or
	 * backup mode.
	 * </p>
	 * 
	 * @param isBackup
	 *            is server working in backup or primary mode (default is
	 *            primary)
	 * @param port
	 *            on which server should listen to connection (default is
	 *            '47777')
	 * @param timeout
	 *            in seconds after which server will mark connected module as
	 *            inactive and disconnected (default is '30 seconds')
	 */
	public ComputationalServer(boolean isBackup, Integer port, Integer timeout)
	{
		this.isBackup = isBackup;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.timeout = (null == timeout ? DEFAULT_TIMEOUT : timeout);
	}

	/**
	 * <p>
	 * Creates thread that starts listening for messages.
	 * </p>
	 */
	public void startWork()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				System.out
						.println("Computational server starts listening\non port: "
								+ port
								+ "\nwith timeout: "
								+ timeout
								+ " seconds.");

				ComputationalServerCore core = new ComputationalServerCore();
				try
				{
					core.startListening(port);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @return boolean is server working in backup mode
	 */
	public boolean isBackup()
	{
		return isBackup;
	}

	/**
	 * @return port number on which server is listening for connections
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * @return timeout after which server drops connection
	 */
	public int getTimeout()
	{
		return timeout;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
}
