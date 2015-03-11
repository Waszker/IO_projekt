package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <p>
 * GenericComponent class is a base class for every ComputationalClient,
 * TaskManager and ComputationalNode class. It holds fields common for all three
 * classes and gathers common methods usable in them.
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
	public static final String DEFAUL_IP_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 47777;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 1000;

	protected String ipAddress;
	protected int port;
	protected boolean isGui;
	protected Socket connectionSocket;

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
			boolean isGui)
	{
		this.ipAddress = (null == serverIpAddress ? DEFAUL_IP_ADDRESS
				: serverIpAddress);
		this.port = (null == serverPort ? DEFAULT_PORT : serverPort);
		this.isGui = isGui;

		addShutdownHook();
	}

	/**
	 * <p>
	 * Sends the message to server and waits for answer.
	 * </p>
	 * 
	 * @param message
	 */
	public void sendMessage(IMessage message)
	{
		connectionSocket = getConnectionSocket();
		if (connectionSocket != null)
		{
			try
			{
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
						connectionSocket.getOutputStream()));
				out.write(message.toString());
				out.write(IMessage.ETB);
				out.flush();
				
				parseMessage(receiveMessage());

				// TODO: Do we need to close socket?
				connectionSocket.close();
			}
			catch (IOException e)
			{
				showError(e.getMessage());
			}
		}
	}
	
	/**
	 * <p>
	 * Connects to server.
	 * </p>
	 */
	public abstract void connectToServer();

	/**
	 * <p>
	 * This method specifies component behaviour after message has benn
	 * retrieved.
	 * </p>
	 * 
	 * @param messageContent
	 */
	protected abstract void parseMessage(String messageContent);

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

	private String receiveMessage() throws IOException
	{
		int readChar;
		StringBuilder messageBuilder = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));

		while ((readChar = in.read()) != -1)
		{
			if (readChar == IMessage.ETB)
				break;
			messageBuilder.append((char) readChar);
		}

		return messageBuilder.toString();
	}

	private Socket getConnectionSocket()
	{
		Socket socket = new Socket();
		try
		{
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

	private void showError(String message)
	{
		if (isGui)
		{
			JOptionPane.showMessageDialog(new JFrame(), message, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			System.err.println(message);
		}
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
