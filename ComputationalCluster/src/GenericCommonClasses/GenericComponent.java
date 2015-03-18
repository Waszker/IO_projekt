package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.Status;

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
				"ComputationalNode"), TaskManager("TaskManager");

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
	 * 
	 * @return has connection succeded
	 */
	public void connectToServer()
	{
		try
		{
			sendMessage(getComponentRegisterMessage());
			IMessage receivedMessage = receiveMessage();

			if (null == receivedMessage
					|| MessageType.REGISTER_RESPONSE != receivedMessage
							.getMessageType())
			{
				throw new IOException("Unsupported response from server!");
			}

			getRegisterResponseDetails((RegisterResponse) receivedMessage);
			startResendingThread();
			connectionSocket.close();
		}
		catch (IOException e)
		{
			showError(e.getMessage());
		}
	}

	/**
	 * <p>
	 * Sends the message to server. This method creates connection before
	 * sending message.
	 * </p>
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected void sendMessage(IMessage... messages) throws IOException
	{
		if (null != messages)
		{
			connectionSocket = getConnectionSocket();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connectionSocket.getOutputStream()));
			try
			{
				for (IMessage m : messages)
					out.write(m.getString() + IMessage.ETB);
			}
			catch (JAXBException e)
			{
			}
			out.write(IMessage.ETX);
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
	protected IMessage receiveMessage() throws IOException
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

		return Parser.parse(messageBuilder.toString());
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
						Thread.sleep(timeout * 1000); // TODO: Check if seconds
														// or not
					}
					catch (InterruptedException e)
					{
					}
					try
					{
						Status status = new Status();
						status.setId(id);

						sendMessage(status);
						reactToMessage(receiveMessage());
					}
					catch (IOException e)
					{
						// TODO: react to connection error
						// (Probably Server is not accessible anymore)
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
		System.err.println(message);
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
		System.out.println(message);
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
