package ComputationalClient;

import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
	private int port;
	private InetAddress address;

	/******************/
	/* FUNCTIONS */
	/******************/

	/**
	 * <p>
	 * ComputationalClientWindow starts as invisible window that works in the
	 * background.
	 * </p>
	 * 
	 * @param address
	 *            IP address of ComputationalServer which CLient wants to
	 *            connect with
	 * @param port
	 *            port which will be used during connection with
	 *            ComputationalServer (default is '47777')
	 */

	public ComputationalClientWindow(InetAddress address, Integer port)
	{
		super("Computational Client", new ComputationalClientActionListener());
		
		this.port = (null == port ? DEFAULT_PORT : port);
		this.address = address;

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
