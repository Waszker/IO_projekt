package Problems.DVRPProblem;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Class providing finding minimal cost of DVRP problem where every vehicle is
 * assigned separate set of clients. Returns also a path for every vehicle.
 * </p>
 * 
 * @author Filip
 */
public class SeparateDVRPSolver
{

	/* VARIABLES */
	private static double currentBest; // current best solution of oneDvrp
										// function
	public static List<PathNode> path = new LinkedList<PathNode>();
	private static double maxCapacity;
	private static int graphSize;

	/* NESTED CLASSES */
	public static class PathNode
	{
		public double x, y, arriveTime;

		public PathNode(double x, double y, double t)
		{
			this.x = x;
			this.y = y;
			this.arriveTime = t;
		}
	}

	/* PRIVATE AUXILIARY FUNCTIONS */

	private static boolean CheckIfToEarlyClosedCycle(
			boolean[][] actualChoosenEdges, int beginning)
	{
		int count = 0;
		int colIndex;
		int rowIndex = beginning;
		boolean found = false;
		boolean cycle = false;
		for (int i = 0; i < graphSize; ++i)
		{
			colIndex = rowIndex;
			for (int j = 0; j < graphSize; ++j)
			{
				if (actualChoosenEdges[colIndex][j])
				{
					rowIndex = j;
					++count;
					found = true;
					break;
				}
			}
			if (rowIndex == beginning)
			{
				cycle = true;
				break;
			}
		}

		if (found && cycle && count < graphSize - 1) return true;
		return false;
	}

	private static boolean CheckIfClosedCycle(boolean[][] actualChosenEdges)
	{
		int colindex = -1;
		int rowindex = -1;
		// Choose any beginning
		for (int i = 0; i < graphSize; i++)
		{
			for (int j = 0; j < graphSize; j++)
			{
				if (actualChosenEdges[i][j])
				{
					colindex = i;
					rowindex = j;
					break;
				}
			}
			if (colindex != -1) break;
		}
		int[] vertices = new int[graphSize + 1];
		vertices[0] = colindex;
		vertices[1] = rowindex;
		vertices[graphSize] = -1;
		for (int i = 0; i < graphSize - 1; i++)
		{
			colindex = rowindex;
			for (int j = 0; j < graphSize; j++)
			{
				if (actualChosenEdges[colindex][j])
				{
					rowindex = j;
					break;
				}
			}
			vertices[i + 2] = rowindex;
			if (vertices[0] == rowindex) break;
		}
		if (vertices[0] == vertices[graphSize]) return true;
		return false;
	}

	private static double addToPath(Graph g, int[] vertices)
	{
		path.clear();
		Client client;
		double cap = maxCapacity;
		double timeOfTravel = 0;

		path.add(new PathNode(g.v[0].getX(), g.v[0].getY(), 0));
		int i;
		for (i = 0; i < graphSize - 1; i++)
		{
			client = (Client) g.v[vertices[i + 1]];
			if (cap < client.size)
			{
				timeOfTravel += g.e[vertices[i]][0];
				timeOfTravel += g.e[0][vertices[i]];
				cap = maxCapacity;
			}
			if (client.time > timeOfTravel) timeOfTravel = client.time;

			timeOfTravel += g.e[vertices[i]][vertices[i + 1]];
			timeOfTravel += client.unld;
			cap -= client.size;

			path.add(new PathNode(g.v[vertices[i + 1]].getX(),
					g.v[vertices[i + 1]].getY(), timeOfTravel));
		}
		timeOfTravel += g.e[vertices[i]][0];
		path.add(new PathNode(g.v[0].getX(), g.v[0].getY(), timeOfTravel));

		return timeOfTravel;
	}

	private static double printCycle(Graph g, boolean[][] actualChosenEdges)
	{
		int colindex = -1;
		int rowindex = -1;

		// Choose beginning in depot
		for (int i = 0; i < graphSize; i++)
		{
			if (actualChosenEdges[0][i])
			{
				colindex = 0;
				rowindex = i;
				break;
			}
		}
		int[] vertices = new int[graphSize + 1];
		vertices[0] = colindex;
		vertices[1] = rowindex;
		for (int i = 0; i < graphSize - 1; i++)
		{
			colindex = rowindex;
			for (int j = 0; j < graphSize; j++)
			{
				if (actualChosenEdges[colindex][j])
				{
					rowindex = j;
					break;
				}
			}
			vertices[i + 2] = rowindex;
		}

		return addToPath(g, vertices);
	}

