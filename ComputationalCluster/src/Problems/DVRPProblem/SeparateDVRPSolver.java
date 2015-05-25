package Problems.DVRPProblem;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Class providing finding minimal cost of DVRP problem where every
 * vehicle is assigned separate set of clients.
 * Returns also a path for every vehicle.
 * </p>
 * @author Filip
 */
public class SeparateDVRPSolver
{
	
	/* VARIABLES */
	private static double currentBest; //current best solution of oneDvrp function
	public static List<PathNode> path = new LinkedList<PathNode>();
	
	
	
	
	/* NESTED CLASSES */
	public static class PathNode
	{
		public double x,y, arriveTime;
		public PathNode(double x, double y, double t)
		{
			this.x = x;
			this.y = y;
			this.arriveTime = t;
		}
	}
	
	
	
	
	
	/* PRIVATE AUXILIARY FUNCTIONS */
	
	// solves DVRP on one graph
	private static double oneDvrp(Graph g, double cap)
	{
		currentBest = Double.POSITIVE_INFINITY;
		path.clear();
		
		//TODO here do recursive search; set currentBest=bestCost, path=bestPath
		
		return currentBest;
	}
	
	
	
	
	
	/* PUBLIC METHODS */
	
	/**
	 * <p>Solves and sumes DVRP cost on each graph in the set. </p>
	 * @param g Graph set (given by array).
	 * @param cap Single vehicle capacity.
	 * @return
	 */
	public static double solveDVRPOnGraphSet(Graph[] g, double cap, List<PathNode[]> pathsForAllVehicles)
	{
		double ret = 0; 
		for ( int i=0; i<g.length; i++ )
		{
			ret += oneDvrp(g[i], cap);
			pathsForAllVehicles.add(SeparateDVRPSolver.path.toArray(new PathNode[path.size()])); 
		}
		return ret;
	}
}
