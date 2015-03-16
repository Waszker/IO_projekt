package GenericCommonClasses;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <p>
 * Class responsible for parsing messages. It should be able to parse every
 * possible message that can be send by each component.
 * </p>
 * 
 * @author Monika Żurkowska
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
	 * @return enum indicating what type of message that is
	 */
	static MessageType parse(String messageContent)
	{
		MessageType result = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try
		{
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(messageContent
					.getBytes()));
			Node root = doc.getDocumentElement();
			result = getMessageTypeFromRoot(root);
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{}
		return result;
	}
	
	private static MessageType getMessageTypeFromRoot(Node root)
	{
		MessageType result = null;
		switch(root.getNodeName())
		{
			case "Register":
				result = MessageType.REGISTER;
				break;
				
			case "RegisterResponse":
				result = MessageType.REGISTER_RESPONSE;
				break;
				
			case "Status":
				result = MessageType.STATUS;
				break;
				
			case "SolveRequestResponse":
				result = MessageType.SOLVE_REQUEST_RESPONSE;
				break;

			case "SolveRequest":
				result = MessageType.SOLVE_REQUEST;
				break;
				
			case "SolvePartialProblems":
				result = MessageType.PARTIAL_PROBLEM;
				break;
				
			case "DivideProblem":
				result = MessageType.DIVIDE_PROBLEM;
				break;
				
			case "NoOperation":
				result = MessageType.NO_OPERATION;
				break;
				
			case "Solution":
				result = MessageType.SOLUTION;
				break;
		}
		
		return result;
	}

}
