package GenericCommonClasses;

/**
 * <p>
 * GenericComponent class is a base class for every ComputationalClient,
 * TaskManager and ComputationalNode class. It holds fields common for all three
 * classes and gathers common methods usable in them.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public abstract class GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/
	protected GenericConnector connector;

	/******************/
	/* FUNCTIONS */
	/******************/

	/**
	 * <p>
	 * Get connector instance that will be used in connecting to server.
	 * </p>
	 * 
	 * @return instance of connector
	 */
	public GenericConnector getConnector()
	{
		return connector;
	}

}
