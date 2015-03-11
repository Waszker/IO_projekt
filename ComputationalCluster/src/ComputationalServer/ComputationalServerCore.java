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
import java.util.concurrent.Semaphore;

import GenericCommonClasses.IMessage;

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
	private Semaphore queueSemaphore; // used to indicate if there are any
										// messages in queue
	private BlockingQueue<ClientMessage> messageQueue;

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
		queueSemaphore = new Semaphore(0, true);
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

		final ServerSocket socket = new ServerSocket(this.port);
		startMessageRetrievalModule(socket);
		addCloseSocketHook(socket);
		startMessageRetrievalModule(socket);
		startMessageProcessingModule();
	}

	private void startMessageRetrievalModule(final ServerSocket ssocket)
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
						retrieveClientMessage(clientSocket);

						// reactToClientConnected();
						// clientSocket.close();
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

	private void startMessageProcessingModule()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true)
				{
					// semaphore will not be acquired if queue is empty
					try
					{
						queueSemaphore.acquire();
						ClientMessage message = messageQueue.remove();
						processMessage(message);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
		}).start();
	}

	private void retrieveClientMessage(final Socket clientSocket)
	{
		// Start procedure in new thread to not block other connections
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					messageQueue.add(new ClientMessage(
							receiveMessage(clientSocket), clientSocket));
					queueSemaphore.release();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// do not close in stream here
				// it should be done in message parsing routine!

			}
		}).start();
	}

	private void processMessage(ClientMessage message)
	{
		// Look at:
		// http://www.codeproject.com/Articles/11602/Java-and-Net-interop-using-Sockets
		// TODO: Add process routine (XML Schema, etc.)
		try
		{
			Socket socket = message.getClientSocket();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			System.out.println("Client [" + socket.getInetAddress()
					+ "] connected and sent message:\n"
					+ message.getMessageContent());

			out.write("Hello from the server!\n" + IMessage.ETB);
			out.flush();

			socket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

					for (ClientMessage m : messageQueue)
						m.getClientSocket().close();

					clientSocket.close();
				}
				catch (IOException | NullPointerException e)
				{ /* failed */
				}

				System.out.println("The server is shut down!");
			}
		});
	}

	private String receiveMessage(Socket connectionSocket) throws IOException
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
}
