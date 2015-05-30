package Problems.DVRPProblem;

import java.math.BigInteger;

/**
 * <p>Class providing graph building from given list of clients, depots and vehicle speed.
 * 	  It also provides graph dividing depending on partitioning number</p>
 * @author Filip
 */
public class Graph
{
	/* PUBLIC STATIC METHODS */
	/**
	 * <p>Returns number of combinations in graph partitioning</p>
	 */
	public static BigInteger calcNumOfPartitionings(int numOfClients, int numOfVehicles)
	{
		BigInteger numberOfPartitionings;
		BigInteger novFactorial = BigInteger.ONE;
		for ( int i=2; i<=numOfVehicles; i++ )
			novFactorial = novFactorial.multiply( BigInteger.valueOf(i) );
		numberOfPartitionings = BigInteger.valueOf(numOfVehicles).pow(numOfClients - numOfVehicles).multiply(novFactorial);
		return numberOfPartitionings;
	}
	
	/* PUBLIC VARIABLES */
	
	public Double[][] e; //adjency matrix; weight = travelCost
	public IGraphNode[] v;
	
	/* PRIVATE VARIABLES */
	
	BigInteger numOfPartitionings, from, to, partitioningNumber = null;
	int numOfVehicles, numOfClients;
	
	/* PUBLIC METHODS */
	
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
		
		numOfClients = c.length;
	}
	
	
	/**
	 * <p>Initialzes graph before partitioning.</p>
	 * @param from lower bound of the set of partitioning numbers
	 * @param to higher bound of the set of partitioning numbers; if null its set to max (numOfPatitionings)
	 */
	public void initialize(BigInteger from, BigInteger to, int numOfVehicles)
	{
		this.from = from;
		this.numOfVehicles = Math.min(numOfVehicles, v.length-1);
		this.partitioningNumber = from;
		this.numOfPartitionings = calcNumOfPartitionings(numOfVehicles);
		
		if ( to != null )this.to = to;
		else this.to = this.numOfPartitionings;
	}
	
	
	
	/**
	 * <p>
	 * Divides calling graph into numOfVehicles parts. Every part contains all depots
	 * and some part of clients. 
	 * </p>
	 * @return Array of result graphs or null if partitioningNumber is wrong
	 * @throws IllegalStateException when called uninitialized
	 */
	public Graph[] nextPartitioning()
	{
		if ( partitioningNumber == null )throw new IllegalStateException();
		
		int[] p = partitioning();
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
	
	/**
	 * <p>Returns number of combinations in graph partitioning</p>
	 */
	public BigInteger calcNumOfPartitionings(int numOfVehicles)
	{
		BigInteger numberOfPartitionings;
		BigInteger novFactorial = BigInteger.ONE;
		for ( int i=2; i<=numOfVehicles; i++ )
			novFactorial = novFactorial.multiply( BigInteger.valueOf(i) );
		numberOfPartitionings = BigInteger.valueOf(numOfVehicles).pow(numOfClients - numOfVehicles).multiply(novFactorial);
		return numberOfPartitionings;
	}
	
	
	/* PRIVATE AUXILIARY FUNCTIONS */
	
	// constructs any variation of ints
	private int[] partitioning()
	{
		int currentMax;
		int[] div = new int[numOfClients];
		BigInteger m;
		boolean repeat;
		
		do
		{
			if ( partitioningNumber.compareTo( to ) >= 0 || 
				 partitioningNumber.compareTo( numOfPartitionings ) >= 0 )
				 	return null;
			
			m = partitioningNumber;
			repeat = false;
			currentMax = 0;
			
			for ( int i=0; i<numOfClients; i++ )
			{
				int divider = Math.min(i+1, numOfVehicles);
				int nextValue = m.mod(BigInteger.valueOf(divider)).intValue();
				
				if ( nextValue > currentMax+1 )
				{
					int digitsToSkip = nextValue - div[nextValue] - 1;
					BigInteger numberToAdd = BigInteger.valueOf(digitsToSkip);
					for ( int j=1; j<nextValue; j++ )
						numberToAdd = numberToAdd.multiply(BigInteger.valueOf(j));
					
					partitioningNumber = partitioningNumber.add(numberToAdd);
					repeat = true;
					break;
				}
				
				if ( nextValue > currentMax )
					currentMax = nextValue;
				
				div[i] = nextValue;
				m = m.divide(BigInteger.valueOf(divider));
			}
		}while ( repeat );
		
		/*
		for ( int i : div )
			System.out.print(i + " ");
		System.out.println();*/
		
		partitioningNumber = partitioningNumber.add(BigInteger.ONE);
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
