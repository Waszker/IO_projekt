package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import ComputationalServer.ComputationalServerWindow;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import XMLMessages.Register;
import XMLMessages.Status;

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
	String primaryServerAddress;
	BigInteger myId;
	boolean isInBackupMode;
	int port, timeout, primaryServerPort;
	ServerSocket serverSocket;
	Semaphore queueSemaphore; // used to indicate if there are any
								// messages in queue
	BlockingQueue<ClientMessage> messageQueue;

	ConcurrentMap<BigInteger, TaskManagerInfo> taskManagers;
	ConcurrentMap<BigInteger, ComputationalNodeInfo> computationalNodes;
	ConcurrentMap<BigInteger, ProblemInfo> problemsToSolve;
	BackupServerInformation backupServer;
	List<IMessage> listOfMessagesForBackupServer;

	ComponentMonitorThread componentMonitorThread;

	BigInteger freeComponentId, freeProblemId;
	private ConnectionEstabilisherThread connectionEstabilisherThread;
	private MessageParserThread messageParserThread;
	private ComputationalServerWindow mainWindow;

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
		this.isInBackupMode = false;
		queueSemaphore = new Semaphore(0, true);
		messageQueue = new ArrayBlockingQueue<>(MAX_MESSAGES, true);
		listOfMessagesForBackupServer = Collections
				.synchronizedList(new ArrayList<IMessage>());

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
		startPrimaryServerFunctions();
	}

	/**
	 * <p>
	 * Starts server core in backup mode.
	 * </p>
	 * 
	 * @param primaryServerAddress
	 * @param primaryServerPort
	 * @param timeout
	 * @throws IOException
	 */
	public void startAsBackupServer(String primaryServerAddress,
			int primaryServerPort, int timeout, BigInteger id, int myLocalPort)
			throws IOException
	{
		this.isInBackupMode = true;
		this.primaryServerAddress = primaryServerAddress;
		this.primaryServerPort = primaryServerPort;
		this.port = myLocalPort;
		this.timeout = timeout;
		this.myId = id;

		Logger.log("Started as backup with my id " + myId + "\n");

		startBackupServerFunctions();
		startPrimaryServerFunctions();
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
		BigInteger idForComponent = new BigInteger("-1");

		if (message.getType().contentEquals(
				GenericComponent.ComponentType.TaskManager.name))
		{
			Logger.log("TM connected\n");
			idForComponent = getCurrentFreeComponentId();
			taskManagers.put(idForComponent, new TaskManagerInfo(
					idForComponent, message));
		}
		else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalNode.name))
		{
			Logger.log("CN connected\n");
			idForComponent = getCurrentFreeComponentId();
			computationalNodes.put(idForComponent, new ComputationalNodeInfo(
					idForComponent, message));
		}
		else if (message.getType().contentEquals(
				GenericComponent.ComponentType.ComputationalServer.name))
		{
			Logger.log("CS Connected\n");
			if (null == backupServer)
			{
				idForComponent = new BigInteger("9999");
				backupServer = (new BackupServerInformation(idForComponent,
						port, address));
			}
			else
			{
				Logger.log("Only one Backup Server permitted! Rejecting...\n");
			}
		}
		else
		{
			Logger.log("Unsupported component Connected\n");
		}

		return idForComponent;
	}

	/**
	 * <p>
	 * Called when any changes regarding components occur.
	 * </p>
	 */
	void informAboutComponentChanges()
	{
		if (null != mainWindow)
			mainWindow.refreshConnectedComponents();
	}

	synchronized BigInteger getCurrentFreeProblemId()
	{
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger(freeProblemId.toString());
		freeProblemId = freeProblemId.add(one);
		return result;
	}

	synchronized BigInteger getCurrentFreeComponentId()
	{
		BigInteger one = new BigInteger("1");
		BigInteger result = new BigInteger(freeComponentId.toString());
		freeComponentId = freeComponentId.add(one);
		return result;
	}

	/**
	 * <p>
	 * Returns list of connected task managers.
	 * </p>
	 * 
	 * @return
	 */
	public List<String> getTaskManagers()
	{
		List<String> taskManagersList = new ArrayList<>(taskManagers.size());

		for (Map.Entry<BigInteger, TaskManagerInfo> entry : taskManagers
				.entrySet())
		{
			taskManagersList.add(entry.getValue().toString());
		}

		return taskManagersList;
	}

	/**
	 * <p>
	 * Returns list of connected computational nodes.
	 * </p>
	 * 
	 * @return
	 */
	public List<String> getComputationalNodes()
	{
		List<String> computationalNodesList = new ArrayList<>(
				computationalNodes.size());

		for (Map.Entry<BigInteger, ComputationalNodeInfo> entry : computationalNodes
				.entrySet())
		{
			computationalNodesList.add(entry.getValue().toString());
		}

		return computationalNodesList;
	}

	/**
	 * <p>
	 * Returns list of requested problems to solve.
	 * </p>
	 * 
	 * @return
	 */
	public List<String> getProblemsToSolve()
	{
		List<String> problemsList = new ArrayList<>(computationalNodes.size());

		for (Map.Entry<BigInteger, ProblemInfo> entry : problemsToSolve
				.entrySet())
		{
			problemsList.add(entry.getValue().toString());
		}

		return problemsList;
	}

	private void startBackupServerFunctions()
	{
		(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				MessageParserForBackupServer messageParser = new MessageParserForBackupServer(
						ComputationalServerCore.this);
				while (isInBackupMode)
				{
					Status statusMessage = new Status();
					statusMessage.setId(myId);
					try
					{
						Thread.sleep(timeout * 1000);
						Socket connectionSocket = new Socket(
								primaryServerAddress, primaryServerPort);
						GenericProtocol.sendMessages(connectionSocket,
								statusMessage);
						List<IMessage> messages = GenericProtocol
								.receiveMessage(connectionSocket);
						listOfMessagesForBackupServer.addAll(messages);
						messageParser.parseMessages(messages);
					}
					catch (InterruptedException e)
					{
					}
					catch (IOException e)
					{
						Logger.log("Primary server is down! Falling back to primary server functionality!\n");
						isInBackupMode = false;

						for (Entry<BigInteger, TaskManagerInfo> entry : taskManagers
								.entrySet())
							componentMonitorThread
									.informaAboutConnectedComponent(entry
											.getKey());
						for (Entry<BigInteger, ComputationalNodeInfo> entry : computationalNodes
								.entrySet())
							componentMonitorThread
									.informaAboutConnectedComponent(entry
											.getKey());
					}
				}
			}
		})).start();
	}

	private void startPrimaryServerFunctions() throws IOException
	{
		serverSocket = new ServerSocket(this.port);
		addCloseSocketHook(serverSocket);
		connectionEstabilisherThread.start();
		messageParserThread.run();
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
