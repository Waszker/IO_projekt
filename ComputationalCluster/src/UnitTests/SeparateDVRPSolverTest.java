package UnitTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import Problems.DVRPProblem.Client;
import Problems.DVRPProblem.Depot;
import Problems.DVRPProblem.Graph;
import Problems.DVRPProblem.SeparateDVRPSolver;
import Problems.DVRPProblem.SeparateDVRPSolver.PathNode;

public class SeparateDVRPSolverTest
{
	@Test
	public void graphSolvingTest() throws IOException
	{
		Depot d = new Depot(0, 1, 0, 560);
		Client[] c = new Client[] { new Client(1, 1, 0, 1, 20),
				new Client(2, 1, 0, 1, 20), new Client(3, 1, 0, 1, 20),
				new Client(4, 1, 0, 1, 20), new Client(5, 1, 0, 1, 20),
				new Client(6, 1, 0, 1, 20), new Client(7, 1, 0, 1, 20),
				new Client(8, 1, 0, 1, 20) };

		Graph given = new Graph(d, c);
		given.initialize(BigInteger.ZERO, null, 1);

		Graph[] gset;
		double finalTime;
		double minTime = Double.POSITIVE_INFINITY;
		List<PathNode[]> pathsForAllVehicles = new LinkedList<PathNode[]>();
		List<PathNode[]> minPaths = new LinkedList<PathNode[]>();

		while ((gset = given.nextPartitioning()) != null)
		{
			pathsForAllVehicles.clear();
			finalTime = SeparateDVRPSolver.solveDVRPOnGraphSet(gset, 100.0,
					pathsForAllVehicles);
			if (finalTime < minTime)
			{
				minPaths.clear();
				minTime = finalTime;
				minPaths.addAll(pathsForAllVehicles);
			}
		}

		assertEquals(22, minTime, 0.01);

		for (int i = 0; i < minPaths.size(); ++i)
		{
			PathNode[] pn = minPaths.get(i);
			if (pn.length == 0) continue;
			System.out.println("Path for " + (i + 1) + " vehicle: ");
			int j = 0;
			for (j = 0; j < pn.length - 1; ++j)
				System.out.print("(" + pn[j].x + ", " + pn[j].y
						+ "):arrivalTime=" + pn[j].arriveTime + " -> ");
			System.out.println("(" + pn[j].x + ", " + pn[j].y
					+ "):arrivalTime=" + pn[j].arriveTime);
		}

	}

}
