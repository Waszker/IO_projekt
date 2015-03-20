package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import ComputationalServer.ComputationalServerWindow;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import XMLMessages.Register;

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
	Semaphore queueSemaphore; // used to indicate if there are any
								// messages in queue
	BlockingQueue<ClientMessage> messageQueue;
	ConcurrentMap<BigInteger, TaskManagerInfo> taskManagers;
	ConcurrentMap<BigInteger, ComputationalNodeInfo> computationalNodes;
	ConcurrentMap<BigInteger, ProblemInfo> problemsToSolve;
	BackupServerInformation backupServer;

	ComputationalServerWindow mainWindow;
	ComponentMonitorThread componentMonitorThread;

	private BigInteger freeComponentId, freeProblemId;
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
		this.freeComponentId = new BigInteger("1");
		this.freeProblemId = new BigInteger("1");
		queueSemaphore = new Semaphore(0, true);
		messageQueue = new ArrayBlockingQueue<>(MAX_MESSAGES, true);

		taskManagers = new ConcurrentHashMap<>();
		computationalNodes = new ConcurrentHashMap<>();
		problemsToSolve = new ConcurrentHashMap<>();

		connectionEstabilisherThread = new ConnectionEstabilisherThread(this);
		messageParserThread = new MessageParserThread(this);
		componentMonitorThread = new ComponentMonitorThread(this);
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

	/**
	 * <p>
	 * Logs information about connecting component and returns its new
	 * identificator. In case of failure (unsupported component) -1 value is
	 * returned.
	 * </p>
	 * 
	 * @param message
	 * @param port
	 * @param address
	 * @return
	 */
	BigInteger registerComponent(Register message, Integer port, String address)
	{
		BigInteger idForComponent = getCurrentFreeComponentId();

		if (message.getType().contentEquals(
				GenericComponent.ComponentType.TaskManager.name))
		{
			Logger.log("TM connected\n");
			taskManagers.put(idForComponent, new TaskManagerInfo(
					idForComponent, message));
		}
		else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalNode.name))
		{
			Logger.log("CN connected\n");
			computationalNodes.put(idForComponent, new ComputationalNodeInfo(
					idForComponent, message));
		}
		else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalServer.name))
		{
			Logger.log("CS Connected\n");
			if (null != backupServer)
			{
				backupServer = (new BackupServerInformation(idForComponent,
						port, address));
			}
			else
			{
				Logger.log("Only one Backup Server permitted! Rejecting...\n");
				idForComponent = new BigInteger("-1");
			}
		}
		else
		{
			Logger.log("Unsupported component Connected\n");
			idForComponent = new BigInteger("-1");
		}

		return idForComponent;
	}

	synchronized BigInteger getCurrentFreeProblemId()
	{
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger(freeProblemId.toString());
		freeProblemId = freeProblemId.add(one);
		return result;
	}

	private synchronized BigInteger getCurrentFreeComponentId()
	{
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger(freeComponentId.toString());
		freeComponentId = freeComponentId.add(one);
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
				}
				catch (IOException | NullPointerException e)
				{ /* failed */
				}

				Logger.log("The server is shut down!\n");
			}
		});
	}
}
