package ComputationalServer;

import java.awt.event.ActionEvent;

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
				((ComputationalServerWindow)mainWindow).startWork();
				break;
		}

	}
}
