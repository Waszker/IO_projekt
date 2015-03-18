package GenericCommonClasses;

import java.security.InvalidParameterException;


/**
 * <p>
 * IProblem is an for any problem.
 * </p>
 * 
 * @author Filip Turkot
 * @version 1.0
 * 
 */
interface IProblem
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
	 * Gets binary data needed to solve the problem
	 * </p>
	 * @return binary data of a problem
	 */
	byte[] getData();
	
	/**
	 * <p>
	 * Loads binary data needed to solve the problem
	 * </p>
	 * @param data - binary data of a problem
	 * @throws InvalidParameterException
	 */
	void loadData(byte[] data) throws InvalidParameterException;
}
