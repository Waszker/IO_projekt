package UnitTests;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Test;

import Problems.DVRPSolver;
import Problems.DVRPProblem.Client;
import Problems.DVRPProblem.Depot;
import Problems.DVRPProblem.Graph;
import Problems.DVRPProblem.IGraphNode;

public class DVRPSolverTests
{
	@Test
	public void graphPartitionigTest() throws IOException
	{/*
		String inputMessage = "2 1.0 1.0 2" + "\n" +
							  "0.0 0.0 0.0 1000000.0" + "\n" + 
							  "0.0 1.0 0.0 10.0 1.0"+ "\n" +
							  "0.0 2.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 0.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 1.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 2.0 0.0 10.0 1.0"+ "\n";
							  
		byte[] problemData = inputMessage.getBytes();
		DVRPSolver solver = new DVRPSolver(problemData); 
		
		byte[][] res = solver.DivideProblem(1);
		
		for ( byte[] b : res )
			System.out.println( new String(b) );
		
		for ( int i=0; i<res.length; i++ )
			res[i] = solver.Solve(res[i], 0);
		
		byte[] finalResult = solver.MergeSolution(res);
		System.out.println(new String(finalResult));*/
		
		final int NOV = 3;
		Depot d = new Depot(0, 0, 0, 10);
		Client[] c = new Client[]
				{
					new Client(0, 1, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(0, 2, 0, 0, 0),
					new Client(1, 0, 0, 0, 0)
				};
		
		Graph g = new Graph(d, c);
		g.initialize(BigInteger.ZERO, null, NOV);
		
		System.out.println( "Clients: " + c.length + ", Vehicles: " + NOV );
		
		int i=0;
		while ( g.nextPartitioning()!=null )
			i++;
		System.out.println(i);
		System.out.println("(old: "+BigInteger.valueOf(NOV).pow(c.length) + ")");
	}

}
