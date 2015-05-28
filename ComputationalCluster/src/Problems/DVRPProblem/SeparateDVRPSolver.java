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
	private static boolean[] activeColumns;
	private static boolean[] activeRows;
	private static boolean[][] choosenEdges;

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

	public static boolean CheckIfClosedCycle(boolean[][] actualChoosenEdges)
	{
		int colindex = -1;
		int rowindex = -1;
		// wybieramy jakikolwiek poczatek
		for (int i = 0; i < graphSize; i++)
		{
			for (int j = 0; j < graphSize; j++)
			{
				if (actualChoosenEdges[i][j])
				{
					colindex = i;
					rowindex = j;
				}
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
				if (actualChoosenEdges[colindex][j]) rowindex = j;
			}
			vertices[i + 2] = rowindex;
		}
		if (vertices[0] == vertices[graphSize]) return true;
		return false;
	}

	public static void printCycle(Graph g, boolean[][] actualChoosenEdges)
	{
		int colindex = -1;
		int rowindex = -1;
		path.clear();
		// wybieramy jakikolwiek poczatek
		for (int i = 0; i < graphSize; i++)
		{
			for (int j = 0; j < graphSize; j++)
			{
				if (actualChoosenEdges[i][j])
				{
					colindex = i;
					rowindex = j;
				}
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
				if (actualChoosenEdges[colindex][j]) rowindex = j;
			}
			vertices[i + 2] = rowindex;
		}
		for (int i = 0; i <= graphSize; i++)
		{
			path.add(new PathNode(g.v[vertices[i]].getX(), g.v[vertices[i]]
					.getY(), 0));
		}
	}

	/* PRIVATE AUXILIARY FUNCTIONS */

	private static void TSP(Graph g, double cap, double[][] table, double d,
			boolean[] activeCol, boolean[] activeRow,
			boolean[][] actualChoosenEdges)
	{

		double oszac = 0;
		int index_col = -1;
		int index_row = -1;
		double bestCopy = d;
		double capCopy = cap;
		Client client;
		// przetwarzamy wiersze
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
		// przetwarzamy kolumny
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
		System.out.println("oszacowanie: " + d);
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
		}

		// zamkniecie cyklu i aktualizacja best
		if (colCount == 2 && rowCount == 2)
		{
			boolean do_work = true;
			bestCopy = d;
			capCopy = cap;
			for (int i = 0; i < 2; i++)
			{
				if (!do_work) break;
				for (int j = 0; j < 2; j++)
				{
					client = (Client) g.v[last[i][0]];
					if (cap < client.size)
					{
						d = d + g.e[last[i][0]][0] + g.e[0][last[i][0]];
						cap = maxCapacity;
					}
					if (d < client.time) d = client.time;
					cap -= client.size;
					client = (Client) g.v[last[j][0]];
					if (cap < client.size)
					{
						d = d + g.e[last[i][0]][0] + g.e[0][last[i][0]];
						cap = maxCapacity;
					}
					if (d < client.time) d = client.time;
					cap -= client.size;
					actualChoosenEdges[last[0][1]][last[i][0]] = true;
					actualChoosenEdges[last[1][1]][last[j][0]] = true;

					if (CheckIfClosedCycle(actualChoosenEdges))
					{
						System.out
								.println("  zamkniety cykl, dodane krawedzie: "
										+ last[0][1] + "->" + last[i][0]
										+ " i " + last[1][1] + "->"
										+ last[j][0]);
						do_work = false;
						break;
					}
					else
					{
						actualChoosenEdges[last[0][1]][last[i][0]] = false;
						actualChoosenEdges[last[1][1]][last[j][0]] = false;
						d = bestCopy;
						cap = capCopy;
					}
				}
			}

			for (int i = 0; i < graphSize; i++)
			{
				for (int j = 0; j < graphSize; j++)
				{
					System.out.print(actualChoosenEdges[j][i] == true ? 1 + " "
							: 0 + " ");
				}
				System.out.println(" ");
			}
			printCycle(g, actualChoosenEdges);
			if (d < currentBest) currentBest = d;
			return;
		}
		// wyznaczamy krawedz dzielaca
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

					// dla kolumny k
					for (int r = 0; r < graphSize; r++)
					{
						if (!activeRow[r]) continue;
						if (r != w)
						{
							if (table[k][r] < min_c) min_c = table[k][r];
						}
					}
					// dla wiersza w
					for (int c = 0; c < graphSize; c++)
					{
						if (!activeCol[c]) continue;
						if (c != k)
							if (table[c][w] < min_r) min_r = table[c][w];
					}

					if ((min_c + min_r) > max)
					{
						max = min_c + min_r;
						index_col = k;
						index_row = w;
					}
				}
			}
		}
		System.out.println("max to:" + max + " kolumna: " + index_col
				+ " wiersz: " + index_row);
		if (max == 0) // nie da sie wyznaczyc krawedzi dzielacej
			return;

		double[][] TL = new double[graphSize][graphSize];
		for (int i = 0; i < graphSize; i++)
			for (int j = 0; j < graphSize; j++)
				TL[i][j] = table[i][j];
		bestCopy = d;
		capCopy = cap;
		client = (Client) g.v[index_row];
		if (cap < client.size)
		{
			d = d + g.e[index_row][0] + g.e[0][index_row];
			cap = maxCapacity;
		}
		if (d < client.time) d = client.time;
		d += client.unld;
		cap -= client.size;
		actualChoosenEdges[index_col][index_row] = true;

		activeRow[index_row] = false;
		activeCol[index_col] = false;
		TL[index_row][index_col] = Double.POSITIVE_INFINITY; // krawedz odwrotna
		TSP(g, cap, TL, d, activeCol, activeRow, actualChoosenEdges); // moze
																		// poprawić
																		// best

		if (d >= currentBest) // czy to o to chodzi??
			return;
		actualChoosenEdges[index_col][index_row] = false;
		d = bestCopy;
		cap = capCopy;
		activeRow[index_row] = true;
		activeCol[index_col] = true;
		table[index_col][index_row] = Double.POSITIVE_INFINITY;
		TSP(g, cap, table, d, activeCol, activeRow, actualChoosenEdges);

	}

	// solves DVRP on one graph
	private static double oneDvrp(Graph g, double cap)
	{
		currentBest = Double.POSITIVE_INFINITY;
		path.clear();
		graphSize = g.v.length;
		activeColumns = new boolean[graphSize];
		activeRows = new boolean[graphSize];
		choosenEdges = new boolean[graphSize][graphSize];
		maxCapacity = cap;

		double[][] table = new double[g.e.length][g.e.length];
		for (int i = 0; i < g.e.length; ++i)
		{
			for (int j = 0; j < g.e[i].length; ++j)
			{
				table[i][j] = g.e[i][j].doubleValue();
			}
		}

		TSP(g, cap, table, 0, activeColumns, activeRows, choosenEdges);

		return currentBest;
	}

	/* PUBLIC METHODS */

	/**
	 * <p>
	 * Solves and sumes DVRP cost on each graph in the set.
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
