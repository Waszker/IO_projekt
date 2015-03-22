package ComputationalClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import org.omg.CORBA_2_3.portable.OutputStream;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.ProblemHelper;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Register;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;

/**
 * <p>
 * ComputationalClient is a class providing the Computational Client logic.
 * </p>
 * 
 * @author Anna Zawadzka
 */

public class ComputationalClient extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/

	private BigInteger problemId;
	private BigInteger taskId;
	protected File dataFile;
	private Integer timeout;
	private byte[] solutionData;
	private String filePath;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClient(String address, Integer port,
			boolean isGuiEnabled, String filePath)
	{
		super(address, port, isGuiEnabled, ComponentType.ComputationalClient);
		this.filePath = filePath;
		if (null != filePath)
			dataFile = new File(filePath);
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register r = new Register();
		r.setType(GenericComponent.ComponentType.ComputationalClient.name);
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{

	}

	protected void sendSolveRequestMessage()
	{
		try
		{
			byte[] data = loadFile(dataFile);
			SolveRequest sr = new SolveRequest();
			sr.setProblemType("TestProblem");
			sr.setSolvingTimeout(new BigInteger("1000"));
			sr.setData(data);
			this.sendMessages(sr);
			ReactToReceivedMessage();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected void sendSolutionRequestMessage()
	{
		SolutionRequest sr = new SolutionRequest();
		sr.setId(this.problemId);
		try
		{
			Thread.sleep(20000);
			this.sendMessages(sr);
			ReactToReceivedMessage();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void ReactToReceivedMessage()
	{
		try
		{
			List<IMessage> messages = receiveMessage();
			IMessage message = messages.get(0);
			if (message.getMessageType() == MessageType.SOLVE_REQUEST_RESPONSE)
			{
				this.problemId = ((SolveRequestResponse) message).getId();
				Logger.log("problem id: " + this.problemId + "\n");
				sendSolutionRequestMessage();
			}
			if (message.getMessageType() == MessageType.SOLUTION)
			{
				String problemType = ((Solutiones) message).getSolutions()
						.getSolution().get(0).getType();
				Logger.log("Received problem type: " + problemType + "\n");
				solutionData = ((Solutiones) message).getSolutions()
						.getSolution().get(0).getData();
				String stringData = new String(solutionData);
				Logger.log("\n\nMessage content : \n***\n" + stringData
						+ "\n***\n\n");
				SaveSolution();

			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void SaveSolution()
	{
		String saveFilePath = filePath;
		String ext = "";
		int pos = filePath.lastIndexOf(".");
		if (pos != -1)
		{
			saveFilePath = filePath.substring(0, pos);
			ext = filePath.substring(pos + 1, filePath.length());
			ext="."+ext;
		}

		saveFilePath = saveFilePath + "Solution" + ext;
		String stringData = new String(solutionData);
		File outputFile = new File(saveFilePath);

		try
		{
			if (!outputFile.exists())
				outputFile.createNewFile();
			FileOutputStream fop = new FileOutputStream(outputFile);
			PrintStream ps = new PrintStream(fop);
			ps.println(stringData);
			ps.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public BigInteger getProblemId()
	{
		return problemId;
	}

	public File getDataFile()
	{
		return dataFile;
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
