package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.IMessage;
import XMLMessages.DivideProblem;
import XMLMessages.Register;
import XMLMessages.Solutiones;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import XMLMessages.SolveRequest;

/**
 * <p>
 * Class responsible for parsing messages while being in backup state.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class MessageParserForBackupServer
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;
	private CommunicationThread communicationThread;

	/******************/
	/* FUNCTIONS */
	/******************/
	MessageParserForBackupServer(ComputationalServerCore core)
	{
		this.core = core;
		communicationThread = new CommunicationThread(core);
	}

	void parseMessages(List<IMessage> messages)
	{
		for (IMessage message : messages)
		{
			try
			{
				Logger.log("Received message: \n" + message.getString() + "\n");

				switch (message.getMessageType())
				{
					case REGISTER:
						reactToRegisterMessage((Register) message);
						break;

					case SOLVE_REQUEST:
						reactToSolveRequest((SolveRequest) message);
						break;

					case DIVIDE_PROBLEM:
						reactToDivideProblem((DivideProblem) message);
						break;

					case PARTIAL_PROBLEM:
						reactToPartialProblems((SolvePartialProblems) message);
						break;

					case SOLUTION:
						reactToSolution((Solutiones) message);
						break;

					default:
						Logger.log("Received message I should not have...\n");
						break;
				}
			}
			catch (JAXBException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		core.informAboutComponentChanges();
	}

	private void reactToRegisterMessage(Register message)
	{
		if (message.isDeregister())
		{
			core.componentMonitorThread.dropComponent(message.getId());
		}
		else
		{
			core.freeComponentId = message.getId().add(new BigInteger("1"));
			if (message.getType().contentEquals(ComponentType.TaskManager.name))
				core.taskManagers.put(message.getId(), new TaskManagerInfo(
						message.getId(), message));
			else
				core.computationalNodes.put(message.getId(),
						new ComputationalNodeInfo(message.getId(), message));
		}
	}

	private void reactToSolveRequest(SolveRequest message)
	{
		BigInteger id = message.getId();
		ProblemInfo problem = new ProblemInfo(id, message);
		core.problemsToSolve.put(id, problem);
	}

	private void reactToDivideProblem(DivideProblem message)
	{
		MessageGeneratorThread.assignProblemForTaskManager(
				core.taskManagers.get(message.getNodeID()),
				core.problemsToSolve.get(message.getId()));
	}

	private void reactToPartialProblems(SolvePartialProblems message)
			throws IOException
	{
		// If problem was just divided
		if (!core.problemsToSolve.get(message.getId()).isProblemDivided)
		{
			communicationThread.reactToPartialProblems(message, null);
		}
		else
		{
			// partial problem was sent to ComputationalNode
			for (PartialProblem pproblem : message.getPartialProblems()
					.getPartialProblem())
			{
				ProblemInfo problem = core.problemsToSolve.get(message.getId());
				for (int i = 0; i < problem.partialProblems.size(); i++)
				{
					if (problem.partialProblems.get(i).getTaskId().equals(pproblem
							.getTaskId()))
					{
						problem.partialProblems.remove(i);
						break;
					}
				}
				MessageGeneratorThread.assignPartialProblemForComputationaNode(
						core.computationalNodes.get(pproblem.getNodeID()),
						core.problemsToSolve.get(message.getId()), pproblem);
			}
		}
	}

	private void reactToSolution(Solutiones message)
	{
		// TODO: Differentiate between solution sent to TM or final solution
		// from TM or partial solution sent to CN
		ProblemInfo problem = core.problemsToSolve.get(message.getId());

		if (problem.isProblemReadyToSolve
				&& problem.isProblemCurrentlyDelegated)
		{
			// Received final solution!
			problem.finalSolution = message.getSolutions().getSolution().get(0);

			for (Map.Entry<BigInteger, TaskManagerInfo> entry : core.taskManagers
					.entrySet())
			{
				if (entry.getValue().assignedProblems.contains(message.getId()))
				{
					entry.getValue().assignedProblems.remove(message.getId());
				}
			}
		}
		else if (problem.isProblemDivided
				&& !problem.isProblemCurrentlyDelegated
				&& problem.isProblemReadyToSolve)
		{
			// Primary server sent solution to TM
			TaskManagerInfo taskManager = core.taskManagers.get(message
					.getSolutions().getSolution().get(0).getTaskId());
			MessageGeneratorThread.assignProblemForTaskManager(taskManager,
					problem);
		}
		else
		{
			// Received partial solution from ComputationalNode
			communicationThread.receivePartialSolution(problem, message);
		}
	}
}
