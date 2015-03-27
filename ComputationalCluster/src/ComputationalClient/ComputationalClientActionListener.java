package ComputationalClient;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericFlagInterpreter;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.GenericWindowActionListener;
import GenericCommonClasses.GenericWindowGui;
import GenericCommonClasses.IMessage;

public class ComputationalClientActionListener extends
		GenericWindowActionListener
{
	/******************/
	/* VARIABLES */
	/******************/

	private ComputationalClient client;
	private byte[] data;
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
			client.filePath=file.getAbsolutePath();
			this.window.fileName.setText(filename);
			this.window.sendButton.setEnabled(true);
		}
	}

	private void reactToSendButtonPress()
	{
		//Logger.log("timeout: " + window.getIntegerValueFromField(window.timeoutField) + "\n");
		//Integer timeout = window.getIntegerValueFromField(window.timeoutField);
		client.setTimeout(new BigInteger(window.timeoutField.getText()));
		client.sendSolveRequestMessage();
		//JOptionPane.showMessageDialog(new JFrame(), "SENT", "Dialog",
		//		JOptionPane.INFORMATION_MESSAGE);
		this.window.requestButton.setEnabled(true);
	}

	private void reactToRequestButtonPress()
	{
		client.sendSolutionRequestMessage();
	}

}
