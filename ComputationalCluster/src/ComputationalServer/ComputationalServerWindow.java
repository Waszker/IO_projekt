package ComputationalServer;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowGui;

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

	private static final long serialVersionUID = 1716266392047737745L;
	private boolean isBackup;
	private JTextField workModeField;
	private JTextArea connectedModules, activeProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalServerWindow(boolean isBackup)
	{
		super("Computational Server", new ComputationalServerActionListener());

		this.isBackup = isBackup;
		hideUnusedFields();
		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Work mode"),
				workModeField = createTextField(
						(isBackup ? ServerWorkMode.BACKUP.modeString
								: ServerWorkMode.PRIMARY.modeString), false)));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connected modules"), new JLabel("Active problems")));

		this.add(createTwoHorizontalComponentsPanel(
				connectedModules = new JTextArea(
						"Currently no connected modules..."),
				activeProblems = new JTextArea(
						"Currently no active problems to solve...")));
		// TODO: connectedModules and activeProblems should fit within window

		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void hideUnusedFields()
	{
		this.serverIpField.getParent().setVisible(false);
		this.connectionStatusField.getParent().setVisible(false);
		this.connectButton.setVisible(false);
	}
}
