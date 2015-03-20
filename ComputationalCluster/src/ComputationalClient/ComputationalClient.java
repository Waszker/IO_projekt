package ComputationalClient;

import java.io.IOException;
import java.math.BigInteger;

import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Register;
import XMLMessages.SolutionRequest;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;
import DebugTools.Logger;


/**
 * <p>
 * ComputationalClient is a class providing the Computational Client logic.
 * </p>
 * 
 * @author Anna Zawadzka
 */

public class ComputationalClient extends GenericComponent
{
	/******************/
	/* VARIABLES */
	/******************/

	private BigInteger problemId;
	private BigInteger taskId;

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
	{	//to jest niepotrzebne
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
			//server ma timeout czasu na rozwiazanie problemu, 
			//jesli timeout mija, klient konczy prace?
			// sendSolutionRequestMessage();
		}
		if (message.getMessageType() == MessageType.SOLUTION)
		{
			taskId = ((Solution) message).getTaskId();	
			byte[] solutionData =((Solution) message).getData();
			Logger.log("Task id: " + taskId + "\n");
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

	protected void sendSolutionRequestMessage()
	{
		SolutionRequest sr = new SolutionRequest();
		//sr.setId(problemId);
		sr.setId(new BigInteger("1"));
		try
		{
			this.sendMessages(sr);
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
