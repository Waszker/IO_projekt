package GenericCommonClasses;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

import ComputationalServer.ComputationalServer;
import ComputationalServer.ComputationalServerWindow;

/**
 * Last modification: 03/03/2015
 * 
 * <p>
 * Generic class that every component should inherit from. It provides ready to
 * use GUI interface that should be common for every component.
 * </p>
 * <p>
 * Each window should also have its own action listener that will react to
 * actions on buttons and menu items. This action listener should inherit from
 * GenericWindowActionListener class.
 * </p>
 * 
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericWindowGui extends JFrame
{
	/******************/
	/* VARIABLES */
	/******************/
	public static final String GENERIC_WINDOW_CONNECT_BUTTON = "GENERIC_WINDOW_BUTTON_CONNECT";
	protected JTextField myIpField, serverIpField, connectionStatusField;
	protected JFormattedTextField serverPort;
	protected JButton connectButton;
	protected GenericComponent component;
	private static final long serialVersionUID = -8867459368772331697L;
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private GenericWindowActionListener actionListener;
	private IpChecker ipChecker;

	/**
	 * IpChecker class reacts to ip address input and checks if it is valid
	 * address.
	 * 
	 * @author Piotr Waszkiewicz
	 * 
	 */
	private class IpChecker implements DocumentListener
	{

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			if (e.getDocument() instanceof PlainDocument)
			{
				reactToIpEntering((PlainDocument) e.getDocument());
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			if (e.getDocument() instanceof PlainDocument)
			{
				reactToIpEntering((PlainDocument) e.getDocument());
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			if (e.getDocument() instanceof PlainDocument)
			{
				reactToIpEntering((PlainDocument) e.getDocument());
			}
		}

		private void reactToIpEntering(PlainDocument field)
		{
			String text;
			try
			{
				text = field.getText(0, field.getLength());
				connectButton.setEnabled(isIpValid(text));
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}

		private boolean isIpValid(String ip)
		{
			Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
			Matcher matcher = pattern.matcher(ip);
			return matcher.matches();
		}
	}

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates titled window with default close operation set to application
	 * exit. It starts centered on screen with default size set to 640x480.
	 * </p>
	 * 
	 * @param title
	 *            displayed on window
	 */
	public GenericWindowGui(String title,
			GenericWindowActionListener actionListener)
	{
		super();
		this.actionListener = actionListener;
		this.ipChecker = new IpChecker();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(640, 480);
		this.setLocationRelativeTo(null); // sets window centered
		this.setTitle(title);
		this.setResizable(false);

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		addGuiElements();

		serverIpField.getDocument().addDocumentListener(ipChecker);
		getMyIp();

		connectButton.setEnabled(false);
	}

	/**
	 * <p>
	 * Connects to server using port and IP address specified by user.
	 * </p>
	 */
	public void connectToServer()
	{
		// no need to check ip and port sanity - window blocks connect button if
		// those values are not valid
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				if (component.getConnector() != null)
				{
					component.getConnector().connectToServer(
							serverIpField.getText(),
							Integer.parseInt(serverPort.getText()));
				}
				else
				{
					JOptionPane
							.showMessageDialog(
									new JFrame(),
									"No connector specified\nAttach connector variable in your class!",
									"Error", JOptionPane.ERROR_MESSAGE);
				}
				connectButton.setEnabled(true);
			}
		}).start();
	}

	/**
	 * <p>
	 * Creates menu with certain number of menu items. Every menu item has
	 * registered default window action listener.
	 * </p>
	 * 
	 * @param menuTitle
	 * @param itemNames
	 * @return
	 */
	final protected JMenu createMenuWithItems(String menuTitle,
			String... itemNames)
	{
		JMenu menu = new JMenu(menuTitle);

		for (String s : itemNames)
		{
			JMenuItem item = new JMenuItem(s);
			item.setActionCommand(menuTitle + s);
			item.addActionListener(actionListener);
			menu.add(item);
		}

		return menu;
	}

	/**
	 * <p>
	 * Creates button with certain text and action string property. It also gets
	 * registered at local action listener specific to its window.
	 * </p>
	 * 
	 * @param buttonString
	 * @return
	 */
	final protected JButton createButton(String buttonString,
			String actionCommandString)
	{
		JButton button = new JButton(buttonString);
		button.setActionCommand(actionCommandString);
		button.addActionListener(actionListener);

		return button;
	}

	/**
	 * <p>
	 * Function creates text field with centered text with prefered dimension
	 * set to 100x30.
	 * </p>
	 * 
	 * @param textFieldString
	 *            text shown on not edited text field
	 * @param isTextFieldEditable
	 *            indicates if user can edit text field
	 * @return
	 */
	final protected JTextField createTextField(String textFieldString,
			boolean isTextFieldEditable)
	{
		JTextField textField;

		if (textFieldString == null)
		{
			textField = new JTextField();
		}
		else
		{
			textField = new JTextField(textFieldString);
		}
		textField.setEditable(isTextFieldEditable);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setAlignmentY(CENTER_ALIGNMENT);
		textField.setPreferredSize(new Dimension(100, 30));

		return textField;
	}

	/**
	 * <p>
	 * Function creates text field with centered text with prefered dimension
	 * set to 100x30. This field can hold only integer values
	 * </p>
	 * 
	 * @param textFieldString
	 *            text shown on not edited text field
	 * @param isTextFieldEditable
	 *            indicates if user can edit text field
	 * @return
	 */
	final protected JFormattedTextField createIntegerFormattedTextField(
			String textFieldString, boolean isTextFieldEditable)
	{
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);

		JFormattedTextField textField = new JFormattedTextField(formatter);
		textField.setText(textFieldString);
		textField.setEditable(isTextFieldEditable);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setAlignmentY(CENTER_ALIGNMENT);
		textField.setPreferredSize(new Dimension(100, 30));

		return textField;
	}

	/**
	 * <p>
	 * Function creates panel with two components next to each other, oriented
	 * horizontally.
	 * </p>
	 * 
	 * @param labelString
	 *            string describing text field
	 * @param textFieldString
	 *            field initial text
	 * @param isTextFieldEditable
	 *            is text field editable by user or can be changed
	 *            programatically only
	 * @return
	 */
	final protected JPanel createTwoHorizontalComponentsPanel(
			Component component1, Component component2)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		panel.add(component1);
		panel.add(component2);

		return panel;
	}

	private JMenuBar createJMenuBar()
	{
		JMenuBar bar = new JMenuBar();

		// Add items if needed!
		bar.add(createMenuWithItems("File", "Exit"));

		return bar;
	}

	private void getMyIp()
	{
		// taken from:
		// http://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
		URL whatismyip;
		try
		{
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			String ip = in.readLine();
			myIpField.setText(ip);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Cannot obtain IP!",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private void addGuiElements()
	{
		this.setJMenuBar(createJMenuBar());

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("My IP address"),
				myIpField = createTextField("unknown", false)));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Server IP address"),
				serverIpField = createTextField("unknown IP", true)));

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("Server port"),
				serverPort = createIntegerFormattedTextField(
						Integer.toString(ComputationalServer.DEFAULT_PORT),
						true)));

		this.add(connectButton = createButton("Connect",
				GENERIC_WINDOW_CONNECT_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connection status"),
				connectionStatusField = createTextField("unknown", false)));
	}
}
