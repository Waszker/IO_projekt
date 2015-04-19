package Problems.DVRP.Definition;

/**
 * <p>
 * Class representing DVRP problem instance. It consists of data required to
 * solve it and offers method for quick to-byte-(de)serialization.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class DVRPProblemDefinition
{
	/******************/
	/* VARIABLES */
	/******************/
	private Vehicle[] vehicles;
	private Depot[] depots;
	private Client[] clients;

	/******************/
	/* FUNCTIONS */
	/******************/
	public DVRPProblemDefinition()
	{
		// TODO: Fill this information here
	}
	
	/**
	 * <p>
	 * Serializes class to byte array for easy over-the-net sending.
	 * <p>
	 * 
	 * @return serialized object
	 */
	public byte[] serialize()
	{
		// TODO: Implement serialization
		return null;
	}

	public static DVRPProblemDefinition deserialize(byte[] data)
	{
		// TODO: Implement deserialization or specific constructor?
		return null;
	}

}
