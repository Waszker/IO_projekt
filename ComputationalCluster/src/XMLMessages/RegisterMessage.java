package XMLMessages;

import java.util.List;

import GenericCommonClasses.IMessage;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.Parser.MessageType;

/**
 * <p>
 * Register message is sent when component wants to connect to the server.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class RegisterMessage implements IMessage
{
	/******************/
	/* VARIABLES */
	/******************/
	private ComponentType componentType;
	private boolean isDeregistering;
	private int componentId, parallelThreads;
	private List<Integer> solvableProblems;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates register message that can be sent to ComputationalServer in order
	 * to register or deregister component.
	 * </p>
	 * 
	 * @param componentId: -1 if none available
	 * @param componentType
	 * @param isDeregistering: true if you want to deregister
	 * @param solvableProblems: null if not applicable
	 * @param parallelThreads
	 */
	public RegisterMessage(int componentId, ComponentType componentType,
			boolean isDeregistering, List<Integer> solvableProblems,
			int parallelThreads)
	{
		this.componentType = componentType;
		this.isDeregistering = isDeregistering;
		this.componentId = componentId;
		this.solvableProblems = solvableProblems;
		this.parallelThreads = parallelThreads;
	}

	@Override
	public MessageType getType(String messageContent)
	{
		return MessageType.REGISTER;
	}

	@Override
	public String getString()
	{
		StringBuilder messageBuilder = new StringBuilder(
				"﻿<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
						+ "<Register  xmlns=\"http://www.mini.pw.edu.pl/ucc/\""
						+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
						+ "xsi:noNamespaceSchemaLocation=\"Register.xsd\">"
						+ "<Type>");
		messageBuilder.append(componentType.name + "</Type>");

		if (null != solvableProblems)
		{
			messageBuilder.append("<SolvableProblems>");
			for (Integer p : solvableProblems)
			{
				messageBuilder.append("<ProblemName>" + String.valueOf(p)
						+ "</ProblemName>");
			}
			messageBuilder.append("</SolvableProblems>");
		}

		messageBuilder.append("<ParallelThreads>");
		messageBuilder.append(String.valueOf(parallelThreads));
		messageBuilder.append("</ParallelThreads><Deregister>");
		messageBuilder
				.append(String.valueOf(isDeregistering) + "</Deregister>");
		messageBuilder.append("<Id>" + componentId + "</Id>");
		messageBuilder.append("</Register>");

		return messageBuilder.toString();
	}

}
