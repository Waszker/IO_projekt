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
import javax.xml.bind.JAXBException;

import XMLMessages.Register;

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
	public enum ComponentType
	{
		ComputationalServer("CommunicationServer"), ComputationalNode(
				"ComputationalServer"), ComputationalClient(
				"ComputationalServer"), TaskManager("TaskManager");

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
	 * Connects to server.
	 * </p>
	 * 
	 * @return has connection succeded
	 */
	public void connectToServer()
	{
		connectionSocket = getConnectionSocket();
		if (null != connectionSocket)
		{
			try
			{
				sendMessage(getComponentRegisterMessage());
				Parser.parse(receiveMessage()); // TODO: React to timeout (for
												// example server heavy load)
				connectionSocket.close();
				startResendingThread(); // TODO: Change that!
			}
			catch (IOException e)
			{
				showError(e.getMessage());
			}
		}
	}

	/**
	 * <p>
	 * Sends the message to server.
	 * </p>
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected void sendMessage(IMessage message) throws IOException
	{
		if (null != message)
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connectionSocket.getOutputStream()));
			try
			{
				out.write(message.getString());
			}
			catch (JAXBException e)
			{
			}
			out.write(IMessage.ETB);
			out.flush();
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
	protected String receiveMessage() throws IOException
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

	protected abstract Register getComponentRegisterMessage();

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

	// TODO: Change (probably remove this in the future!)
	private void startResendingThread()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(30 * 1000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try
					{
						connectionSocket = getConnectionSocket();
						sendMessage(getComponentRegisterMessage());
						receiveMessage();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).run();
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
