package ComputationalServer;

import java.io.IOException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowGui;

/**
 * <p>
 * ComputationalServerWindow class makes it possible to provide gui to
 * ComputationalServer class. It provides graphic interface to the user and
 * makes organizing work easy.
 * </p>
 * <p>
 * If the server is started without '-gui' flag specified then this window
 * should still be instantiated but not shown.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public final class ComputationalServerWindow extends GenericWindowGui
{
	/******************/
	/* VARIABLES */
	/******************/
	public enum ServerWorkMode
	{
		PRIMARY("primary"), BACKUP("backup");

		public String modeString;

		private ServerWorkMode(String string)
		{
			modeString = string;
		}
	}

	public static int DEFAULT_PORT = 47777;
	public static int DEFAULT_TIMEOUT = 30;
	public static final String START_SERVER_BUTTON = "START_SERVER_BUTTON";

	private static final long serialVersionUID = 1716266392047737745L;
	private boolean isBackup;
	private int port, timeout;
	private JTextField workModeField;
	private JFormattedTextField portField, timeoutField;
	private JTextArea connectedModules, activeProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * ComputationalServerWindow starts as invisible window that works in the
	 * background.
	 * </p>
	 * 
	 * @param isBackup
	 *            is server working in backup or primary mode (default is
	 *            primary)
	 * @param port
	 *            on which server should listen to connection (default is
	 *            '47777')
	 * @param timeout
	 *            in seconds after which server will mark connected module as
	 *            inactive and disconnected (default is '30 seconds')
	 */
	public ComputationalServerWindow(boolean isBackup, Integer port,
			Integer timeout)
	{
		super("Computational Server", new ComputationalServerActionListener());

		this.isBackup = isBackup;
		this.port = (null == port ? DEFAULT_PORT : port);
		this.timeout = (null == timeout ? DEFAULT_TIMEOUT : timeout);

		hideUnusedFields();
		addRequiredFields();

		this.add(createTwoHorizontalComponentsPanel(
				connectedModules = new JTextArea(
						"Currently no connected modules..."),
				activeProblems = new JTextArea(
						"Currently no active problems to solve...")));
		// TODO: connectedModules and activeProblems should fit within window

		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * <p>
	 * Starts listening for messages. This function should not be called outside
	 * button click behavior if '-gui' flag was specified!
	 * </p>
	 */
	public void startWork()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				System.out
						.println("Computational server starts listening\non port: "
								+ port
								+ "\nwith timeout: "
								+ timeout
								+ " seconds.");
				
				ComputationalServerCore core = new ComputationalServerCore();
				try
				{
					portField.setEditable(false);
					timeoutField.setEditable(false);
					core.startListening(port);
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void hideUnusedFields()
	{
		this.serverIpField.getParent().setVisible(false);
		this.serverPort.getParent().setVisible(false);
		this.connectionStatusField.getParent().setVisible(false);
		this.connectButton.setVisible(false);
	}

	private void addRequiredFields()
	{
		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Listening on port"),
				portField = createIntegerFormattedTextField(
						Integer.toString(this.port), true)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Timeout value"),
				timeoutField = createIntegerFormattedTextField(
						Integer.toString(this.timeout), true)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Work mode"),
				workModeField = createTextField(
						(isBackup ? ServerWorkMode.BACKUP.modeString
								: ServerWorkMode.PRIMARY.modeString), false)));

		this.add(createButton("Start Server", START_SERVER_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connected modules"), new JLabel("Active problems")));
	}
}
