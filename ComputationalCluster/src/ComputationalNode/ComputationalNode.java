package ComputationalNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import Problems.TestProblem;
import XMLMessages.Register;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

public class ComputationalNode extends GenericComponent
{
	private boolean firstNoOp = true; // if true, print assigned id, then turn
										// into false
	private byte[] solutions; // list of solved solutions
	private byte[] commonData;
	private BigInteger problemId;
	private String problemType;
	private BigInteger taskId;
	private boolean finalSolution = false;

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
		} else if (message.getMessageType() == MessageType.PARTIAL_PROBLEM)
		{
			Logger.log("Node: " + id + "recieved PARTIAL_PROBLEM message\n");
			handlePartialProblemMessage((SolvePartialProblems) message);
		}

	}

	private void handlePartialProblemMessage(SolvePartialProblems sppm)
	{
		// TODO
		commonData = sppm.getCommonData();
		problemId = sppm.getId();
		problemType = sppm.getProblemType();
		
		if (problemType == "TestProblem")
		{
			TestProblem tp = new TestProblem(commonData);

			List<PartialProblem> lpp = sppm.getPartialProblems()
					.getPartialProblem();

			for (int i = 0; i < lpp.size(); ++i)
			{
				taskId = lpp.get(i).getTaskId();
				solutions = tp.Solve(lpp.get(i).getData(), sppm
						.getSolvingTimeout().longValue());
				
				if (i == lpp.size() - 1)
				{
					finalSolution = true;
				}
				
				sendSolutionsMessage();
			}
		}
	}
	
	protected void sendSolutionsMessage()
	{
		Solutiones response = new Solutiones();
		
		response.setCommonData(commonData);
		response.setId(problemId);
		response.setProblemType(problemType);
		
		Solution s = new Solution();
		s.setTaskId(taskId);
		s.setTimeoutOccured(false);
		
		if (!finalSolution)
		{
			s.setType("Ongoing");
		}
		else
		{
			s.setType("Partial");
		}
		
		s.setData(solutions);
		
		Solutions ss = new Solutions();
		List<Solution> ls = ss.getSolution();
		ls.add(s);
		
		response.setSolutions(ss);
		
		try
		{
			this.sendMessages(response);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
