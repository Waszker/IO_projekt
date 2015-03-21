package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import XMLMessages.SolveRequest;

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

	List<PartialProblem> partialProblems;
	List<Solution> partialSolutions;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates problem instance with specified data and initial segmentation set
	 * to -1 (no partial problems for now).
	 * </p>
	 * 
	 * @param data
	 */
	public ProblemInfo(BigInteger id, SolveRequest message)
	{
		this.id = id;
		this.data = message.getData().clone();
		this.problemType = message.getProblemType();
		this.timeout = message.getSolvingTimeout();
		this.parts = 0;

		isProblemDivided = isProblemReadyToSolve = isProblemCurrentlyDelegated = false;
		partialProblems = new ArrayList<>();
		partialSolutions = new ArrayList<>();
	}

}
