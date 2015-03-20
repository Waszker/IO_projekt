package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import XMLMessages.Register;

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
	List<ProblemInfo> assignedProblems;

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
		assignedProblems = new ArrayList<>(info.getParallelThreads());		
	}
}
