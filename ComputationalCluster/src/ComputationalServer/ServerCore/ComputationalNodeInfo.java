package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.SolvePartialProblems;

/**
 * <p>
 * Class representing ComputationalNode in ComputationalServer internal
 * information list.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class ComputationalNodeInfo
{
	/******************/
	/* VARIABLES */
	/******************/
	int numberOfThreads;
	BigInteger id;
	List<String> supportedProblems;
	List<IMessage> assignedMessages;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates ComputationalNode information with assigned id.
	 * </p>
	 * 
	 * @param id
	 */
	ComputationalNodeInfo(int noOfThreads, BigInteger id,
			List<String> solvableProblems)
	{
		this.numberOfThreads = noOfThreads;
		this.id = id;
		assignedMessages = new ArrayList<>();
		try
		{
			supportedProblems = new ArrayList<>(solvableProblems);
		}
		catch (NullPointerException e)
		{
			Logger.log("Looks like component " + id
					+ " solves no problems at all!\n");
		}
	}

	@Override
	public String toString()
	{
		return "ComputationalNodeInfo [id=" + id + ", supportedProblems="
				+ supportedProblems + ", assignedMessages="
				+ assignedMessages.size() + "]";
	}

	/**
	 * <p>
	 * Checks if problem type is supported by this component.
	 * </p>
	 * 
	 * @param message
	 * @return
	 */
	boolean isProblemSupported(IMessage message)
	{
		boolean isSupported = false;

		if (message.getMessageType() == MessageType.PARTIAL_PROBLEM
				&& supportedProblems.contains(((SolvePartialProblems) message)
						.getProblemType())) isSupported = true;

		return isSupported;
	}
}
