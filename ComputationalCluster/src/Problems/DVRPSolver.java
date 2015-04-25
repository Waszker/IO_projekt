package Problems;

import java.util.Scanner;

import Problems.DVRPProblem.Depot;
import pl.edu.pw.mini.se2.TaskSolver;
import pl.edu.pw.mini.se2.TaskSolverState;

public class DVRPSolver extends TaskSolver
{
	/* CONSTANTS */
	public static final String PROBLEMNAME = "dvrpproblem";
	
	/* METHODS */
	
	/*
	 * input string format:
	 * 								  %d %f %f %d	 - number of vehicles, v. speed, v. capacity, number of clients
	 * 		first entry is depot	: %f %f %f %f 	 - x,y,startHour,endHour
	 * 		next entries are clients: %f %f %f %f %f - x,y,startHour,unloadTime,cargoSize
	 */
	
	public DVRPSolver(byte[] problemData)
	{
		super(problemData);
	}

	
	/*
	 * Partial problem format:
	 */
	@Override
	public byte[][] DivideProblem(int numOfNodes)
	{
		String problemInput = new String(_problemData);
		Scanner s = new Scanner(problemInput);
		
		int nov = s.nextInt(); //number of vehicles
		s.nextDouble();
		s.nextDouble();
		int noc = s.nextInt(); //number of clients
		
		int numOfVariations = (int)Math.pow(nov, noc); //number of possible divisions
		
		for ( int i=0; i<numOfNodes; i++ )
		{
			
		}
		
		s.close();
		return null;
	}

	/*
	 * Partial solution format: %d - lowest cost of
	 */
	@Override
	public byte[] MergeSolution(byte[][] partialSolutions)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] Solve(byte[] partialProblemData, long timeout)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		return PROBLEMNAME;
	}
	
	@Override
	public Exception getException()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public TaskSolverState getState()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void setException(Exception e)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void setState(TaskSolverState arg0)
	{
		throw new UnsupportedOperationException();
	}

}
