package TaskManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import pl.edu.pw.mini.se2.TaskSolver;
import XMLMessages.DivideProblem;
import XMLMessages.Register;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.ProblemHelper;
import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * TaskManager is a class providing the TaskManager logic.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.0
 */
public final class TaskManager extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	
	private TaskSolver currentProblemTaskSolver = null; //if null - we hav no problem assigned
	private boolean firstNoOp = true; //if true, print assigned id, then turn into false

	/******************/
	/* FUNCTIONS */
	/******************/

	public TaskManager(String address, Integer port, boolean isGuiEnabled)
	{
		super(address, port, isGuiEnabled, ComponentType.TaskManager);
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register r = new Register();
		r.setType(ComponentType.TaskManager.name);
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		switch (message.getMessageType())
		{
			case NO_OPERATION:
				//print id only once
				if ( firstNoOp )
				{
					Logger.log("Your id is: "+id+"\n");
					firstNoOp = false;
				}
				break;
			case ERROR:
				showError("Error message from server: " + ((XMLMessages.Error)message).getErrorDetails());
				break;
			case DIVIDE_PROBLEM:
				handleDivideProblemMessage((DivideProblem)message);
				break;
			default:
				break;
		}
	}
	
	
	private void handleDivideProblemMessage(DivideProblem dvm)
	{
		if ( currentProblemTaskSolver != null )
		{
			//TODO: Error - we have other job assigned
			return;
		}
		
		//divide problem
		currentProblemTaskSolver = ProblemHelper.instantinateTaskSolver(dvm);
		final int numOfComputationalNodes = dvm.getComputationalNodes().intValue();
		byte[][] divided = currentProblemTaskSolver.DivideProblem(numOfComputationalNodes);
		
		//prepare message
		SolvePartialProblems response = new SolvePartialProblems();
		response.setProblemType(dvm.getProblemType());
		response.setId(dvm.getId());
		response.setSolvingTimeout(BigInteger.valueOf(0));
		response.setCommonData(null);
		
		PartialProblems partialProblems = new PartialProblems();
		List<PartialProblem> list = partialProblems.getPartialProblem();
		for ( int i=0; i<divided.length; i++ )
		{
			PartialProblem pp = new PartialProblem();
			pp.setNodeID(id);
			pp.setTaskId(BigInteger.valueOf(i));
			pp.setData(divided[i]);
			
			list.add(pp);
		}
		response.setPartialProblems(partialProblems);
		
		
		//send the response
		try
		{
			sendMessages(response);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			currentProblemTaskSolver = null;
		}
	}
	
	private void handleSolutionsMessage(Solutiones sm)
	{
		if ( currentProblemTaskSolver == null )
		{
			//TODO: Error - no active jobs
			return;
		}
		
		//retreive the solutions
		List<Solution> list = sm.getSolutions().getSolution();
		byte[][] solutions = new byte[list.size()][];
		for ( int i=0; i<list.size(); i++ ) //kolejność po getTaskID
			solutions[i] = list.get(i).getData();
		
		byte[] dataToSend = currentProblemTaskSolver.MergeSolution(solutions);
		
		Solution finalSolution = new Solution();
		finalSolution.setTaskId(BigInteger.valueOf(0));
		finalSolution.setType("Final");
		finalSolution.setData(dataToSend);
		
		Solutions solutionsToSend = new Solutions();
		solutionsToSend.getSolution().add(finalSolution);
		
		Solutiones response = new Solutiones();
		response.setProblemType(sm.getProblemType());
		response.setId(sm.getId());
		response.setSolutions(solutionsToSend);
		
		try
		{
			sendMessages(response);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		currentProblemTaskSolver = null;
	}
}
