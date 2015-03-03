package GenericCommonClasses;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
	protected JButton connectButton;
	private static final long serialVersionUID = -8867459368772331697L;
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
				System.out.println(text);
			} catch (BadLocationException e)
			{
				e.printStackTrace();
			}
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

		this.setJMenuBar(createJMenuBar());

		this.add(createTwoHorizontalComponentsPanel(
				new JLabel("My IP address"),
				myIpField = createTextField("unknown", false)));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Server IP address"),
				serverIpField = createTextField("unknown IP", true)));

		this.add(connectButton = createButton("Connect",
				GENERIC_WINDOW_CONNECT_BUTTON));

		this.add(createTwoHorizontalComponentsPanel(new JLabel(
				"Connection status"),
				connectionStatusField = createTextField("unknown", false)));
		
		serverIpField.getDocument().addDocumentListener(ipChecker);
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
		} else
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
}
