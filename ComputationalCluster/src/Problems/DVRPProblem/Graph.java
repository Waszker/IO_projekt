package Problems.DVRPProblem;

import java.util.Arrays;
import java.util.List;

public class Graph
{
	/* VARIABLES */
	
	public Double[][] e; //adjency matrix; weight = travelTime
	public IGraphNode[] v;
	public Depot[] d;
	public Client[] c;
	private double vehicleSpeed;
	
	//constructs graph, where nodes are in order: { d0 d1 d2 ... dm c1 ... ck } , d=depot, c=client
	public Graph(Depot[] d, Client[] c, double vehicleSpeed)
	{
		final int n = c.length + d.length;
		v = new IGraphNode[n];
		this.d = d;
		this.c = c;
		this.vehicleSpeed = vehicleSpeed;
		
		//we assume graph nodes order: d1 d2 d3 ... dm c1 c2 ... ck , m+k=n
		for ( int i = 0; i<d.length; i++ )
			v[i] = d[i];
		for ( int i = 0; i<c.length; i++ )
			v[d.length + i] = c[i];
		
		e = new Double[n][n];
		for ( int i=0; i<n; i++ )
			for ( int j=i; j<n; j++ )
				e[i][j] = e[j][i] = calcTravelTime(v[i], v[j], vehicleSpeed); 
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
	public Graph[] divideGraph(int numOfVehicles, int partitioningNumber)
	{
		if ( numOfVehicles < 1 )throw new IllegalArgumentException();
		
		final int n = c.length;
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
					cl[k++] = c[j];
			ret[i] = new Graph(d, cl, vehicleSpeed);
		}
		
		return ret;
	}
	
	
	/* PRIVATE AUXILIARY FUNCTIONS */
	
	// constructs any variation of ints
	private int[] partitioning(final int m, final int nov, final int n)
	{
		if ( m >= Math.round(Math.pow(nov, n)) )
			return null;
		
		int d = 1;
		int dd = nov;
		int[] div = new int[n];
		for ( int i=0; i<n; i++ )
		{
			div[n-i-1] = m%dd/d;
			d = dd;
			dd*=nov;
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
	private double calcTravelTime(IGraphNode g1, IGraphNode g2, double vehicleSpeed)
	{		
		double dx = g1.getX()-g2.getX();
		double dy = g1.getY()-g2.getY();
		//return Math.sqrt(dx*dx + dy*dy) / vehicleSpeed;
		return Math.sqrt(dx*dx + dy*dy);
	}
}
