package GenericCommonClasses;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
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
 * 
 */
public abstract class GenericWindowGui extends JFrame
{
	/******************/
	/* VARIABLES */
	/******************/
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
				reactToIpEntering((PlainDocument) e.getDocument());
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			if (e.getDocument() instanceof PlainDocument)
				reactToIpEntering((PlainDocument) e.getDocument());
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			if (e.getDocument() instanceof PlainDocument)
				reactToIpEntering((PlainDocument) e.getDocument());
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

		this.setJMenuBar(createJMenuBar());
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.add(createLabelAndTextField("My IP address", "unknown", false));
		this.add(createLabelAndTextField("Server IP address", "unknown IP",
				true));
		this.add(createButton("Connect"));
		this.add(createLabelAndTextField("Connection status", "disconnected",
				false));
	}

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

	final protected JButton createButton(String buttonString)
	{
		JButton button = new JButton(buttonString);
		button.setActionCommand(buttonString);
		button.addActionListener(actionListener);

		return button;
	}

	final protected JPanel createLabelAndButton(String labelString,
			String buttonString)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		Label label = new Label(labelString);
		JButton button = createButton(buttonString);

		panel.add(label);
		panel.add(button);

		return panel;
	}

	final protected JPanel createLabelAndTextField(String labelString,
			String textFieldString, boolean isTextFieldEditable)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		Label label = new Label(labelString);
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
		
		// TODO: change it somehow!
		textField.getDocument().addDocumentListener(ipChecker);

		panel.add(label);
		panel.add(textField);

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
