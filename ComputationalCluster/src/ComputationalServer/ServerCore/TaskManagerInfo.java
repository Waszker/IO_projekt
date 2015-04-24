package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import DebugTools.Logger;

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
	List<BigInteger> assignedProblems;
	List<String> supportedProblems;

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
	TaskManagerInfo(BigInteger id, List<String> solvableProblems)
	{
		this.id = id;
		assignedProblems = new ArrayList<>();

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
		return "TaskManager [id=" + id + ", assignedProblems="
				+ assignedProblems.size() + ", supportedProblems="
				+ supportedProblems + "]";
	}

}
