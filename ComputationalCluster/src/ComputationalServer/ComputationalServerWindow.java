package ComputationalServer;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowActionListener;
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

	public static final String START_SERVER_BUTTON = "START_SERVER_BUTTON";

	private static final long serialVersionUID = 1716266392047737745L;
	private ComputationalServer server;
	private JTextField workModeField;
	private JFormattedTextField portField, timeoutField;
	private JTextArea connectedModules, activeProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * ComputationalServerWindow is gui window taht makes it easier for user to
	 * change default server runtime parameters.
	 * </p>
	 * 
	 * @param serverInstance
	 *            required to provide normal operation flow
	 */
	public ComputationalServerWindow(ComputationalServer serverInstance)
	{
		super("Computational Server", serverInstance);
		server = serverInstance;

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
	 * Starts listening for messages on port and with timeout specified by user
	 * in window. This function should not be called outside button click
	 * behavior if '-gui' flag was specified!
	 * </p>
	 */
	public void startWork()
	{
		setServerParametersFromFields();
		server.startWork();
		portField.setEditable(false);
		timeoutField.setEditable(false);
	}
	

	@Override
	public GenericWindowActionListener createActionListener()
	{
		return new ComputationalServerActionListener(this);
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
						Integer.toString(server.getPort()), true)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Timeout value"),
				timeoutField = createIntegerFormattedTextField(
						Integer.toString(server.getTimeout()), true)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Work mode"),
				workModeField = createTextField(
						(server.isBackup() ? ServerWorkMode.BACKUP.modeString
								: ServerWorkMode.PRIMARY.modeString), false)));

		this.add(createButton("Start Server", START_SERVER_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connected modules"), new JLabel("Active problems")));
	}

	private void setServerParametersFromFields()
	{
		Integer port, timeout;
		if ((port = getIntegerValueFromField(portField)) == null)
		{
			portField.setText(Integer.toString(server.getPort()));
		}
		else
		{
			server.setPort(port);
		}

		if ((timeout = getIntegerValueFromField(timeoutField)) == null)
		{
			portField.setText(Integer.toString(server.getTimeout()));
		}
		else
		{
			server.setTimeout(timeout);
		}

	}
}
