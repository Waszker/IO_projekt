package Problems.DVRPProblem;

import java.math.BigInteger;

/**
 * <p>Class providing graph building from given list of clients, depots and vehicle speed.
 * 	  It also provides graph dividing depending on partitioning number</p>
 * @author Filip
 */
public class Graph
{
	/* VARIABLES */
	
	public Double[][] e; //adjency matrix; weight = travelCost
	public IGraphNode[] v;
	
	//constructs graph, where nodes are in order: { d c1 ... ck } , d=depot, ci=client
	public Graph(Depot d, Client[] c)
	{
		final int n = c.length + 1;
		v = new IGraphNode[n];
		
		v[0] = d;
		for ( int i = 0; i<c.length; i++ )
			v[i+1] = c[i];
		
		e = new Double[n][n];
		for ( int i=0; i<n; i++ )
			for ( int j=i; j<n; j++ )
				e[i][j] = e[j][i] = calcTravelTime(v[i], v[j]); 
	}
	
	/**
	 * <p>
	 * Divides calling graph into numOfVehicles parts. Every part contains all depots
	 * and some part of clients. 
	 * </p>
	 * @param numOfVehicles Number of result subgraphs
	 * @param partitioningNumber Number of set partitioning (from 0 to pow(numberOfVehicles, numberOfClients))
	 * @return Array of result graphs or null if partitioningNumber is wrong
	 * @throws IllegalArgumentException When numOfVehicles is not a positive number
	 */
	public Graph[] divideGraph(int numOfVehicles, BigInteger partitioningNumber)
	{
		if ( numOfVehicles < 1 )throw new IllegalArgumentException();
		
		final int n = v.length - 1;
		int[] p = partitioning(partitioningNumber,numOfVehicles,n);
		if ( p == null )return null;
		int[] cnt = count(p,numOfVehicles);
		
		Graph[] ret = new Graph[numOfVehicles];
		
		for ( int i=0; i<numOfVehicles; i++ )
		{
			int k=0;
			Client[] cl = new Client[cnt[i]];
			for ( int j=0; j<p.length; j++ )
				if ( p[j] == i )
					cl[k++] = (Client)v[j+1];
			ret[i] = new Graph( (Depot)v[0], cl );
		}
		
		return ret;
	}
	
	
	/* PRIVATE AUXILIARY FUNCTIONS */
	
	// constructs any variation of ints
	private int[] partitioning(BigInteger m, final int nov, final int n)
	{
		if ( m.compareTo( BigInteger.valueOf(nov).pow(n) ) >= 0 )
			return null;
		
		int[] div = new int[n];
		for ( int i=0; i<n; i++ )
		{
			div[n-i-1] = m.mod(BigInteger.valueOf(nov)).intValue();
			m = m.divide(BigInteger.valueOf(nov));
		}
		
		return div;
	}
	
	// counts clients assigned to each vehicle
	private int[] count(int[] p, final int nov)
	{
		int[] c = new int[nov];
		for ( int i=0; i<nov; i++ )
		{
			c[i] = 0;
			for ( int j=0; j<p.length; j++ )
				if ( p[j] == i )
					c[i]++;
		}
		return c;
	}
	
	// calculates travel time from node g1 to node g2
	private double calcTravelTime(IGraphNode g1, IGraphNode g2)
	{		
		double dx = g1.getX()-g2.getX();
		double dy = g1.getY()-g2.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}
}
