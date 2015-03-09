package ComputationalClient;

import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ComputationalServer.ComputationalServer;
import GenericCommonClasses.GenericConnector;
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
	public static int DEFAULT_PORT = 47777;
	private static final long serialVersionUID = -1254898218440155506L;
	private JTextField computationStatusField;
	private JButton sendButton;
	private ComputationalClient client;

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
		super("Computational Client", new ComputationalClientActionListener());
		client = clientInstance;

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Upload file"),
				createButton("Choose file",
						COMPUTATIONAL_CLIENT_CHOOSE_FILE_BUTTON)));

		this.add(sendButton = createButton("Send",
				COMPUTATIONAL_CLIENT_SEND_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Computation status"),
				computationStatusField = createTextField("unknown", false)));

		this.pack();
		this.setLocationRelativeTo(null);
	}

	public void startWork()
	{
	}
	

}
