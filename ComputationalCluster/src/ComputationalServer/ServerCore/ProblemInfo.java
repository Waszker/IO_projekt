package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import XMLMessages.Solutiones.Solutions.Solution;

/**
 * <p>
 * Class used by ComputationalServer to hold information about problem instance
 * and its current state.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class ProblemInfo
{
	/******************/
	/* VARIABLES */
	/******************/
	enum ProblemStatus
	{
		Ongoing("Ongoing"), Partial("Partial"), Final("Final");

		String text;

		private ProblemStatus(String text)
		{
			this.text = text;
		}
	}

	BigInteger id, timeout;
	byte[] data;
	boolean isProblemDivided;
	boolean isProblemReadyToSolve;
	boolean isProblemCurrentlyDelegated;
	String problemType;
	
	int parts;
	List<Solution> partialSolutions;
	Solution finalSolution;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates problem with specified values. Initial segmentation is set to -1.
	 * </p>
	 * 
	 * @param id
	 * @param data
	 * @param problemType
	 * @param solvingTimeout
	 */
	public ProblemInfo(BigInteger id, byte[] data, String problemType,
			BigInteger solvingTimeout)
	{
		this.id = id;
		this.data = data;
		this.problemType = problemType;
		this.timeout = solvingTimeout;
		this.parts = -1;

		isProblemDivided = isProblemReadyToSolve = isProblemCurrentlyDelegated = false;
		partialSolutions = new ArrayList<>();
		finalSolution = null;
	}

	@Override
	public String toString()
	{
		return "ProblemInfo [id=" + id + ", timeout=" + timeout + ", data="
				+ Arrays.toString(data) + ", isProblemDivided="
				+ isProblemDivided + ", isProblemReadyToSolve="
				+ isProblemReadyToSolve + ", isProblemCurrentlyDelegated="
				+ isProblemCurrentlyDelegated + ", problemType=" + problemType
				+ ", parts=" + parts + ", partialSolutions="
				+ partialSolutions.size() + ", finalSolution=" + finalSolution
				+ "]";
	}

}
