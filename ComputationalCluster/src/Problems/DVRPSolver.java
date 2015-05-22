package Problems;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.List;
import Problems.DVRPProblem.SeparateDVRPSolver.PathNode;
import Problems.DVRPProblem.Client;
import Problems.DVRPProblem.Depot;
import Problems.DVRPProblem.Graph;
import Problems.DVRPProblem.SeparateDVRPSolver;
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
	 * Partial problem format: 'S %d %d' from, to variation numbers; means that we need to handle [from,to) variations
	 * 					   or: 'P ...............'
	 */
	@Override
	public byte[][] DivideProblem(int numOfNodes)
	{
		String problemInput = new String(_problemData);
		Scanner s = new Scanner(problemInput);
		s.useLocale(Locale.ENGLISH);
		
		int nov = s.nextInt(); //number of vehicles
		s.nextDouble();
		s.nextDouble();
		int noc = s.nextInt(); //number of clients
		s.close();
		
		int numOfVariations = (int)Math.pow(nov, noc); //number of possible divisions (partitions)
		byte[][] ret = new byte[numOfNodes][];
			
		double variationPerNode = (double)numOfVariations / (double) numOfNodes;
		for ( int i=0; i<numOfNodes; i++ )
		{
			int from = (int)(i*variationPerNode);
			int to = (int)((i+1)*variationPerNode); // vartiations to handle by one node: [from, to)
				
			ret[i] = ("S " + from + " " + to).getBytes(); //S means that there is a set of whole TSP problems
														  //to solve (maybe single element set)
		}
				
		return ret;
	}

	/*
	 * Partial solution format: 'S %f' - lowest cost of solution
	 * 						or: 'P beginPath %f' - lowest cost of solution
	 */
	@Override
	public byte[] MergeSolution(byte[][] partialSolutions)
	{
		Double lowestCost = Double.MAX_VALUE;
		String ret = lowestCost.toString() + "\n-1";
		
		for ( int i=0; i<partialSolutions.length; i++ )
		{
			String _s = new String(partialSolutions[i]);
			Scanner s = new Scanner(_s);
			s.useLocale(Locale.ENGLISH);
			s.next();
			
			double cost = s.nextDouble();
			if ( cost < lowestCost )
			{
				lowestCost = cost;
				ret = _s;
			}			
			s.close();
		}
		
		return ret.getBytes();
	}

	@Override
	public byte[] Solve(byte[] partialProblemData, long timeout)
	{
		String _s = new String(partialProblemData);
		Scanner s = new Scanner(_s);
		s.useLocale(Locale.ENGLISH);
		String ret = "";
		ProblemData pd = extractProblemData();
		Graph g = new Graph(pd.d,pd.clients,pd.vehicleSpeed);
		List<PathNode[]> pathForEachVehicle = null;
		
		s.next();
		final int from = s.nextInt();
		final int to = s.nextInt();
		Double lowerCost = Double.MAX_VALUE;
		for ( int i=from; i<to; i++ )
		{
			List<PathNode[]> pathList = new LinkedList<>();
			double cost = SeparateDVRPSolver.solveDVRPOnGraphSet(g.divideGraph(pd.numberOfVehicles, i), pd.vehicleCapacity, pathList);
			if ( cost < lowerCost )
			{
				lowerCost = cost;
				pathForEachVehicle = pathList; 
			}
		}
			
		ret = "S " + lowerCost.toString();
			
		// Encode path to the message (variable pathForEachVehilce)
		if (null == pathForEachVehicle)
			ret += "\n-1";
		else
		{
			for (PathNode[] path : pathForEachVehicle)
			{
				if (path == null)
					continue;
				else
				{
					ret += "\n" + path.length;
					for (int j = 0; j < path.length; j++)
						ret += " " + path[j].x + " " + path[j].y;
				}
			}
		}
		
		s.close();
		return ret.getBytes();
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

	/* PRIVATE AUXILIARY FUNCTIONS */
	
	private ProblemData extractProblemData()
	{
		int numberOfClients;
		String _s = new String(_problemData);
		Scanner s = new Scanner(_s);
		s.useLocale(Locale.ENGLISH);
		
		ProblemData ret = new ProblemData();
		ret.numberOfVehicles = s.nextInt();
		ret.vehicleSpeed = s.nextDouble();
		ret.vehicleCapacity = s.nextDouble();
		numberOfClients = s.nextInt();
		ret.d = new Depot[]{new Depot(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble())};
		ret.clients = new Client[numberOfClients];
		
		for ( int i=0; i<numberOfClients; i++ )
			ret.clients[i] = new Client(s.nextDouble(), s.nextDouble(), s.nextDouble(),
										s.nextDouble(), s.nextDouble());
		
		s.close();
		return ret;
	}
	
	/* EMBEDDED CLASSES */
	private class ProblemData
	{
		public int numberOfVehicles;
		public double vehicleSpeed;
		public double vehicleCapacity;
		public Depot[] d;
		public Client[] clients;
	}
}
