package GenericCommonClasses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
			case "Connect":
				reactToConnectButtonPress();
				break;
		}

	}
	
	private void reactToConnectButtonPress()
	{
		// TODO: Change that method!
		JOptionPane.showMessageDialog(new JFrame(), "CONNECTED", "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
