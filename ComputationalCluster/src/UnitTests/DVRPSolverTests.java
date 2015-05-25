package UnitTests;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Test;

import Problems.DVRPProblem.Client;
import Problems.DVRPProblem.Depot;
import Problems.DVRPProblem.Graph;

public class DVRPSolverTests
{
	@Test
	public void graphPartitionigTest() throws IOException
	{
		/*
		String inputMessage = "2 1.0 1.0 2" + "\n" +
							  "0.0 0.0 0.0 1000000.0" + "\n" + 
							  "0.0 1.0 0.0 10.0 1.0"+ "\n" +
							  "0.0 2.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 0.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 1.0 0.0 10.0 1.0"+ "\n" +
							  "1.0 2.0 0.0 10.0 1.0"+ "\n";
							  
		byte[] problemData = inputMessage.getBytes();
		DVRPSolver solver = new DVRPSolver(problemData); 
		
		byte[][] res = solver.DivideProblem(10);
		
		//for ( int i=0; i<res.length; i++ )
		//	res[i] = solver.Solve(res[i], 0);
		
		//byte[] finalResult = solver.MergeSolution(res);
		//System.out.println(new String(finalResult));*/
		
		Depot[] d = new Depot[] { new Depot(0, 0, 5, 20) };
		Client[] c = new Client[]
				{
					new Client(1, 1, 0, 0, 5),
					new Client(2, 1, 0, 0, 5),
					new Client(3, 1, 0, 0, 5),
					new Client(4, 1, 0, 0, 5),
					new Client(5, 1, 0, 0, 5),
					new Client(6, 1, 0, 0, 5),
					new Client(7, 1, 0, 0, 5),
					new Client(8, 1, 0, 0, 5),
					new Client(9, 1, 0, 0, 5),
					new Client(1, 2, 0, 0, 5),
					new Client(1, 3, 0, 0, 5),
					new Client(1, 4, 0, 0, 5)
				};
		
		Graph given = new Graph(d, c, 1.0);
		
		int i = 0;
		Graph[] gset;
		do
		{
			gset = given.divideGraph(3, BigInteger.valueOf(i++));
		}while ( gset != null );
	}

}
