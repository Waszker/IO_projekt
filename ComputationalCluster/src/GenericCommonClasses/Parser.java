package GenericCommonClasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import DebugTools.Logger;
import XMLMessages.DivideProblem;
import XMLMessages.Error;
import XMLMessages.NoOperation;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import XMLMessages.Status;

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
		STATUS,
		
		ERROR
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
	 * @return message or null
	 */
	public static IMessage parse(String messageContent)
	{
		IMessage result = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try
		{
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(messageContent
					.getBytes()));
			Node root = doc.getDocumentElement();
			result = getMessageTypeFromRoot(root, messageContent);
		}
		catch (ParserConfigurationException | SAXException | IOException
				| JAXBException e)
		{
			Logger.log("Badly formatted message: \n" + messageContent + "\n");
		}

		return result;
	}

	/**
	 * <p>
	 * Marshalls message into string object so it can be easily sent over
	 * network. Marshalled object can then be unmarshalled using parse() method.
	 * </p>
	 * 
	 * @param message
	 * @param type
	 * @return
	 * @throws JAXBException
	 */
	public static String marshallMessage(IMessage message,
			@SuppressWarnings("rawtypes") Class type) throws JAXBException
	{
		String string;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(type);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(message, out);
		string = new String(out.toByteArray());

		return string;
	}

	private static IMessage getMessageTypeFromRoot(Node root,
			String messageString) throws JAXBException
	{
		IMessage result = null;
		switch (root.getNodeName())
		{
			case "Register":
				result = getMessage(Register.class, messageString);
				break;

			case "RegisterResponse":
				result = getMessage(RegisterResponse.class, messageString);
				break;

			case "Status":
				result = getMessage(Status.class, messageString);
				break;

			case "SolveRequestResponse":
				result = getMessage(SolveRequestResponse.class, messageString);
				break;

			case "SolveRequest":
				result = getMessage(SolveRequest.class, messageString);
				break;

			case "SolvePartialProblems":
				result = getMessage(SolvePartialProblems.class, messageString);
				break;

			case "DivideProblem":
				result = getMessage(DivideProblem.class, messageString);
				break;

			case "NoOperation":
				result = getMessage(NoOperation.class, messageString);
				break;

			case "Solutions":
				result = getMessage(Solutiones.class, messageString);
				break;
				
			case "Error":
				result = getMessage(Error.class, messageString);
				break;				
		}

		return result;
	}

	private static IMessage getMessage(
			@SuppressWarnings("rawtypes") Class classToUnmarshall, String xml)
			throws JAXBException
	{
		IMessage message;

		JAXBContext jaxbContext = JAXBContext.newInstance(classToUnmarshall);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(xml);
		message = (IMessage) jaxbUnmarshaller.unmarshal(reader);

		return message;
	}

}
