package Problems.DVRPProblem;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Class providing finding minimal cost of DVRP problem where every
 * vehicle is assigned separate set of clients.
 * </p>
 * @author Filip
 */
public class SeparateDVRPSolver
{
	/* VARIABLES */
	private static double currentBest; //current best solution of oneDvrp function
	
	
	/* PRIVATE AUXILIARY FUNCTIONS */
	
	// checks if move to 'to' node is legal
	private static boolean moveLegal(IGraphNode to, double currentCargo, double arriveTime)
	{
		if ( to instanceof Depot )
		{
			Depot d = (Depot)to;
			if ( arriveTime < d.startTime || arriveTime > d.endTime )return false;
			return true;
		}
		
		if ( to instanceof Client )
		{
			Client c = (Client)to;
			if ( arriveTime < c.time )return false;
			if ( currentCargo < c.size )return false;
			return true;
		}
		
		return false;
	}
	
	// checks if we can wait for the client (wait and go to him without going to depot)
	private static boolean canWait(Client to, double currentCargo)
	{
		if ( currentCargo < to.size )return false;
		return true;
	}
	
	// checks if all booleans in array are 'true'
	private static boolean allVisited(boolean[] cv)
	{
		for ( int i=0; i<cv.length; i++ )
			if ( !cv[i] )
				return false;
		return true;
	}
	
	// main recursive function
	private static void recursive(Graph g, int thisIndex, List<IGraphNode> currentPath, boolean[] clientVisited, double cap, double currentCargo, double curTime, double curCost)
	{
		if ( allVisited(clientVisited) )
		{
			int next = -1;
			double cost = Double.MAX_VALUE;
			
			for ( int i=0; i<g.d.length; i++ )
			{
				double travelTime = g.e[thisIndex][i];
				if ( !moveLegal(g.d[i], currentCargo, curTime+travelTime) )continue;
				if ( travelTime < cost )
				{
					cost = g.e[thisIndex][i];
					next = i;
				}
			}
			
			if ( next > -1 ) //można wrócić do Depot
			{			
				curCost+=cost;
				if ( curCost < currentBest )
					currentBest = curCost;
			}
			return;
		}
		
		for ( int i=0; i<g.c.length; i++ ) //TODO: Make one depot available.
		{
			if ( thisIndex == i+g.d.length )continue;
			if ( clientVisited[i] )continue;
			
			double travelTime = g.e[thisIndex][g.d.length+i];
			if ( !canWait(g.c[i], currentCargo) )continue;
			
			double nextTime = curTime+travelTime+g.c[i].unld;
			if ( !moveLegal(g.c[i], currentCargo, curTime+travelTime) )
				nextTime = g.c[i].time + g.c[i].unld; //we're waiting
			
			currentPath.add(g.c[i]);
			clientVisited[i] = true;
			recursive(g, g.d.length+i, currentPath, clientVisited, cap, currentCargo-g.c[i].size, nextTime, curCost+travelTime);
			currentPath.remove(currentPath.size()-1);
			clientVisited[i] = false;
		}
		
		if ( thisIndex >= g.d.length ) //jesteśmy u klienta (nie ma sensu jechać z Depot do Depot)
		{
			for ( int i=0; i<g.d.length; i++ )
			{
				if ( thisIndex == i )continue;
				
				double travelTime = g.e[thisIndex][i];
				if ( !moveLegal(g.c[i], cap, curTime+travelTime) )continue;
				currentPath.add(g.d[i]);
				recursive(g, i, currentPath, clientVisited, cap, cap, curTime+travelTime, curCost+travelTime);
				currentPath.remove(currentPath.size()-1);
			}
		}
	}
	
	// solves DVRP on one graph
	private static double oneDvrp(Graph g, double cap)
	{
		currentBest = Double.POSITIVE_INFINITY;
		for ( int i=0; i<g.d.length; i++ )
		{
			List<IGraphNode> path = new LinkedList<>();
			boolean[] clientVisited = new boolean[g.c.length];
			for ( int j=0; j<g.c.length; j++ )
				clientVisited[j] = false;
			path.add(g.d[i]);
			recursive(g, i, path, clientVisited,cap,cap,g.d[i].startTime, 0);
		}
		return currentBest;
	}
	
	
	/* PUBLIC METHODS */
	
	/**
	 * <p>Solves and sumes DVRP cost on each graph in the set. </p>
	 * @param g Graph set (given by array).
	 * @param cap Single vehicle capacity.
	 * @return
	 */
	public static double solveDVRPOnGraphSet(Graph[] g, double cap)
	{
		double ret = 0;
		for ( int i=0; i<g.length; i++ )
			ret += oneDvrp(g[i], cap);
		return ret;
	}
}
