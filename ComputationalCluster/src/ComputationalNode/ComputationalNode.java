package ComputationalNode;

import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import Problems.TestProblem;
import XMLMessages.Register;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

public class ComputationalNode extends GenericComponent
{
	private boolean firstNoOp = true; // if true, print assigned id, then turn
										// into false
	private List<byte[]> solutions; // list of solved solutions

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
			Logger.log("Node: " + id + "recieved PARTIAL_PROBLEM message");
			handlePartialProblemMessage((SolvePartialProblems) message);
		}

	}

	private void handlePartialProblemMessage(SolvePartialProblems sppm)
	{
		// TODO
		if (sppm.getProblemType() == "TestProblem")
		{
			TestProblem tp = new TestProblem(sppm.getCommonData());

			List<PartialProblem> lpp = sppm.getPartialProblems()
					.getPartialProblem();

			for (int i = 0; i < lpp.size(); ++i)
			{
				solutions.add(tp.Solve(lpp.get(i).getData(), sppm
						.getSolvingTimeout().longValue()));
			}
		}
	}
}
