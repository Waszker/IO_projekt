package GenericCommonClasses;

import java.nio.charset.Charset;

import pl.edu.pw.mini.se2.TaskSolver;
//import pl.edu.pw.mini.se2.okulewicz.IntegralTaskSolver;
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
	public static String[] types = {
		"TestProblem", "IntegralProblem"
	};
	
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
		return instantinate(message.getProblemType(), message.getCommonData());
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
		return instantinate(message.getProblemType(), message.getCommonData());
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
				
//			case "IntegralProblem":
//				ret = new IntegralTaskSolver(data);
//				break;
		}
		
		return ret;
	}
	
	/**
	 * <p>
	 * Turns binary data of result into form readable by human. 
	 * </p>
	 * @param problemType type of problem; how to interprete data
	 * @param data result binary data
	 * @return result readable by human
	 */
	public static String extractResult(String problemType, byte[] data)
	{
		switch(problemType)
		{
			case "TestProblem":
				return new String(data);
				

			case "IntegralProblem":
				return new String(data, Charset.forName("UTF-8"));
				
			default:
				return null;
		}
	}
}
