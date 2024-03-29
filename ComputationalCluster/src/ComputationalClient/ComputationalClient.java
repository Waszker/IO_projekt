package ComputationalClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import Problems.DVRPSolver;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers;
import XMLMessages.Register;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import XMLMessages.Status;

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
	public static final String DEFAULT_TIMEOUT = "0";
	public static final String DEFAULT_CUTOFFTIME = "0";

	private BigInteger problemId;
	protected File dataFile;
	private byte[] solutionData;
	protected String filePath;
	private BigInteger timeout;
	private BigInteger cutOffTime;
	protected boolean computationIsDone;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClient(String address, Integer port,
			boolean isGuiEnabled, String filePath, Integer timeout,
			Integer cutOffTime)
	{
		super(address, port, isGuiEnabled, ComponentType.ComputationalClient);

		this.filePath = filePath;
		if (null != timeout)
			this.timeout = new BigInteger(timeout.toString());
		else
			this.timeout = new BigInteger(DEFAULT_TIMEOUT);

		if (null != filePath) dataFile = new File(filePath);

		if (null != cutOffTime)
			this.cutOffTime = new BigInteger(cutOffTime.toString());
		else
			this.cutOffTime = new BigInteger(DEFAULT_CUTOFFTIME);

		computationIsDone = false;

	}

	/**
	 * <p>
	 * Returns problemId.
	 * </p>
	 * 
	 * @return
	 */
	public BigInteger getProblemId()
	{
		return problemId;
	}

	/**
	 * <p>
	 * Returns file containing the problem definition.
	 * </p>
	 * 
	 * @return
	 */
	public File getDataFile()
	{
		return dataFile;
	}

	/**
	 * <p>
	 * Returns timeout.
	 * </p>
	 * 
	 * @return
	 */
	public BigInteger getTimeout()
	{
		return timeout;
	}

	/**
	 * <p>
	 * Sets timeout.
	 * </p>
	 */
	public void setTimeout(BigInteger timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * <p>
	 * Returns cutoff time.
	 * </p>
	 * 
	 * @return
	 */
	public BigInteger getCutOffTime()
	{
		return cutOffTime;
	}

	/**
	 * <p>
	 * Sets cutoff time.
	 * </p>
	 */
	public void setCutOffTime(BigInteger cutOffTime)
	{
		this.cutOffTime = cutOffTime;
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

	@Override
	protected Status getStatusMessage()
	{
		return null;
	}

	protected void sendSolveRequestMessage()
	{
		try
		{
			byte[] data = loadFile(this.dataFile, this.cutOffTime.intValue());
			SolveRequest sr = new SolveRequest();
			sr.setProblemType(DVRPSolver.PROBLEMNAME);
			sr.setSolvingTimeout(this.timeout);
			sr.setData(data);
			this.sendMessages(sr);
			ReactToReceivedMessage();

		}
		catch (IOException e)
		{
			e.printStackTrace();
			ipAddress = backupServerIp;
			port = backupServerPort;
			sendSolveRequestMessage();

		}
	}

	protected void sendSolutionRequestMessage()
	{
		SolutionRequest sr = new SolutionRequest();
		sr.setId(this.problemId);
		try
		{
			if (isGui == false) Thread.sleep(10000);
			this.sendMessages(sr);
			ReactToReceivedMessage();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			ipAddress = backupServerIp;
			port = backupServerPort;
			sendSolutionRequestMessage();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void ReactToReceivedMessage()
	{
		try
		{
			List<IMessage> messages = receiveMessage();
			if (messages.size() == 0) return;

			for (int i = 0; i < messages.size(); i++)
			{
				IMessage message = messages.get(i);
				if (message.getMessageType() == MessageType.NO_OPERATION)
				{
					BackupCommunicationServers backups = ((NoOperation) message)
							.getBackupCommunicationServers();
					if (backups != null
							&& backups.getBackupCommunicationServer() != null)
					{
						backupServerIp = backups.getBackupCommunicationServer()
								.getAddress();
						backupServerPort = backups
								.getBackupCommunicationServer().getPort();
						Logger.log("NO_OPERATION -->  IP: " + backupServerIp
								+ " PORT: " + backupServerPort + "\n");
					}
				}
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
						if (isGui == false) sendSolutionRequestMessage();
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
			}
		}
		catch (IOException e)
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
			if (!outputFile.exists()) outputFile.createNewFile();
			FileOutputStream fop = new FileOutputStream(outputFile);
			PrintStream ps = new PrintStream(fop);
			ps.println(stringData);
			ps.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static byte[] loadFile(File file, int cutOffTime)
			throws IOException
	{

		int vehicles = -1;
		int capacities = -1;
		int depotnumber = -1;
		int clients = -1;
		double[][] vehiclesanddepotinfo = null;
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line = null;
		while ((line = br.readLine()) != null)
		{
			Scanner linia = new Scanner(line);
			switch (linia.next())
			{
				case "NUM_VISITS:":
					clients = linia.nextInt();
					break;
				case "NUM_VEHICLES:":
					vehicles = linia.nextInt();
					vehiclesanddepotinfo = new double[vehicles + 1][5];
					break;
				case "CAPACITIES:":
					capacities = linia.nextInt();
					break;
				case "DEPOTS":
					String depotsline = br.readLine();
					Scanner liniad = new Scanner(depotsline);
					depotnumber = liniad.nextInt();
					break;
				case "DEMAND_SECTION":
					for (int i = 0; i < vehicles; i++)
					{
						String vehicleline = br.readLine();
						Scanner linia2 = new Scanner(vehicleline);
						int vehiclenumber = linia2.nextInt();
						int capacity = linia2.nextInt();
						if (capacity < 0) capacity = -capacity;
						vehiclesanddepotinfo[vehiclenumber][4] = capacity;
						linia2.close();
					}
					break;
				case "LOCATION_COORD_SECTION":
					for (int i = 0; i < vehicles + 1; i++)
					{
						String vehicleline = br.readLine();
						Scanner linia2 = new Scanner(vehicleline);
						int vehiclenumber = linia2.nextInt();
						int x = linia2.nextInt();
						int y = linia2.nextInt();
						vehiclesanddepotinfo[vehiclenumber][0] = x;
						vehiclesanddepotinfo[vehiclenumber][1] = y;
						linia2.close();
					}
					break;
				case "DEPOT_LOCATION_SECTION":
					break;
				case "VISIT_LOCATION_SECTION":
					break;
				case "DURATION_SECTION":
					for (int i = 0; i < vehicles; i++)
					{
						String vehicleline = br.readLine();
						Scanner linia2 = new Scanner(vehicleline);
						int vehiclenumber = linia2.nextInt();
						int unloadTime = linia2.nextInt();
						vehiclesanddepotinfo[vehiclenumber][3] = unloadTime;
						linia2.close();
					}
					break;
				case "DEPOT_TIME_WINDOW_SECTION":
				{
					String depottime = br.readLine();
					Scanner linia2 = new Scanner(depottime);
					int depotnum = linia2.nextInt();
					int startHour = linia2.nextInt();
					int endHour = linia2.nextInt();
					vehiclesanddepotinfo[depotnum][2] = startHour;
					vehiclesanddepotinfo[depotnum][3] = endHour;
					if (cutOffTime == Integer.parseInt(DEFAULT_CUTOFFTIME))
						cutOffTime = (int) (endHour / 2);
					linia2.close();
					break;
				}
				case "TIME_AVAIL_SECTION":
					for (int i = 0; i < vehicles; i++)
					{
						String vehicleline = br.readLine();
						Scanner linia2 = new Scanner(vehicleline);
						int vehiclenumber = linia2.nextInt();
						int vstartHour = linia2.nextInt();
						vehiclesanddepotinfo[vehiclenumber][2] = vstartHour;
						linia2.close();
					}
					break;
			}
			linia.close();
		}

		br.close();
		/*
		 * input string format: %d %f %f %d - number of vehicles, v. speed, v.
		 * capacity, number of clients first entry is depot : %f %f %f %f -
		 * x,y,startHour,endHour next entries are clients: %f %f %f %f %f -
		 * x,y,startHour,unloadTime,cargoSize
		 */
		for (int i = 0; i < vehicles + 1; i++)
		{
			if (i != depotnumber)
			{
				if (vehiclesanddepotinfo[i][3] >= cutOffTime)
					vehiclesanddepotinfo[i][3] = 0;
			}
		}
		String text = vehicles + " " + "1" + " " + capacities + " " + clients;
		for (int i = 0; i < vehicles + 1; i++)
		{
			if (i != depotnumber)
				text = text + "\n" + vehiclesanddepotinfo[i][0] + " "
						+ vehiclesanddepotinfo[i][1] + " "
						+ vehiclesanddepotinfo[i][2] + " "
						+ vehiclesanddepotinfo[i][3] + " "
						+ vehiclesanddepotinfo[i][4];
			else
				text = text + "\n" + vehiclesanddepotinfo[i][0] + " "
						+ vehiclesanddepotinfo[i][1] + " "
						+ vehiclesanddepotinfo[i][2] + " "
						+ vehiclesanddepotinfo[i][3];
		}
		byte[] bytes = text.getBytes();
		return bytes;
	}

}
