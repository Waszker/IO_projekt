package GenericCommonClasses;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		this.setLocationRelativeTo(null); // sets window centered
		this.setSize(640, 480);
		this.setTitle(title);
		
		this.add(createJMenuBar());
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
		
		for(String s : itemNames) {
			JMenuItem item = new JMenuItem(s);
			// TODO: add item action listeners!!!
			menu.add(item);
		}
		
		return menu;
	}
}