	private static void TSP(Graph g, double[][] table, double d,
			boolean[] activeCol, boolean[] activeRow,
			boolean[][] actualChosenEdges)
	{

		double oszac = 0;
		int index_col = -1;
		int index_row = -1;
		// going through rows
		for (int w = 0; w < graphSize; w++)
		{
			if (!activeRow[w]) continue;
			double min = Double.MAX_VALUE;
			for (int k = 0; k < graphSize; k++)
			{
				if (!activeCol[k]) continue;
				if (table[k][w] < min) min = table[k][w];
			}
			oszac = oszac + min;
			for (int k = 0; k < graphSize; k++)
			{
				if (!activeCol[k]) continue;
				table[k][w] = table[k][w] - min;
			}
		}
		// going through columns
		for (int k = 0; k < graphSize; k++)
		{
			if (!activeCol[k]) continue;
			double min = Double.MAX_VALUE;
			for (int w = 0; w < graphSize; w++)
			{
				if (!activeRow[w]) continue;
				if (table[k][w] < min) min = table[k][w];
			}
			oszac = oszac + min;
			for (int w = 0; w < graphSize; w++)
			{
				if (!activeRow[w]) continue;
				table[k][w] = table[k][w] - min;
			}
		}

		d = d + oszac;
		if (d >= currentBest) return;

		int colCount = 0;
		int rowCount = 0;
		int[][] last = new int[2][2];
		for (int i = 0; i < graphSize; i++)
		{
			if (activeCol[i])
			{
				last[colCount % 2][1] = i;
				colCount++;
			}
			if (activeRow[i])
			{
				last[rowCount % 2][0] = i;
				rowCount++;
			}
			if (colCount > 2 || rowCount > 2) break;
		}

		// Close cycle and update currentBest
		if (colCount == 2 && rowCount == 2)
		{
			boolean do_work = true;
			for (int i = 0; i < 2; i++)
			{
				if (!do_work) break;
				if (last[0][1] == last[i][0]) continue;
				for (int j = 0; j < 2; j++)
				{

					if (last[1][1] == last[j][0]) continue;
					actualChosenEdges[last[0][1]][last[i][0]] = true;
					actualChosenEdges[last[1][1]][last[j][0]] = true;

					if (CheckIfClosedCycle(actualChosenEdges))
					{
						do_work = false;
						break;
					}
					else
					{
						actualChosenEdges[last[0][1]][last[i][0]] = false;
						actualChosenEdges[last[1][1]][last[j][0]] = false;
					}
				}
			}

			if (!do_work)
			{
				d = printCycle(g, actualChosenEdges);
				if (d < currentBest) currentBest = d;
			}
			return;
		}
		// Choose dividing edge
		double max = Double.MIN_VALUE;
		for (int k = 0; k < graphSize; k++)
		{
			if (!activeCol[k]) continue;
			for (int w = 0; w < graphSize; w++)
			{
				if (!activeRow[w]) continue;
				if (table[k][w] == 0)
				{
					double min_r = Double.MAX_VALUE;
					double min_c = Double.MAX_VALUE;

					// For column k
					for (int r = 0; r < graphSize; r++)
					{
						if (!activeRow[r]) continue;
						if (r != w)
							if (table[k][r] < min_c) min_c = table[k][r];
					}
					// For row w
					for (int c = 0; c < graphSize; c++)
					{
						if (!activeCol[c]) continue;
						if (c != k)
							if (table[c][w] < min_r) min_r = table[c][w];
					}

					if ((min_c + min_r) > max)
					{
						actualChosenEdges[k][w] = true;

						if (!CheckIfToEarlyClosedCycle(actualChosenEdges, k))
						{
							max = min_c + min_r;
							index_col = k;
							index_row = w;
						}
						actualChosenEdges[k][w] = false;
					}
				}
			}
		}
		if (max == Double.MIN_VALUE) // Impossible to choose dividing edge
			return;

		double[][] TL = new double[graphSize][graphSize];
		for (int i = 0; i < graphSize; i++)
			for (int j = 0; j < graphSize; j++)
				TL[i][j] = table[i][j];

		actualChosenEdges[index_col][index_row] = true;
		activeRow[index_row] = false;
		activeCol[index_col] = false;
		TL[index_row][index_col] = Double.POSITIVE_INFINITY; // Opposite edge

		TSP(g, TL, d, activeCol, activeRow, actualChosenEdges); // Can
																// improve
																// currentBest

		if (d >= currentBest) return;
		actualChosenEdges[index_col][index_row] = false;
		activeRow[index_row] = true;
		activeCol[index_col] = true;
		table[index_col][index_row] = Double.POSITIVE_INFINITY;
		TSP(g, table, d, activeCol, activeRow, actualChosenEdges);
	}

	// solves DVRP on one graph
	private static double oneDvrp(Graph g, double cap)
	{
		currentBest = Double.POSITIVE_INFINITY;
		path.clear();
		graphSize = g.v.length;
		maxCapacity = cap;

		boolean[] activeColumns = new boolean[graphSize];
		boolean[] activeRows = new boolean[graphSize];
		boolean[][] chosenEdges = new boolean[graphSize][graphSize];
		double[][] table = new double[graphSize][graphSize];

		for (int i = 0; i < graphSize; ++i)
		{
			activeColumns[i] = true;
			activeRows[i] = true;
			for (int j = 0; j < graphSize; ++j)
			{
				chosenEdges[i][j] = false;
				table[i][j] = g.e[i][j].doubleValue();
			}
			table[i][i] = Double.POSITIVE_INFINITY;
		}

		TSP(g, table, 0, activeColumns, activeRows, chosenEdges);
		return currentBest;
	}

	/* PUBLIC METHODS */

	/**
	 * <p>
	 * Solves and sums DVRP cost on each graph in the set.
	 * </p>
	 * 
	 * @param g
	 *            Graph set (given by array).
	 * @param cap
	 *            Single vehicle capacity.
	 * @return
	 */
	public static double solveDVRPOnGraphSet(Graph[] g, double cap,
			List<PathNode[]> pathsForAllVehicles)
	{
		double ret = 0;
		for (int i = 0; i < g.length; i++)
		{
			ret += oneDvrp(g[i], cap);
			pathsForAllVehicles.add(SeparateDVRPSolver.path
					.toArray(new PathNode[path.size()]));
		}
		return ret;
	}
}
