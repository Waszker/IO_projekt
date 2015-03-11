package GenericCommonClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import ComputationalServer.ComputationalServer;

/**
 * <p>
 * GenericComponent class is a base class for every ComputationalClient,
 * TaskManager and ComputationalNode class. It holds fields common for all three
 * classes and gathers common methods usable in them.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.1
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
	
	Integer serverPort;
	Integer timeout;
	String serverIp;
	boolean isGuiEnabled;

	/******************/
	/* FUNCTIONS */
	/******************/
	
	public GenericComponent(String serverIpAddress, Integer serverPort,
			boolean isGui)
	{
		this.serverIp = (null == serverIpAddress ? DEFAUL_IP_ADDRESS
				: serverIpAddress);
		this.serverPort = (null == serverPort ? DEFAULT_PORT : serverPort);
		this.isGuiEnabled = isGui;

		addShutdownHook();
	}
	
	
	/**
	 * <p>
	 * Function sends message to computational server and receives a response.
	 * </p> 
	 * @param toSend - message to be sent to communication server.
	 * @return response received from the server.
	 * @throws IOException 
	 * @see IMessage
	 */
	IMessage sendMessage(IMessage toSend) throws IOException
	{
		Socket socket;
		BufferedWriter out;
		BufferedReader in;
		StringBuilder messageBuilder = new StringBuilder();
		
		//connect to the server
		socket = new Socket();
		socket.connect(new InetSocketAddress(serverIp, serverPort), timeout);

		out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		
		//send toSend message
		out.write(toSend.toString());
		out.flush();

		//receive response
		char readChar;
		while ((readChar = in.read()) != -1)
		{
			if (readChar == IMessage.ETB)
				break;
			messageBuilder.append((char) readChar);
		}
		
		//clean up
		in.close();
		out.close();
		socket.close();
		
		return Parser.parse(messageBuilder.toString());
	}
	
	/**
	 * <p>
	 * Displays a message to the user.
	 * If gui is enabled, message dialog is shown.
	 * </p>
	 * @param message String to be displayed.
	 */
	protected void showInformation(String message)
	{
		System.out.println(message);
		if (isGuiEnabled)
		{
			JOptionPane.showMessageDialog(null, message,
					"Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * <p>
	 * Displays an error message to the user.
	 * If gui is enabled, error message dialog is shown.
	 * </p>
	 * @param message String to be displayed.
	 */
	protected void showError(String message)
	{
		System.err.println(message);
		if (isGuiEnabled)
		{
			JOptionPane.showMessageDialog(null, message,
					"Error", JOptionPane.ERROR_MESSAGE);
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

	/* Getters and setters */
	public Integer getServerPort()
	{
		return serverPort;
	}

	public void setServerPort(Integer serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public Integer getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}
	/* ******************* */
}
