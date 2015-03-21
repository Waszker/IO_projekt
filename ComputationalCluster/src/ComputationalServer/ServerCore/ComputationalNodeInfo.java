package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import XMLMessages.Register;
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
	Register info;
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
	ComputationalNodeInfo(BigInteger id, Register message)
	{
		this.id = id;
		info = message;
		assignedPartialProblems = new ConcurrentHashMap<>();
	}
}
