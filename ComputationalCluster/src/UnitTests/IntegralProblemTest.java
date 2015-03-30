package UnitTests;

import org.junit.Test;

import XMLMessages.SolvePartialProblems;
import GenericCommonClasses.ProblemHelper;
import pl.edu.pw.mini.se2.TaskSolver;

public class IntegralProblemTest
{

	@Test
	public void test()
	{
		final String input = "0 1 100000";
		final int nnodes = 1;
		final long timeout = 10000;
		
		byte[] commondata = input.getBytes();
		System.out.print("commondata = ");
		for ( int j=0; j<commondata.length; j++ )
			System.out.print(commondata[j] + ", ");
		System.out.println();
		
		SolvePartialProblems sppm = new SolvePartialProblems();
		sppm.setCommonData(input.getBytes());
		sppm.setProblemType("IntegralProblem");
		
		TaskSolver its = ProblemHelper.instantinateTaskSolver(sppm);
		byte[][] r = its.DivideProblem(nnodes);
		for ( int i=0; i<r.length; i++ )
		{
			System.out.println(r[i].length);
			System.out.print("r["+i+"] = ");
			for ( int j=0; j<r[i].length; j++ )
				System.out.print((byte)r[i][j] + ", ");
			System.out.println();
			r[i] = its.Solve(r[i], timeout);
		}
			
		byte [] result = its.MergeSolution(r);
		
		System.out.println("Result = " + new String(result));
	}

}
