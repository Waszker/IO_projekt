package GenericCommonClasses;

import java.security.InvalidParameterException;

import XMLMessages.SolvePartialProblems;


/**
 * <p>
 * IProblem is an for any problem.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.0
 * 
 */
public interface IProblem
{
	/**
	 * <p>
	 * Gets problem type, for example "DVRP"
	 * </p>
	 * @return type of the problem
	 */
	String getType();
	
	/**
	 * <p>
	 * Gets binary data (necessary to solve the problem) in order to send it through network
	 * </p>
	 * @return binary data of a problem
	 */
	byte[] getData();
	
	/**
	 * <p>
	 * Loads received binary data of the problem.
	 * </p>
	 * @param data - binary data of a problem
	 * @throws InvalidParameterException
	 */
	void loadData(byte[] data) throws InvalidParameterException;
	
	/**
	 * <p>
	 * Divides problem into partial problems.
	 * </p>
	 * @param nodes Number of available computational nsodes
	 * @return array of partial problems
	 */
	IProblem[] divide(int nodes);
	
	/**
	 * <p>
	 * Executed on ComputationalNode. Runs the major computation.
	 * </p>
	 * @return result of computation
	 */
	Object doComputation();
	
	/**
	 * <p>
	 * Merges partial solutions and returns final solution.
	 * </p>
	 * @param partialSolutions - array of partial solution to merge (the order must be the same
	 * 							 as divide() method returned)
	 * @return merged final solution
	 */
	Object mergeSolutions(Object[] partialSolutions);
}
