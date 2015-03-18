package GenericCommonClasses;

import Problems.TestProblem;
import XMLMessages.DivideProblem;
import XMLMessages.SolvePartialProblems.PartialProblems;

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
	public static IProblem instantinateProblem(DivideProblem message)
	{
		return instantinate(message.getProblemType(), message.getData());
	}
	
	/**
	 * <p>
	 * Method recognizes types of problem and instantinates them from PartialProblems message.
	 * </p>
	 * @param message received PartialProblems message
	 * @return proper problem objects array
	 */
	public static IProblem[] instantinateProblem(PartialProblems message)
	{
		//TODO: Complete!!!!
		
		return null;
	}
	
	
	//private method doing main work
	private static IProblem instantinate(String type, byte[] data)
	{
		IProblem ret = null;
		
		switch ( type )
		{
			case "TestProblem":
				ret = new TestProblem();
				break;
			
			default:
				throw new UnsupportedOperationException("Unknown problem type!");
		}
		
		
		ret.loadData(data);
		return ret;
	}
}
