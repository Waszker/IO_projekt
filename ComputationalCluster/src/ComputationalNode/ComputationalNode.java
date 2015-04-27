package ComputationalNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import pl.edu.pw.mini.se2.TaskSolver;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import GenericCommonClasses.ProblemHelper;
import XMLMessages.NoOperation;
import XMLMessages.NoOperation.BackupCommunicationServers.BackupCommunicationServer;
import XMLMessages.Register;
import XMLMessages.Register.SolvableProblems;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import XMLMessages.Status;
import XMLMessages.Status.Threads;

//import XMLMessages.Status.Threads.Thread;

public class ComputationalNode extends GenericComponent
{
	private boolean firstNoOp = true; // if true, print assigned id, then turn
										// into false
	private byte[] solutions; // list of solved solutions
	private byte[] commonData;
	private BigInteger problemId;
	private String problemType;
	private BigInteger taskId;
	private TaskSolver currentProblemTaskSolver = null; // if null - we have no
														// problem assigned
	private BigInteger timeout = BigInteger.valueOf(0);
	
	private boolean busy = false;

	public ComputationalNode(String serverIpAddress, Integer serverPort,
			boolean isGui)
	{
		super(serverIpAddress, serverPort, isGui,
				ComponentType.ComputationalNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register r = new Register();
		r.setType(ComponentType.ComputationalNode.name);
		r.setParallelThreads((short) 1);

		SolvableProblems problems = (new SolvableProblems());
		problems.getProblemName().addAll(Arrays.asList(ProblemHelper.types));
		r.setSolvableProblems(problems);
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		if (message.getMessageType() == MessageType.NO_OPERATION)
		{
			// print id only once
			if (firstNoOp)
			{
				Logger.log("Your id is: " + id + "\n");
				firstNoOp = false;
			}
			
			BackupCommunicationServer bcs = ((NoOperation)message).getBackupCommunicationServers().getBackupCommunicationServer();
			
			if (bcs != null)
			{
				backupServerIp = bcs.getAddress();
				backupServerPort = bcs.getPort();
			}
		} else if (message.getMessageType() == MessageType.PARTIAL_PROBLEM)
		{
			Logger.log("Node: " + id + " recieved PARTIAL_PROBLEM message\n");
			handlePartialProblemMessage((SolvePartialProblems) message);
		} else if (message.getMessageType() == MessageType.ERROR)
		{
			showError("Error message from server: "
					+ ((XMLMessages.Error) message).getErrorType());
		}

	}

	private void handlePartialProblemMessage(SolvePartialProblems sppm)
	{
		commonData = sppm.getCommonData();
		problemId = sppm.getId();
		problemType = sppm.getProblemType();
		timeout = sppm.getSolvingTimeout();

		Logger.log("Type of message: " + problemType + "\n");

		List<PartialProblem> lpp = sppm.getPartialProblems()
				.getPartialProblem();

		currentProblemTaskSolver = ProblemHelper.instantinateTaskSolver(sppm);
		busy = true;
		
		for (int i = 0; i < lpp.size(); ++i)
		{
			taskId = lpp.get(i).getTaskId();
			solutions = currentProblemTaskSolver.Solve(lpp.get(i).getData(),
					timeout.longValue());
			sendSolutionsMessage();
		}
		
		busy = false;
	}

	protected void sendSolutionsMessage()
	{
		Logger.log("sendSolutionsMessage\n");
		Solutiones response = new Solutiones();

		response.setCommonData(commonData);
		response.setId(problemId);
		response.setProblemType(problemType);

		Solution s = new Solution();
		s.setTaskId(taskId);
		s.setTimeoutOccured(false);
		s.setType("Partial");

		s.setData(solutions);

		Solutions ss = new Solutions();
		ss.getSolution().add(s);

		response.setSolutions(ss);

		Logger.log("Problem Type: " + problemType + " " + id
				+ " sending partial solution: " + taskId + "\n");
		Logger.log("Solution "
				+ ProblemHelper.extractResult(problemType, solutions) + "\n");

		try
		{
			this.sendMessages(response);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected Status getStatusMessage()
	{
		Status ret = new Status();
		Threads threads = new Threads();
		Threads.Thread thread = new Threads.Thread();
		thread.setHowLong(BigInteger.valueOf(5000));
		thread.setState(busy ? "Busy" : "Idle");
		threads.getThread().add(thread);

		ret.setId(id);
		ret.setThreads(threads);
		return ret;
	}
}
