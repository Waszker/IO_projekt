package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import XMLMessages.Register;

/**
 * <p>
 * Class representing connected TaskManager in ComputationalCluster. It holds
 * information about current problems assigned to it.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class TaskManagerInfo
{
	/******************/
	/* VARIABLES */
	/******************/
	BigInteger id;
	Register info;
	List<BigInteger> assignedProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates TaskManager information with assigned id.
	 * </p>
	 * 
	 * @param id
	 */
	TaskManagerInfo(BigInteger id, Register message)
	{
		this.id = id;
		info = message;
		assignedProblems = new ArrayList<>(info.getParallelThreads());
	}

}
