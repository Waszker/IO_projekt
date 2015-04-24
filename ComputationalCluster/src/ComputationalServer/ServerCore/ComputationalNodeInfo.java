package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import DebugTools.Logger;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

/**
 * <p>
 * Class representing ComputationalNode in ComputationalServer internal
 * information list.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class ComputationalNodeInfo
{
	/******************/
	/* VARIABLES */
	/******************/
	BigInteger id;
	List<String> supportedProblems;
	/**
	 * Map with key responding to ProblemInfo and containing PartialProblems
	 * assigned for this Node.
	 */
	ConcurrentHashMap<BigInteger, List<PartialProblem>> assignedPartialProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates ComputationalNode information with assigned id.
	 * </p>
	 * 
	 * @param id
	 */
	ComputationalNodeInfo(BigInteger id, List<String> solvableProblems)
	{
		this.id = id;
		assignedPartialProblems = new ConcurrentHashMap<>();
		try
		{
			supportedProblems = new ArrayList<>(solvableProblems);
		}
		catch (NullPointerException e)
		{
			Logger.log("Looks like component " + id
					+ " solves no problems at all!\n");
		}
	}

	@Override
	public String toString()
	{
		return "ComputationalNodeInfo [id=" + id + ", supportedProblems="
				+ supportedProblems + ", assignedPartialProblems="
				+ assignedPartialProblems.size() + "]";
	}
}
