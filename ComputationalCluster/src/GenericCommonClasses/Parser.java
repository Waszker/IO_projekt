package GenericCommonClasses;

/**
 * <p>
 * Class responsible for parsing messages. It should be able to parse every
 * possible message that can be send by each component.
 * </p>
 * 
 * @author Monika �urkowska
 * @version 0.9
 * 
 */
public abstract class Parser
{

	/******************/
	/* ENUMS */
	/******************/
	/**
	 * Enum of possible types of messages to be sent.
	 * 
	 * <li>{@link #DIVIDE_PROBLEM}</li> <li>{@link #NO_OPERATION}</li> <li>
	 * {@link #PARTIAL_PROBLEM}</li> <li>{@link #REGISTER}</li> <li>
	 * {@link #REGISTER_RESPONSE}</li> <li>{@link #SOLUTION}</li> <li>
	 * {@link #SOLUTION_REQUEST}</li> <li>{@link #SOLVE_REQUEST}</li> <li>
	 * {@link #SOLVE_REQUEST_RESPONSE}</li> <li>{@link #STATUS}</li>
	 */
	public enum MessageType
	{
		/**
		 * Send to TM to start the action of dividing the problem instance to
		 * smaller tasks.
		 */
		DIVIDE_PROBLEM,

		/**
		 * Sent by CS in response to STATUS message
		 */
		NO_OPERATION,

		/**
		 * Sent by the TM after dividing the problem into smaller partial
		 * problems.
		 */
		PARTIAL_PROBLEM,

		/**
		 * Sent by TM, CN and Backup CS to the CS after they are activated.
		 */
		REGISTER,

		/**
		 * Sent as a response to the REGISTER message
		 */
		REGISTER_RESPONSE,

		/**
		 * Used for sending info about ongoing computations.
		 */
		SOLUTION,

		/**
		 * Sent from the CC in order to check whether the cluster has
		 * successfully computed the solution.
		 */
		SOLUTION_REQUEST,

		/**
		 * Sent by the CC to CS.
		 */
		SOLVE_REQUEST,

		/**
		 * Sent by CS to CC as an answer for the SOLVE_REQUEST.
		 */
		SOLVE_REQUEST_RESPONSE,

		/**
		 * Sent by TM, CN and Backup CS to CS at least as frequent as a timeout
		 * given in REGISTER_RESPONSE.
		 */
		STATUS
	}

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <P>
	 * Parses message according to message Type.
	 * </p>
	 * 
	 * @param messageContent
	 *            - raw message to be parsed
	 * @return parsed message
	 */
	static IMessage parse(String messageContent)
	{
		// TODO
		return null;
	}

}
