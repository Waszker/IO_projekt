package GenericCommonClasses;

import pl.edu.pw.mini.se2.TaskSolver;
import Problems.TestProblem;
import XMLMessages.DivideProblem;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;

/**
 * <p>
 * ProblemHelper provides automatic problem type recognition from
 * DivideProblem message (for TaskManager) and PartialProblems message (for CN)
 * </p>
 * 
 * @author Filip
 */
public class ProblemHelper
{
	/**
	 * <p>
	 * Method recognizes type of problem and instantinates it from DivideProblem message.
	 * </p>
	 * @param message received DivideProblem message
	 * @return proper problem object
	 */
	public static TaskSolver instantinateTaskSolver(DivideProblem message)
	{
		return instantinate(message.getProblemType(), message.getData());
	}
	
	/**
	 * <p>
	 * Method recognizes type of problem and instantinates it from Solutiones message.
	 * </p>
	 * @param message received Solutiones message
	 * @return proper problem object
	 */
	public static TaskSolver instantinateTaskSolver(Solutiones message)
	{
		return instantinate(message.getProblemType(), null);
	}
	
	/**
	 * <p>
	 * Method recognizes type of problem and instantinates it from SolvePartialProblems message.
	 * </p>
	 * @param message received PartialProblems message
	 * @return proper problem object
	 */
	public static TaskSolver instantinateTaskSolver(SolvePartialProblems message)
	{
		return instantinate(message.getProblemType(), null);
	}
	
	
	//private method doing main work
	private static TaskSolver instantinate(String type, byte[] data)
	{
		TaskSolver ret = null;
		
		switch ( type )
		{
			case "TestProblem":
				ret = new TestProblem(data);
				break;
		}
		
		return ret;
	}
}
