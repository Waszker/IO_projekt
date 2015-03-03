package ComputationalClient;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GenericCommonClasses.GenericWindowActionListener;

public class ComputationalClientActionListener extends
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
		super.actionPerformed(e);
		switch (e.getActionCommand())
		{
			case "Choose file":
				reactToChooseFiletButtonPress();
				break;
			case "Send":
				reactToSendButtonPress();
				break;
		}
	}

	private void reactToChooseFiletButtonPress()
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
		}
	}

	private void reactToSendButtonPress()
	{
		JOptionPane.showMessageDialog(new JFrame(), "SENT", "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
