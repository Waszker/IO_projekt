package ComputationalClient;

import java.io.IOException;
import java.math.BigInteger;

import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Register;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;

public class ComputationalClient extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/

	private BigInteger problemId;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClient(String address, Integer port,
			boolean isGuiEnabled)
	{
		super(address, port, isGuiEnabled, ComponentType.ComputationalClient);
	}

	@Override
	protected Register getComponentRegisterMessage()
	{
		Register r = new Register();
		r.setType(GenericComponent.ComponentType.ComputationalClient.name);
		return r;
	}

	@Override
	protected void reactToMessage(IMessage message)
	{
		showMessage(message.toString());

		if (message.getMessageType() == MessageType.SOLVE_REQUEST_RESPONSE)
		{
			problemId = ((SolveRequestResponse) message).getId();
		}
		if (message.getMessageType() == MessageType.SOLUTION)
		{

		}

	}
	
	protected void sendSolveRequestMessage(SolveRequest message)
	{
		try
		{
			this.sendMessages(message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public BigInteger getProblemId()
	{
		return problemId;
	}
}
