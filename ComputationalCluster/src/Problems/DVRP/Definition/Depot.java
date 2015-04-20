package Problems.DVRP.Definition;

import java.awt.Point;

/**
 * <p>
 * Represents depot in DVRP.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class Depot
{
	/******************/
	/* VARIABLES */
	/******************/
	private Point location;

	/******************/
	/* FUNCTIONS */
	/******************/
	public Depot(Point location)
	{
		this.location = location;
	}

	/**
	 * <p>
	 * Returns location of depot.
	 * </p>
	 * 
	 * @return
	 */
	public Point getLocation()
	{
		return new Point(location);
	}

}
