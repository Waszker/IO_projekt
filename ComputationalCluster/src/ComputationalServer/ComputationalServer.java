package ComputationalServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import ComputationalServer.ServerCore.ComputationalServerCore;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.RegisterResponse.BackupCommunicationServers.BackupCommunicationServer;
import XMLMessages.Status;

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
	private int timeout, myLocalBackupPort;
	/**
	 * Main server functionality is provided by this class.
	 */
	private ComputationalServerCore core;

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
	 * @param backupPort
	 *            port on which backup should listen for connections
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
			Integer backupPort, String primaryServerIp, boolean isGui)
	{
		super(primaryServerIp, port, isGui, ComponentType.ComputationalServer);
		this.isBackup = isBackup;
		this.timeout = (null == timeout ? DEFAULT_TIMEOUT : timeout);
		this.myLocalBackupPort = (null == backupPort ? DEFAULT_PORT
				: backupPort);
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
				Logger.log("Computational server starts listening\non port: "
						+ port + "\nwith timeout: " + timeout + " seconds.\n");

				core = new ComputationalServerCore(mainWindow);
				try
				{
					if (isBackup)
					{
						connectToServer();
						core.startAsBackupServer(ipAddress, port, timeout, id,
								myLocalBackupPort);
					}
					else
					{
						core.startListening(port, timeout);
					}
				}
				catch (IOException | UnsupportedOperationException e)
				{
				}
				finally
				{
					if (null != mainWindow) mainWindow.stoppedWork();
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
	 * <p>
	 * Sets if server works in primary or backup mode.
	 * </p>
	 * 
	 * @param isBackup
	 */
	public void setisBackup(boolean isBackup)
	{
		this.isBackup = isBackup;
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

	public int getMyLocalBackupPort()
	{
		return myLocalBackupPort;
	}

	public void setMyLocalBackupPort(int myLocalBackupPort)
	{
		this.myLocalBackupPort = myLocalBackupPort;
	}

	/**
	 * <p>
	 * Returns server core.
	 * </p>
	 * 
	 * @return
	 */
	public ComputationalServerCore getCore()
	{
		return core;
	}

	@Override
	public void connectToServer() throws UnsupportedOperationException
	{
		boolean isRegistered = false;

		socketAddress = new InetSocketAddress("0.0.0.0", myLocalBackupPort);

		while (!isRegistered)
		{
			try
			{
				sendMessages(getComponentRegisterMessage());
				IMessage response = receiveMessage().get(0);

				if (response instanceof RegisterResponse)
				{
					if (null == ((RegisterResponse) response)
							.getBackupCommunicationServers()
							|| null == ((RegisterResponse) response)
									.getBackupCommunicationServers()
									.getBackupCommunicationServer())
					{
						// There are no other backup servers
						timeout = (int) ((RegisterResponse) response)
								.getTimeout();
						id = ((RegisterResponse) response).getId();
						isRegistered = true;
					}
					else
					{
						// We are no alone - some other backup server already
						// exists
						BackupCommunicationServer bServer = ((RegisterResponse) response)
								.getBackupCommunicationServers()
								.getBackupCommunicationServer();
						ipAddress = bServer.getAddress();
						port = bServer.getPort();
					}
				}
				else
				{
					showError("Unsupported response received!");
					throw new UnsupportedOperationException(
							"Can't connect to primary server");
				}
			}
			catch (IOException | IndexOutOfBoundsException e)
			{
				e.printStackTrace();
				Logger.log("Error connecting to server\n");
				throw new UnsupportedOperationException(
						"Can't connect to primary server");
			}
		}
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register registerMessage = new Register();
		registerMessage.setType(ComponentType.ComputationalServer.name);

		return registerMessage;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		// Server receives messages inside its ServerCore functionality
	}

	@Override
	protected Status getStatusMessage()
	{
		// Server sends Status message inside its ServerCore functionality
		return null;
	}
}
