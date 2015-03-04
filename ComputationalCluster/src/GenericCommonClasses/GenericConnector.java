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
 * GenericConnector class provides routines to easily connect to the server. It
 * should be inherited by every component wanting to gain access to this
 * routine.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericConnector
{
	/******************/
	/* VARIABLES */
	/******************/
	protected String serverIpAddress;
	protected int serverPort;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Function estabilishes connection to the server and retrieves message from
	 * it. Then the connection is closed and message is displayed. WARNING! This
	 * function will change in the future!
	 * </p>
	 * 
	 * @param ip
	 *            address
	 * @param port
	 */
	public void connectToServer(String ipAddress, int port)
	{
		// TODO: This method looks awful!!!!

		serverIpAddress = ipAddress;
		serverPort = port;

		Socket socket = new Socket();
		try
		{
			socket.connect(new InetSocketAddress(ipAddress, port), 1000);

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String line;
			StringBuilder message = new StringBuilder();
			while ((line = in.readLine()) != null)
			{
				message.append(line);
			}

			JOptionPane.showMessageDialog(new JFrame(), message.toString(),
					"Success", JOptionPane.INFORMATION_MESSAGE);

		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);

		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
