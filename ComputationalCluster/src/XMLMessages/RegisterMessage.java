package XMLMessages;

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

	/******************/
	/* FUNCTIONS */
	/******************/
	public RegisterMessage(ComponentType componentType)
	{
		this.componentType = componentType;
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
		messageBuilder.append("</Register>");
		return messageBuilder.toString();
	}

}
