package Problems.DVRP.Definition;

/**
 * <p>
 * Represents products given by and to clients and moved from place to place by
 * vehicles.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class Cargo
{
	/******************/
	/* VARIABLES */
	/******************/
	private int size;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates Cargo object that takes 'size' in vehicle.
	 * </p>
	 * 
	 * @param size
	 */
	public Cargo(int size)
	{
		this.size = size;
	}

	/**
	 * <p>
	 * Get size of cargo.
	 * </p>
	 * 
	 * @return size
	 */
	public int getSize()
	{
		return size;
	}

}
