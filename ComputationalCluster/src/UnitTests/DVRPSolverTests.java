package UnitTests;

import java.io.IOException;

import org.junit.Test;

import Problems.DVRPSolver;

public class DVRPSolverTests
{
	@Test
	public void graphPartitionigTest() throws IOException
	{
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
		
		for ( int i=0; i<res.length; i++ )
			res[i] = solver.Solve(res[i], 0);
		
		byte[] finalResult = solver.MergeSolution(res);
		System.out.println(new String(finalResult));
	}

}
