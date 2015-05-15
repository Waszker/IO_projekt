package Problems;

import java.util.Locale;
import java.util.Scanner;

import Problems.DVRPProblem.Client;
import Problems.DVRPProblem.Depot;
import Problems.DVRPProblem.Graph;
import Problems.DVRPProblem.IGraphNode;
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
		
		if ( numOfVariations >= numOfNodes )
		{			
			double variationPerNode = (double)numOfVariations / (double) numOfNodes;
			for ( int i=0; i<numOfNodes; i++ )
			{
				int from = (int)(i*variationPerNode);
				int to = (int)((i+1)*variationPerNode); // vartiations to handle by one node: [from, to)
				
				ret[i] = ("S " + from + " " + to).getBytes(); //S means that there is a set of whole TSP problems
															  //to solve (maybe single element set)
			}
		}
		
		else // this occures practically only when there is one vehicle
		{
			double nodesPerVariation = ((double) numOfNodes      / (double) numOfVariations);
			double variationsPerNode = ((double) numOfVariations / (double) numOfNodes);
			
			for ( int i=0; i<numOfNodes; i++ )
			{
				final int currentVariation =  (int)(i*variationsPerNode);
				final int currentNumOfNodes = (int)((currentVariation+2)*nodesPerVariation) - (int)((currentVariation+1)*nodesPerVariation);
				final int startIndex = i;
				
				
				for ( ; i<startIndex + currentNumOfNodes; i++ )
					ret[i] = generateSubproblemString(i - startIndex, currentNumOfNodes, noc, currentVariation).getBytes();
				i--;
			}
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
		
		for ( int i=0; i<partialSolutions.length; i++ )
		{
			String _s = new String(partialSolutions[i]);
			Scanner s = new Scanner(_s);
			s.useLocale(Locale.ENGLISH);
			
			if ( Character.toUpperCase(s.next().charAt(0)) == 'S' )
			{
				double cost = s.nextDouble();
				if ( cost < lowestCost )
				{
					lowestCost = cost;
					//int len = s.nextInt();
					
				}
			}
			
			else
			{
				//TODO: Handle one TSP divided among several nodes.
			}
			
			s.close();
		}
		
		return lowestCost.toString().getBytes();
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
		
		if ( Character.toUpperCase(s.next().charAt(0)) == 'S' )
		{
			final int from = s.nextInt();
			final int to = s.nextInt();
			Double lowerCost = Double.MAX_VALUE;
			//IGraphNode[] path = null;
			for ( int i=from; i<to; i++ )
			{
				double cost = SeparateDVRPSolver.solveDVRPOnGraphSet(g.divideGraph(pd.numberOfVehicles, i), pd.vehicleCapacity);
				if ( cost < lowerCost )
				{
					lowerCost = cost;
					//path = SeparateDVRPSolver.path;
				}
			}
			
			ret = "S " + lowerCost.toString();
			
			/*
			if ( path == null )ret+=" 0";
			else
			{
				ret+= " " + path.length;
				for ( int j=0; j<path.length; j++ )
					ret+=" "+path[j].getX()+" "+path[j].getY();
			}*/
		}
		
		else
		{
			//TODO: Handle one TSP divided among several nodes.
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
	
	private double numOfDifferentPaths(int numOfNodes)
	{
		int ret = 1;
		while ( ret < numOfNodes )
			ret*= 2;
		return ret;
	}
	
	private int pathLen(int numOfNodes)
	{
		int v = 1, len = 0;
		while ( v < numOfNodes )
		{
			v*= 2;
			len++;
		}
		return len;
	}
	
	private String generateSubproblemString(final int nodeNum, final int numberOfNodes, final int numberOfClients, final int currentVariation)
	{
		final double currentPathsPerNode = numOfDifferentPaths(numberOfNodes) / numberOfNodes;
		final int pathsForThisNode = (int)((nodeNum+2)*currentPathsPerNode) - (int)((nodeNum+1)*currentPathsPerNode);
		final int pathLenForThisNode = pathLen(numberOfNodes);
		String ret = "P " + currentVariation + " " + pathLenForThisNode + " " + pathsForThisNode + "\n";
		
		for ( int i=0; i<pathsForThisNode; i++ )
			ret += beginingPath(numberOfClients, pathLenForThisNode, i) + ( i<pathsForThisNode-1 ? "\n" : "" );
		
		return ret;
	}
	
	
	private String beginingPath(final int numberOfClients, final int pathLen, final int pathNum)
	{
		
		return "";
	}
	
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