package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import DebugTools.Logger;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.DivideProblem;
import XMLMessages.Solutiones;

/**
 * <p>
 * Class representing connected TaskManager in ComputationalCluster. It holds
 * information about current problems assigned to it.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class TaskManagerInfo
{
	/******************/
	/* VARIABLES */
	/******************/
	BigInteger id;
	List<IMessage> assignedMessages;
	List<String> supportedProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates TaskManager information with assigned id.
	 * </p>
	 * 
	 * @param id
	 */
	TaskManagerInfo(BigInteger id, List<String> solvableProblems)
	{
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
		return "TaskManager [id=" + id + ", assignedMessages="
				+ assignedMessages.size() + ", supportedProblems="
				+ supportedProblems + "]";
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

		if (message.getMessageType() == MessageType.DIVIDE_PROBLEM
				&& supportedProblems.contains(((DivideProblem) message)
						.getProblemType()))
			isSupported = true;
		else if (message.getMessageType() == MessageType.SOLUTION
				&& supportedProblems.contains(((Solutiones) message)
						.getProblemType())) isSupported = true;

		return isSupported;
	}
}
