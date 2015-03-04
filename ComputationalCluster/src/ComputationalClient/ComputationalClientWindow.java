package ComputationalClient;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowGui;

/**
 * Last modification: 03/03/2015
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
	private static final long serialVersionUID = -1254898218440155506L;
	private JTextField computationStatusField;
	private JButton sendButton;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClientWindow()
	{
		super("Computational Client", new ComputationalClientActionListener());

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
}
