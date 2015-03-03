package GenericCommonClasses;

import java.awt.Dimension;
import java.awt.Label;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Last modification: 03/03/2015
 * 
 * <p>
 * Generic class that every component should inherit from. It provides ready to
 * use GUI interface that should be common for every component.
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
	public GenericWindowGui(String title)
	{
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(640, 480);
		this.setLocationRelativeTo(null); // sets window centered
		this.setTitle(title);
		this.setResizable(false);

		this.setJMenuBar(createJMenuBar());
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.add(createLabelAndTextField("My IP address", "unknown", false));
	}

	private JMenuBar createJMenuBar()
	{
		JMenuBar bar = new JMenuBar();

		// Add items if needed!
		bar.add(createMenuWithItems("File", "Exit"));

		return bar;
	}

	private JMenu createMenuWithItems(String menuTitle, String... itemNames)
	{
		JMenu menu = new JMenu(menuTitle);

		for (String s : itemNames)
		{
			JMenuItem item = new JMenuItem(s);
			// TODO: add item action listeners!!!
			menu.add(item);
		}

		return menu;
	}

	private JPanel createLabelAndTextField(String labelString,
			String textFieldString, boolean isTextFieldEditable)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

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
		
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(30, 5)));
		panel.add(textField);
		
		return panel;
	}
}
