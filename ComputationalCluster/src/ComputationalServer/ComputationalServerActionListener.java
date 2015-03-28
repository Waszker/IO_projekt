package ComputationalServer;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import ComputationalServer.ComputationalServerWindow.ServerWorkMode;
import GenericCommonClasses.GenericWindowActionListener;
import GenericCommonClasses.GenericWindowGui;

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
		// TODO Auto-generated constructor stub
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
						.changeServerWorkdMode(((JButton) e.getSource())
								.getText().contentEquals(
										ServerWorkMode.PRIMARY.modeString));
				break;
		}

	}
}
