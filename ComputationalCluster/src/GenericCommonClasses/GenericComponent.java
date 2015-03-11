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
	Integer serverPort = ComputationalServer.DEFAULT_PORT;
	Integer timeout = ComputationalServer.DEFAULT_TIMEOUT;
	String serverIp = null;
	boolean isGuiEnabled;

	/******************/
	/* FUNCTIONS */
	/******************/
	
	public GenericComponent(boolean guiEnabled)
	{
		isGuiEnabled = guiEnabled;
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
		StringBuilder stringBuilder = new StringBuilder();
		final int EOF = 23;
		
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
		String line;
		while ( (line=in.readLine()) != null )
		{
			int indexOfEOF = line.indexOf(EOF);
			if ( indexOfEOF == -1 )
				stringBuilder.append(line);
			else
			{
				stringBuilder.append(line.substring(0, indexOfEOF));
				break;
			}
			
		}
		
		//clean up
		in.close();
		out.close();
		socket.close();
		
		return Parser.parse(stringBuilder.toString());
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
