package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import ComputationalServer.ComputationalServerWindow;

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
	final static int MAX_MESSAGES = 150;
	int port, timeout;
	ServerSocket serverSocket;
	Socket clientSocket;
	Semaphore queueSemaphore; // used to indicate if there are any
								// messages in queue
	BlockingQueue<ClientMessage> messageQueue;
	ComputationalServerWindow mainWindow;

	private BigInteger freeId;
	private ConnectionEstabilisherThread connectionEstabilisherThread;
	private MessageParserThread messageParserThread;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates ComputationalServerCore object with empty messageQueue.
	 * </p>
	 * 
	 * @param mainWindow
	 *            reference to gui window (null if not applicable)
	 */
	public ComputationalServerCore(ComputationalServerWindow mainWindow)
	{
		this.mainWindow = mainWindow;
		this.freeId = new BigInteger("1");
		queueSemaphore = new Semaphore(0, true);
		messageQueue = new ArrayBlockingQueue<>(MAX_MESSAGES, true);
		connectionEstabilisherThread = new ConnectionEstabilisherThread(this);
		messageParserThread = new MessageParserThread(this);
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
	public void startListening(int port, int timeout) throws IOException
	{
		this.port = port;
		this.timeout = timeout;

		serverSocket = new ServerSocket(this.port);
		addCloseSocketHook(serverSocket);
		connectionEstabilisherThread.start();
		messageParserThread.start();
	}
	
	synchronized BigInteger getCurrentFreeId()
	{
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger(freeId.toString());
		freeId.add(one);
		return result;
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
}
