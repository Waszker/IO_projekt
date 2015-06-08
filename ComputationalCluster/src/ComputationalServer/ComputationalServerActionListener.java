package ComputationalServer;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import ComputationalServer.ComputationalServerWindow.ServerWorkMode;
import GenericCommonClasses.GenericWindowActionListener;
import GenericCommonClasses.GenericWindowGui;

/**
 * <p>
 * ComputationalServerActionListener adds some server-specific GUI behaviour.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 *
 */
public class ComputationalServerActionListener extends
		GenericWindowActionListener
{
	/******************/
	/* VARIABLES */
	/******************/

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalServerActionListener(GenericWindowGui window)
	{
		super(window);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case ComputationalServerWindow.START_SERVER_BUTTON:
				((ComputationalServerWindow) mainWindow).startWork();
				break;

			case ComputationalServerWindow.SERVER_WORK_MODE_BUTTON:
				((ComputationalServerWindow) mainWindow)
						.changeServerWorkMode(((JButton) e.getSource())
								.getText().contentEquals(
										ServerWorkMode.PRIMARY.modeString));
				break;
		}

	}
}
