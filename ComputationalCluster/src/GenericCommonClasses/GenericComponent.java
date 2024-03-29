package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DebugTools.Logger;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.Status;

/**
 * <p>
 * GenericComponent class is a base class for every ComputationalServer
 * ComputationalClient, TaskManager and ComputationalNode class. It holds fields
 * common for all four classes and gathers common methods usable in them.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	public enum ComponentType
	{
		ComputationalServer("CommunicationServer"), ComputationalNode(
				"ComputationalNode"), TaskManager("TaskManager"), ComputationalClient(
				"ComputationalClient");

		public String name;

		private ComponentType(String name)
		{
			this.name = name;
		}
	}

	public static final String DEFAUL_IP_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 47777;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 1000;

	protected String ipAddress;
	protected int port;
	protected boolean isGui;
	protected Socket connectionSocket;
	protected BigInteger id;
	protected Long timeout;
	protected String backupServerIp;
	protected int backupServerPort;
	protected SocketAddress socketAddress;

	private ComponentType type;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates component with three fields initialized. If there are null
	 * parameters for serverIpAddress or serverPort, those fields are set to
	 * default values instead.
	 * </p>
	 * 
	 * @param serverIpAddress
	 * @param serverPort
	 * @param isGui
	 */
	public GenericComponent(String serverIpAddress, Integer serverPort,
			boolean isGui, ComponentType type)
	{
		this.ipAddress = (null == serverIpAddress ? DEFAUL_IP_ADDRESS
				: serverIpAddress);
		this.port = (null == serverPort ? DEFAULT_PORT : serverPort);
		this.isGui = isGui;
		this.type = type;

		addShutdownHook();
	}

	/**
	 * <p>
	 * Connects to server. If connection succeeds component sets its id and
	 * timeout to the values sent by server in RegisterResponse message.
	 * </p>
	 */
	public void connectToServer()
	{
		try
		{
			sendMessages(getComponentRegisterMessage());
			List<IMessage> receivedMessages = receiveMessage();

			if (null != receivedMessages && receivedMessages.size() > 0)
			{
				if (MessageType.REGISTER_RESPONSE != receivedMessages.get(0)
						.getMessageType())
				{
					throw new IOException("Unsupported response from server!");
				}

				getRegisterResponseDetails((RegisterResponse) receivedMessages
						.get(0));
				startResendingThread();
			}
		}
		catch (IOException e)
		{
			showError(e.getMessage());
		}
	}

	/**
	 * <p>
	 * Sends the messages to server. This method creates connection before
	 * sending messages.
	 * </p>
	 * 
	 * @param message
	 * @return port on which socket was opened
	 * @throws IOException
	 */
	protected void sendMessages(IMessage... messages) throws IOException
	{
		if (null != messages)
		{
			connectionSocket = getConnectionSocket();
			if (null != connectionSocket)
			{
				connectionSocket.setReuseAddress(true);
				GenericProtocol.sendMessages(connectionSocket, messages);
			}
			else
			{
				throw new IOException("Connection unsuccessful");
			}
		}
	}

	/**
	 * <p>
	 * Receives message from connection socket.
	 * </p>
	 * 
	 * @return received message
	 * @throws IOException
	 */
	protected List<IMessage> receiveMessage() throws IOException
	{
		return GenericProtocol.receiveMessage(connectionSocket);
	}

	/**
	 * <p>
	 * Sets id and timeout sent from ComputationalServer.
	 * </p>
	 * 
	 * @param register
	 *            response message from server
	 */
	protected void getRegisterResponseDetails(RegisterResponse message)
	{
		id = message.getId();
		timeout = message.getTimeout();
		if (null != message.getBackupCommunicationServers()
				&& null != message.getBackupCommunicationServers()
						.getBackupCommunicationServer())
		{
			backupServerIp = new String(message.getBackupCommunicationServers()
					.getBackupCommunicationServer().getAddress());
			backupServerPort = message.getBackupCommunicationServers()
					.getBackupCommunicationServer().getPort();
		}
	}

	protected abstract Register getComponentRegisterMessage();

	protected abstract void reactToMessage(IMessage message);

	/**
	 * <p>
	 * Sets the ip address that will be used when connecting with server.
	 * </p>
	 * 
	 * @param ipAddress
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * <p>
	 * Get ip address that will be used to establish the connection.
	 * </p>
	 * 
	 * @return ip address
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * <p>
	 * Sets port that will be used when connecting with server.
	 * </p>
	 * 
	 * @param port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * <p>
	 * Returns port on which connection will be estabilished.
	 * </p>
	 * 
	 * @return port number
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * <p>
	 * Returns component type enum.
	 * 
	 * @See GenericComponent
	 *      </p>
	 * @return component type
	 */
	public ComponentType getType()
	{
		return type;
	}

	/**
	 * <p>
	 * Shows usage info.
	 * </p>
	 */
	public static void showUsage()
	{
		Logger.log("Usage: java -jar ComponentName.jar "
				+ "[-address [address of server]" + "[-port [port number]] \n");
		Logger.log("Optional configuration file should be named "
				+ GenericFlagInterpreter.CONFIGURATION_FILE
				+ " and should be placed in root directory of .jar file.\n");
	}

	/**
	 * <p>
	 * Returns ip at which component is registered and from which connection
	 * will be estabilished.
	 * </p>
	 * 
	 * @return
	 */
	public static String getMyIp()
	{
		// taken from:
		// http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
		String myIp = "unknown";
		URL whatismyip;
		try
		{
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			myIp = in.readLine();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Cannot obtain IP!",
					"ERROR", JOptionPane.ERROR_MESSAGE);
		}

		return myIp;
	}

	private void startResendingThread()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				CONNECTION_POSSIBLE: while (true)
				{
					boolean isSendingSuccess = false;

					try
					{
						Thread.sleep(timeout * 500);
					}
					catch (InterruptedException e)
					{
					}

					do
					{
						try
						{
							Status status = getStatusMessage();

							sendMessages(status);
							isSendingSuccess = true;
							for (IMessage message : receiveMessage())
								new Thread(() -> {
									reactToMessage(message);
								}).start();
						}
						catch (IOException e)
						{
							DebugTools.Logger.log("Switching to backup...\n");
							isSendingSuccess = false;
							ipAddress = backupServerIp;
							port = backupServerPort;
							backupServerIp = null;
							if (null == ipAddress) break CONNECTION_POSSIBLE;
						}
					} while (!isSendingSuccess);
				}
			}
		}).run();
		Logger.log("Switching to backup impossible!\n");
	}

	// returns status message for every component
	protected abstract Status getStatusMessage();

	private Socket getConnectionSocket()
	{
		Socket socket = new Socket();

		try
		{
			if (null != socketAddress)
			{
				socket.setReuseAddress(true);
				socket.bind(socketAddress);
			}
			socket.connect(new InetSocketAddress(ipAddress, port),
					DEFAULT_CONNECTION_TIMEOUT);
		}
		catch (IOException e)
		{
			socket = null;
			showError(e.getMessage());
		}

		return socket;
	}

	/**
	 * Shows error message in gui and cmd line mode.
	 * 
	 * @param message
	 *            - to be shown
	 */
	protected void showError(String message)
	{
		if (isGui)
		{
			JOptionPane.showMessageDialog(null, message, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		Logger.log(message + "\n");
	}

	/**
	 * Shows information message in gui and cmd line mode.
	 * 
	 * @param message
	 *            - to be shown
	 */
	protected void showMessage(String message)
	{
		if (isGui)
		{
			JOptionPane.showMessageDialog(null, message, "",
					JOptionPane.INFORMATION_MESSAGE);
		}
		Logger.log(message);
	}

	private void addShutdownHook()
	{
		// taken from:
		// http://stackoverflow.com/questions/8051863/how-can-i-close-the-socket-in-a-proper-way
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					connectionSocket.close();
				}
				catch (IOException | NullPointerException e)
				{ /* failed */
				}
			}
		});
	}
}