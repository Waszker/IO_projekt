package Problems.DVRP.Definition;

import java.awt.Point;


/**
 * <p>
 * Represents client in DVRP.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class Client
{
	/******************/
	/* VARIABLES */
	/******************/
	private Point location;
	private int time;
	private Cargo cargoToTake;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates client at specified location, with specified request time and
	 * cargo to take.
	 * </p>
	 * 
	 * @param location
	 * @param time
	 * @param cargo
	 */
	public Client(Point location, int time, Cargo cargo)
	{
		this.location = location;
		this.time = time;
		this.cargoToTake = cargo;
	}

	/**
	 * <p>
	 * Gets location of client.
	 * </p>
	 * 
	 * @return
	 */
	public Point getLocation()
	{
		return new Point(location);
	}

	/**
	 * <p>
	 * Gets client's request time.
	 * </p>
	 * 
	 * @return
	 */
	public int getTime()
	{
		return time;
	}

	/**
	 * <p>
	 * Gets client's cargo information.
	 * </p>
	 * 
	 * @return
	 */
	public Cargo getCargoToTake()
	{
		return new Cargo(cargoToTake.getSize());
	}

}
