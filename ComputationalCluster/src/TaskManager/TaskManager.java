package TaskManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import pl.edu.pw.mini.se2.TaskSolver;
import XMLMessages.DivideProblem;
import XMLMessages.Register;
import XMLMessages.Solutiones;
import XMLMessages.Register.SolvableProblems;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.ProblemHelper;

/**
 * <p>
 * TaskManager is a class providing the TaskManager logic.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.1
 */
public final class TaskManager extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	
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
		r.setParallelThreads((short)1);
		

		SolvableProblems problems = (new SolvableProblems());
		problems.getProblemName().addAll(Arrays.asList(ProblemHelper.types));
		r.setSolvableProblems(problems);
		
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
			case SOLUTION:
				handleSolutionsMessage((Solutiones)message);
				break;
			default:
				break;
		}
	}
	
	private void handleDivideProblemMessage(DivideProblem dvm)
	{
		Logger.log("Received problem to divide...\n");
		
		if ( dvm.getNodeID().compareTo(id) != 0 )
		{
			Logger.log( "Wrong id!\n");
			
			//TODO: Error - shouldn't receive this message (not our id)
			return;
		}
		
		TaskSolver currentProblemTaskSolver = ProblemHelper.instantinateTaskSolver(dvm);
		if ( currentProblemTaskSolver == null )
		{
			unknownProblemType();
			return;
		}
		
		Logger.log("Dividing problem among " + dvm.getComputationalNodes().intValue() + " nodes...\n");
		SolvePartialProblems response = generateResponse(dvm, currentProblemTaskSolver, id);
		try
		{
			Logger.log( "OK. Sending response...\n");
			sendMessages(response);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleSolutionsMessage(Solutiones sm)
	{
		Logger.log("Received solutions to merge...\n");
		
		TaskSolver currentProblemTaskSolver = ProblemHelper.instantinateTaskSolver(sm);
		if ( currentProblemTaskSolver == null )
		{
			unknownProblemType();
			return;
		}
		
		Logger.log("Merging " + sm.getSolutions().getSolution().size() + " results...\n");
		Solutiones response = generateResponse(sm, currentProblemTaskSolver);
		try
		{
			Logger.log("Sending response...\n");
			sendMessages(response);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void unknownProblemType()
	{
		//TODO: Error - unknown problem type
	}
	
	private static SolvePartialProblems generateResponse(DivideProblem dvm, TaskSolver ts, BigInteger id)
	{
		//divide problem
		final int numOfComputationalNodes = dvm.getComputationalNodes().intValue();
		byte[][] divided = ts.DivideProblem(numOfComputationalNodes);
			
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
		return response;
	}
	
	private static Solutiones generateResponse(Solutiones sm, TaskSolver ts)
	{
		//retreive the solutions
		List<Solution> list = sm.getSolutions().getSolution();
		byte[][] solutions = new byte[list.size()][];
		for ( int i=0; i<list.size(); i++ ) // order by TaskID
			solutions[i] = list.get(i).getData();
				
		byte[] dataToSend = ts.MergeSolution(solutions);
			
		Solution finalSolution = new Solution();
		finalSolution.setTaskId(null);
		finalSolution.setType("Final");
		finalSolution.setData(dataToSend);
				
		Solutions solutionsToSend = new Solutions();
		solutionsToSend.getSolution().add(finalSolution);
				
		Solutiones response = new Solutiones();
		response.setProblemType(sm.getProblemType());
		response.setId(sm.getId());
		response.setSolutions(solutionsToSend);
		return response;
	}
}
