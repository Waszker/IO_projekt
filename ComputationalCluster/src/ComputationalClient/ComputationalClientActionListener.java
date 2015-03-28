package ComputationalClient;

import java.awt.event.ActionEvent;
import java.io.File;
import java.math.BigInteger;

import javax.swing.JFileChooser;

import GenericCommonClasses.GenericWindowActionListener;
import GenericCommonClasses.GenericWindowGui;

public class ComputationalClientActionListener extends
		GenericWindowActionListener
{
	/******************/
	/* VARIABLES */
	/******************/

	private ComputationalClient client;
	private ComputationalClientWindow window;

	/******************/
	/* FUNCTIONS */
	/******************/

	public ComputationalClientActionListener(GenericWindowGui window,
			ComputationalClient client)
	{
		super(window);
		this.client = client;
		this.window = (ComputationalClientWindow) window;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		switch (e.getActionCommand())
		{
			case ComputationalClientWindow.COMPUTATIONAL_CLIENT_CHOOSE_FILE_BUTTON:
				reactToChooseFiletButtonPress();
				break;

			case ComputationalClientWindow.COMPUTATIONAL_CLIENT_SEND_BUTTON:
				reactToSendButtonPress();
				break;

			case ComputationalClientWindow.COMPUTATIONAL_CLIENT_REQUEST_BUTTON:
				reactToRequestButtonPress();
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
			String filename = file.getName();
			client.dataFile = file;
			client.filePath = file.getAbsolutePath();
			this.window.fileName.setText(filename);
			this.window.sendButton.setEnabled(true);
		}
	}

	private void reactToSendButtonPress()
	{
		client.setTimeout(new BigInteger(window.timeoutField.getText()));
		client.sendSolveRequestMessage();
		window.computationStatusField.setText("Computing...");
		this.window.requestButton.setEnabled(true);
	}

	private void reactToRequestButtonPress()
	{
		client.sendSolutionRequestMessage();
		if (client.computationIsDone)
			window.computationStatusField.setText("Done");
	}
}
