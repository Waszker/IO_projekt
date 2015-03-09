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

import ComputationalServer.ComputationalServer;

/**
 * <p>
 * GenericConnector class provides routines to easily connect to the server. It
 * should be inherited by every component wanting to gain access to this
 * routine.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.01a :
 * 		class no longer abstract, added disconnecting, sending, receiving
 * 			(edited by Filip)
 */
public class GenericConnector
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final String EOF = "--EOF--";
	
	protected String serverIpAddress;
	protected int serverPort;
	
	Socket socket = null;
	boolean connected = false;
	BufferedWriter out;
	BufferedReader in;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Function estabilishes connection to the server.
	 * </p>
	 * 
	 * @param ip
	 *            address
	 * @param port
	 * @return True if connection was properly established. Otherwise returs false.
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void connectToServer(String ipAddress, int port)
			throws IOException, IllegalStateException
	{
		serverIpAddress = ipAddress;
		serverPort = port;
		
		if ( connected )
			throw new IllegalStateException("connectToServer(): Already connected to server! use 'disconnect()' first!");

		socket = new Socket();
		socket.connect(new InetSocketAddress(ipAddress, port), ComputationalServer.DEFAULT_TIMEOUT);

		out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		
		connected = true;
	}
	
	/**
	 * <p>
	 * Function closes connection to server if established.
	 * </p>
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void disconnect() throws IOException, IllegalStateException
	{
		if ( !connected )
			throw new IllegalStateException("disconnect(): Connection to server not established!");
		
		out.close();
		in.close();
		socket.close();
			
		in = null;
		out = null;
		connected = false;
	}
	
	/**
	 * <p>
	 * Function sends message to the server if connection is established.
	 * </p>
	 * @param m Message to be sent.
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void sendMessage(GenericMessage m) throws IOException, IllegalStateException
	{
		if ( !connected )
			throw new IllegalStateException("sendMessage(): Connection to server not established!");
		
		out.write(m.getMessageContent());
	}
	
	/**
	 * <p>
	 * Function receives message to the server if connection is established.
	 * </p>
	 * @return Received message
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public GenericMessage receiveMessage() throws IOException, IllegalStateException
	{
		if ( !connected )
			throw new IllegalStateException("receiveMessage(): Connection to server not established!");
		
		String received = "";
		while ( in.ready() )
			received+=in.readLine();
		return null;//new GenericMessage(received);
	}
	
	/**
	 * <p>
	 * Checks if there is a message ready to receive.
	 * </p>
	 * @return true if a message can be received.
	 * @throws IOException 
	 */
	public boolean messageWaiting() throws IOException
	{
		return in.ready();
	}
	
	/*
	private void showInformation(String message, boolean isGui)
	{
		if (isGui)
		{
			JOptionPane.showMessageDialog(new JFrame(), message,
					"Information", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			System.out.println(message);
		}
	}

	private void showError(String message, boolean isGui)
	{
		if (isGui)
		{
			JOptionPane.showMessageDialog(new JFrame(), message,
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			System.err.println(message);
		}
	}*/
}
