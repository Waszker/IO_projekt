package ComputationalClient;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowActionListener;
import GenericCommonClasses.GenericWindowGui;

/**
 * Last modification: 05/03/2015
 * 
 * <p>
 * Class that provides ready to use GUI interface for Computational Client
 * component.
 * </p>
 * 
 * 
 * @author Anna Zawadzka
 * 
 */

public final class ComputationalClientWindow extends GenericWindowGui
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final String COMPUTATIONAL_CLIENT_CHOOSE_FILE_BUTTON = "COMPUTATIONAL_CLIENT_CHOOSE_FILE_BUTTON";
	public static final String COMPUTATIONAL_CLIENT_SEND_BUTTON = "COMPUTATIONAL_CLIENT_SEND_BUTTON";
	public static final String COMPUTATIONAL_CLIENT_REQUEST_BUTTON = "COMPUTATIONAL_CLIENT_REQUEST_BUTTON";

	private static final long serialVersionUID = -1254898218440155506L;
	private JTextField computationStatusField;
	protected JButton sendButton;
	protected JFormattedTextField timeoutField;
	protected JLabel fileName;
	protected JButton requestButton;

	/******************/
	/* FUNCTIONS */
	/******************/

	/**
	 * <p>
	 * ComputationalClientWindow starts as invisible window that works in the
	 * background.
	 * </p>
	 * 
	 * @param clientInstance
	 *            required to provide normal operation flow
	 */
	public ComputationalClientWindow(ComputationalClient clientInstance)
	{
		super("Computational Client", clientInstance);
		//hideUnusedFields();

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Upload file"),
				createButton("Choose file",
						COMPUTATIONAL_CLIENT_CHOOSE_FILE_BUTTON)));
		
		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Specify timeout"),
				timeoutField = createIntegerFormattedTextField("", true)));

		this.add(createTwoHorizontalComponentsPanel(
				fileName = new JLabel(""),
				sendButton = createButton("Send",
						COMPUTATIONAL_CLIENT_SEND_BUTTON)));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Computation status"),
				computationStatusField = createTextField("unknown", false)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Solution request"),
				requestButton = createButton("Request",
						COMPUTATIONAL_CLIENT_REQUEST_BUTTON)));
		
		sendButton.setEnabled(false);
		requestButton.setEnabled(false);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void hideUnusedFields()
	{
		this.connectionStatusField.getParent().setVisible(false);
		//this.connectButton.setVisible(false);
	}

	@Override
	public GenericWindowActionListener createActionListener()
	{
		return new ComputationalClientActionListener(this,
				(ComputationalClient) component);
	}
	
	protected Integer getIntegerValueFromField(JFormattedTextField field)
	{
		return super.getIntegerValueFromField(field);
	}

}
