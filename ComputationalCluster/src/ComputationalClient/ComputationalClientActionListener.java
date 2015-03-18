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
import GenericCommonClasses.GenericComponent;
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
	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClientActionListener(GenericWindowGui window,
			ComputationalClient client)
	{
		super(window);
		this.client = client;

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
		}
	}

	private void reactToChooseFiletButtonPress()
	{
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			try
			{
				this.data = loadFile(file);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void reactToSendButtonPress()
	{
		JOptionPane.showMessageDialog(new JFrame(), "SENT", "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
		
		SolveRequest sr=new SolveRequest();
		sr.setId(client.getProblemId());
		sr.setData(data);
		sr.setProblemType("problem");
		sr.setSolvingTimeout(new BigInteger("60"));
        client.sendSolveRequestMessage(sr);
	}

	private static byte[] loadFile(File file) throws IOException
	{
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE)
		{
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
		{
			offset += numRead;
		}

		if (offset < bytes.length)
		{
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}
}
