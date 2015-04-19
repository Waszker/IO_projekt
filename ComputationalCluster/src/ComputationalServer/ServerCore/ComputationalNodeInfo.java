package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import DebugTools.Logger;
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
	ComputationalNodeInfo(BigInteger id, Register message)
	{
		this.id = id;
		info = message;
		assignedPartialProblems = new ConcurrentHashMap<>();
		try
		{
			supportedProblems = new ArrayList<>(message.getSolvableProblems()
					.getProblemName());
		}
		catch (NullPointerException e)
		{
			Logger.log("Looks like component " + id
					+ " solves no problems at all!\n");
		}
	}

	/**
	 * <p>
	 * Returns number of unoccupied threads.
	 * </p>
	 * 
	 * @return
	 */
	int getFreeThreads()
	{
		int partialProblemsCount = 0;
		for (Map.Entry<BigInteger, List<PartialProblem>> entry : this.assignedPartialProblems
				.entrySet())
		{
			partialProblemsCount += entry.getValue().size();
		}

		return this.info.getParallelThreads() - partialProblemsCount;
	}

	@Override
	public String toString()
	{
		return "ComputationalNodeInfo [id=" + id + ", info=" + info
				+ ", supportedProblems=" + supportedProblems
				+ ", assignedPartialProblems=" + assignedPartialProblems.size()
				+ "]";
	}
}
