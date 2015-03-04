package ComputationalServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import GenericCommonClasses.GenericMessage;

/**
 * <p>
 * ComputationalServerCore class consists of two threads named Messaging
 * Processing Module and Messaging Queuing Module. Its main purpose is to
 * provide way to communicate with other components (modules) required to
 * successfully solve problems specified by ComputationalClient.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class ComputationalServerCore
{
	/******************/
	/* VARIABLES */
	/******************/
	private final static int MAX_MESSAGES = 150;
	private int port;
	private Socket clientSocket;
	private BlockingQueue<GenericMessage> messageQueue;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates ComputationalServerCore object with empty messageQueue.
	 * </p>
	 */
	public ComputationalServerCore()
	{
		messageQueue = new ArrayBlockingQueue<>(MAX_MESSAGES, true);
	}

	/**
	 * <p>
	 * Starts listening for messages on certain port.
	 * </p>
	 * 
	 * @param port
	 *            on which messages are expected
	 * @throws IOException
	 */
	public void startListening(int port) throws IOException
	{
		this.port = port;

		final ServerSocket socket = new ServerSocket(port);
		startMessageProcessingModule(socket);
		addCloseSocketHook(socket);
		startMessageProcessingModule(socket);
	}

	private void startMessageProcessingModule(final ServerSocket ssocket)
	{
		// look at:
		// http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
		// for more server - client communication examples
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						clientSocket = ssocket.accept();
						// TODO: do we want to save current clientSocket in
						// order to send response?
						
						reactToClientConnected();
						clientSocket.close();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void reactToClientConnected() throws IOException
	{
		// look at:
		// http://www.codeproject.com/Articles/11602/Java-and-Net-interop-using-Sockets

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));

		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		System.out.println("Client [" + clientSocket.getInetAddress()
				+ "] connected\n");

		out.write("Hello from the server!\n");
		out.flush();
		//  TODO: finish this method 
//		String line;
//		while ((line = in.readLine()) != null)
//			System.out.println(line);
	}

	private void addCloseSocketHook(final ServerSocket ssocket)
	{
		// taken from:
		// http://stackoverflow.com/questions/8051863/how-can-i-close-the-socket-in-a-proper-way
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					// If ssocket is null then there's no way connectionSocket
					// can be open.
					// So this routine is good.
					// If you have any objections write:
					// waszkiewiczp@student.mini.pw.edu.pl
					ssocket.close();
					clientSocket.close();
				}
				catch (IOException | NullPointerException e)
				{ /* failed */
				}

				System.out.println("The server is shut down!");
			}
		});
	}
}
