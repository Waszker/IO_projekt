package GenericCommonClasses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GenericWindowActionListener implements ActionListener
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
