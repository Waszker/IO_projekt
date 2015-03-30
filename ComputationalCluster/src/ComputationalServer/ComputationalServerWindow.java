package ComputationalServer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;

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
	public static final String SERVER_WORK_MODE_BUTTON = "SERVER_WORK_MODE_BUTTON";

	private static final long serialVersionUID = 1716266392047737745L;
	private ComputationalServer server;
	private JFormattedTextField portField, timeoutField;
	private JTextArea connectedModules, activeProblems;
	private JButton startServerButton, workModeButton;

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
		connectedModules.setLineWrap(true);
		activeProblems.setLineWrap(true);

		this.setResizable(true);
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
		server.startWork(this);
		portField.setEditable(false);
		serverPort.setEditable(false);
		timeoutField.setEditable(false);
		startServerButton.setEnabled(false);
		workModeButton.setEnabled(false);
	}

	/**
	 * <p>
	 * Called in case of any trouble starting server.
	 * </p>
	 */
	public void stoppedWork()
	{
		portField.setEditable(true);
		serverPort.setEditable(true);
		timeoutField.setEditable(true);
		startServerButton.setEnabled(true);
		workModeButton.setEnabled(true);
	}

	/**
	 * <p>
	 * Displays information about connected components and sent problems.
	 * </p>
	 */
	public void refreshConnectedComponents()
	{
		StringBuilder informationBuilder = new StringBuilder();

		for (String taskManager : server.getCore().getTaskManagers())
			informationBuilder.append(taskManager + "\n");
		for (String computationalNode : server.getCore()
				.getComputationalNodes())
			informationBuilder.append(computationalNode + "\n");
		connectedModules.setText(informationBuilder.toString());

		informationBuilder = new StringBuilder();
		for (String problem : server.getCore().getProblemsToSolve())
			informationBuilder.append(problem + "\n");
		activeProblems.setText(informationBuilder.toString());

		this.pack();
	}

	/**
	 * <p>
	 * Changes mode that server will run in.
	 * </p>
	 * 
	 * @param isBackup
	 */
	public void changeServerWorkMode(boolean isBackup)
	{
		server.setisBackup(isBackup);
		workModeButton.setText((isBackup ? ServerWorkMode.BACKUP.modeString
				: ServerWorkMode.PRIMARY.modeString));
		this.serverIpField.getParent().setVisible(isBackup);
		this.serverPort.getParent().setVisible(isBackup);
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
				workModeButton = createButton(
						(server.isBackup() ? ServerWorkMode.BACKUP.modeString
								: ServerWorkMode.PRIMARY.modeString),
						SERVER_WORK_MODE_BUTTON)));

		this.add(startServerButton = createButton("Start Server",
				START_SERVER_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connected modules"), new JLabel("Active problems")));
	}

	private void setServerParametersFromFields()
	{
		Integer port, timeout;
		if ((port = getIntegerValueFromField(portField)) == null)
		{
			portField.setText(Integer.toString(server.getPort()));
		} else
		{
			if (server.isBackup())
				server.setMyLocalBackupPort(port);
			else
				server.setPort(port);
		}

		if ((timeout = getIntegerValueFromField(timeoutField)) == null)
		{
			portField.setText(Integer.toString(server.getTimeout()));
		} else
		{
			server.setTimeout(timeout);
		}

		if (server.isBackup())
		{
			server.setIpAddress(serverIpField.getText());
			server.setPort(getIntegerValueFromField(serverPort));
		}
	}
}
