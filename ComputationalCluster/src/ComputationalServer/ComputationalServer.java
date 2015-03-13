package ComputationalServer;

import java.io.IOException;

import XMLMessages.RegisterMessage;

import GenericCommonClasses.GenericComponent;

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
public final class ComputationalServer extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final int DEFAULT_TIMEOUT = 30;

	private boolean isBackup;
	private int timeout;

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
	 * @param primaryServerIp
	 *            that will be used if server starts in backup mode (default is
	 *            127.0.0.1)
	 * @param isGui
	 *            indicating if server is started in windowed mode
	 */
	public ComputationalServer(boolean isBackup, Integer port, Integer timeout,
			String primaryServerIp, boolean isGui)
	{
		super(primaryServerIp, port, isGui, ComponentType.ComputationalServer);
		this.isBackup = isBackup;
		this.timeout = (null == timeout ? DEFAULT_TIMEOUT : timeout);
	}

	/**
	 * <p>
	 * Creates thread that starts listening for messages.
	 * </p>
	 */
	public void startWork(final ComputationalServerWindow mainWindow)
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

				ComputationalServerCore core = new ComputationalServerCore(
						mainWindow);
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
	 * @return timeout after which server drops connection
	 */
	public int getTimeout()
	{
		return timeout;
	}

	/**
	 * <p>
	 * Sets server timeout value.
	 * </p>
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	@Override
	protected RegisterMessage getComponentRegisterMessage()
	{
		// TODO: Change that!
		return new RegisterMessage(-1, getType(), false, null, 1);
	}
}
