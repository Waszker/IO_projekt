package ComputationalClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
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
	public static final String DEFAULT_TIMEOUT = "10000";

	private BigInteger problemId;
	protected File dataFile;
	private byte[] solutionData;
	protected String filePath;
	private BigInteger timeout;
	protected boolean computationIsDone;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClient(String address, Integer port,
			boolean isGuiEnabled, String filePath, Integer timeout)
	{
		super(address, port, isGuiEnabled, ComponentType.ComputationalClient);
		this.filePath = filePath;
		if (null != timeout)
			this.timeout = new BigInteger(timeout.toString());
		else
			this.timeout = new BigInteger(DEFAULT_TIMEOUT);
		if (null != filePath)
			dataFile = new File(filePath);
		computationIsDone = false;
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
			byte[] data = loadFile(this.dataFile);
			SolveRequest sr = new SolveRequest();
			sr.setProblemType("TestProblem");
			sr.setSolvingTimeout(this.timeout);
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
			if (isGui == false)
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
			if (messages.size() == 0)
				return;
			IMessage message = messages.get(0);
			if (message.getMessageType() == MessageType.SOLVE_REQUEST_RESPONSE)
			{
				this.problemId = ((SolveRequestResponse) message).getId();
				Logger.log("problem id: " + this.problemId + "\n");
			}
			if (message.getMessageType() == MessageType.SOLUTION)
			{
				String problemType = ((Solutiones) message).getSolutions()
						.getSolution().get(0).getType();
				Logger.log("Received problem type: " + problemType + "\n");
				if (problemType.contentEquals("Ongoing"))
				{
					if (isGui == false)
						sendSolutionRequestMessage();
				}
				if (problemType.contentEquals("Final"))
				{
					solutionData = ((Solutiones) message).getSolutions()
							.getSolution().get(0).getData();
					String stringData = new String(solutionData);
					Logger.log("\n\nMessage content : \n***\n" + stringData
							+ "\n***\n\n");
					this.computationIsDone = true;
					SaveSolution();
				}
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
			ext = "." + ext;
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

	public BigInteger getTimeout()
	{
		return timeout;
	}

	public void setTimeout(BigInteger timeout)
	{
		this.timeout = timeout;
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
