package ComputationalServer;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import GenericCommonClasses.GenericWindowActionListener;

public class ComputationalServerActionListener extends
		GenericWindowActionListener
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
			// TODO: This case is... just AWFUL!
			case ComputationalServerWindow.START_SERVER_BUTTON:
				JButton startButton = (JButton) e.getSource();
				startButton.setEnabled(false);
				
				Component parent = startButton.getParent();
				while (true)
				{
					if (parent instanceof ComputationalServerWindow)
					{
						((ComputationalServerWindow) parent).startWork();
						break;
					}
					parent = parent.getParent();
				}
				break;
		}

	}
}
