package Problems.DVRPProblem;

public class DVRPSolver
{
	/**
	 * <p>Solve DVRP.</p>
	 * @param numVehicles Number of vehicles.
	 * @param vehicleSpeed Vehicle speed.
	 * @param vehicleCapacity Vehicle capacity.
	 * @param depots Set of depots.
	 * @param clients Set of clients.
	 */
	public static double dvrp(int numVehicles, double vehicleSpeed, double vehicleCapacity, Depot[] depots,
							 Client[] clients)
	{
		int currentPartition = 0;
		Graph[] div = null;
		Graph g = new Graph(depots, clients, vehicleSpeed);
		
		//TODO: Waiting in depot for first client. 
		
		double best = Double.POSITIVE_INFINITY;
		while ( (div = g.divideGraph(numVehicles, currentPartition++)) != null ) //dla każdego podziału
		{
			double result = SeparateDVRPSolver.solveDVRPOnGraphSet(div, vehicleCapacity);
			if ( result < best )best = result;
		}
		return best*vehicleSpeed;
	}
}
