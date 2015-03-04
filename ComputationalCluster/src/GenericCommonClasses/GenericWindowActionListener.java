package GenericCommonClasses;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ComputationalServer.ComputationalServerWindow;

/**
 * <p>
 * GenericWindowActionListener class is responsible for holding all action
 * methods for components (such as button clicks, menu item click, etc.).
 * </p>
 * <p>
 * Each component added into window has specific action string property. It is
 * defined by text displayed by it (so for example button with text "Connect"
 * has actionString == "Connect"). <b>Warning</b>! With MenuItem objects things
 * are little different: each menu item has action string that is concatenation
 * of menu name and menu item name (for example: "FileSave" where File is menu
 * text and Save is menu item text).
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * 
 */
public abstract class GenericWindowActionListener implements ActionListener
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case GenericWindowGui.GENERIC_WINDOW_CONNECT_BUTTON:
				reactToConnectButtonPress((JButton)e.getSource());
				break;

			case "FileExit":
				System.exit(0);
				break;
		}

	}

	private void reactToConnectButtonPress(JButton button)
	{
		// TODO: This method is... just AWFUL!
		button.setEnabled(false);

		Component parent = button.getParent();
		while (true)
		{
			if (parent instanceof GenericWindowGui)
			{
				((GenericWindowGui) parent).connectToServer();
				break;
			}
			parent = parent.getParent();
		}
	}
}
