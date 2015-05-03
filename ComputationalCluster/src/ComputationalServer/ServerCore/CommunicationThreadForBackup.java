package ComputationalServer.ServerCore;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import javax.xml.bind.JAXBException;

import DebugTools.Logger;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

public class CommunicationThreadForBackup extends AbstractServerCoreProtocol
{
	/******************/
	/* VARIABLES */
	/******************/
	Semaphore messageSemaphore;
	BlockingQueue<IMessage> backupMessageQueue;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates object with reference to main server core.
	 * </p>
	 * 
	 * @param core
	 */
	public CommunicationThreadForBackup(ComputationalServerCore core)
	{
		super(core);
		messageSemaphore = new Semaphore(0, true);
		backupMessageQueue = new ArrayBlockingQueue<>(
				ComputationalServerCore.MAX_MESSAGES, true);
	}

	@Override
	public void addBackupServerMessage(IMessage message)
	{
		// nothing to do - server in backup mode should not modify backup server
		// message queue
	}

	@Override
	public BigInteger registerComponent(BigInteger receivedId,
			int numberOfThreads, boolean deregister,
			ComponentType componentType, List<String> solvableProblems,
			Integer port, String address)
	{
		BigInteger id = new BigInteger("-1");

		if (deregister)
			core.componentMonitorThread.dropComponent(receivedId);
		else
			id = core.registerComponent(receivedId, numberOfThreads,
					componentType, solvableProblems, port, address);
		return id;
	}

	@Override
	public List<IMessage> getStatusResponseMessages(BigInteger id,
			int freeThreads)
	{
		// Backup server should never receive this message!
		Logger.log("Received message I should not have!\n");
		return new ArrayList<IMessage>();
	}

	@Override
	public List<IMessage> getBackupServerSynchronizationMessages()
	{
		// Backup server funcitonality does not specify this behaviour
		return new ArrayList<IMessage>();
	}

	@Override
	public Solution getProblemSolution(BigInteger id)
	{
		// Should not receive that
		Logger.log("Received message I should not have!\n");
		return null;
	}

	@Override
	public BigInteger registerProblem(BigInteger sentId, byte[] data,
			String problemType, BigInteger solvingTimeout)
	{
		BigInteger id = core.getCurrentFreeProblemId(sentId);
		core.problemsToSolve.put(id, new ProblemInfo(id, data, problemType,
				solvingTimeout));

		return id;
	}

	@Override
	public boolean setProblemPartsInfo(
			BigInteger id,
			byte[] commonData,
			List<SolvePartialProblems.PartialProblems.PartialProblem> partialProblems)
	{
		boolean isProblemJustDivided = false;
		ProblemInfo problem = core.problemsToSolve.get(id);

		// CN was assigned partial problem
		if (-1 != problem.parts)
		{
			ComputationalNodeInfo computationalNode = core.computationalNodes
					.get(partialProblems.get(0).getNodeID());
			Iterator<IMessage> it = core.delayedMessages.iterator();
			while (it.hasNext() && !partialProblems.isEmpty())
			{
				IMessage m = it.next();
				if (computationalNode.isProblemSupported(m))
				{
					PartialProblem pp = ((SolvePartialProblems) m)
							.getPartialProblems().getPartialProblem().get(0);
					if (partialProblems.contains(pp))
					{
						computationalNode.assignedMessages.add(m);
						core.delayedMessages.remove(m);
						partialProblems.remove(pp);
						break;
					}
				}
			}
		}
		// Got information from TM
		else
		{
			isProblemJustDivided = true;
			int numberOfParts = partialProblems.size();
			problem.data = commonData.clone();
			problem.parts = numberOfParts;
			problem.isProblemDivided = true;
			problem.isProblemCurrentlyDelegated = false;
		}

		return isProblemJustDivided;
	}

	@Override
	public List<Solution> informAboutProblemSolutions(BigInteger problemId,
			BigInteger taskManagerId, List<Solution> solution)
	{
		ProblemInfo problem = core.problemsToSolve.get(problemId);

		// If it was final solution
		if (problem.parts == 0)
		{
			problem.parts = -1;
			problem.finalSolution = solution.get(0);
			problem.isProblemCurrentlyDelegated = false;
			removeTaskManagerSpecificMessage(problemId);
		}
		// Solution for final merging was sent to TM
		else if (problem.parts == problem.partialSolutions.size())
		{
			problem.parts = 0;
			TaskManagerInfo taskManager = core.taskManagers.get(taskManagerId);
			Iterator<IMessage> it = core.delayedMessages.iterator();
			while (it.hasNext())
			{
				IMessage m = it.next();
				if (taskManager.isProblemSupported(m))
				{
					if (m.getMessageType() == MessageType.SOLUTION
							&& ((Solutiones) m).getId().equals(problemId))
					{
						taskManager.assignedMessages.add(m);
						core.delayedMessages.remove(m);
						break;
					}
				}
			}
		}
		else
		// Received partial solution
		{
			problem.partialSolutions.addAll(solution);
			for (Solution s : solution)
				removeComputationalNodeSpecificMessage(problemId, s.getTaskId());
		}

		return (problem.parts == problem.partialSolutions.size() ? problem.partialSolutions
				: null);
	}

	@Override
	public void informAboutComponentStatusMessage(BigInteger componentId)
	{
		// Should never receive that
		Logger.log("Received message I should not have!\n");
	}

	@Override
	public void reactToDivideProblem(BigInteger taskManagerId,
			BigInteger problemId)
	{
		TaskManagerInfo taskManager = core.taskManagers.get(taskManagerId);
		Iterator<IMessage> it = core.delayedMessages.iterator();
		while (it.hasNext())
		{
			IMessage m = it.next();
			if (taskManager.isProblemSupported(m))
			{
				if (m.getMessageType() == MessageType.DIVIDE_PROBLEM
						&& m.getProblemId().equals(problemId))
				{
					taskManager.assignedMessages.add(m);
					core.delayedMessages.remove(m);
					break;
				}
			}
		}
	}

	void parseMessages()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						messageSemaphore.acquire();
						IMessage message = backupMessageQueue.remove();
						Logger.log(message.getString() + "\n");
						core.delayedMessages.addAll(message.prepareResponse(
								CommunicationThreadForBackup.this, null));
						core.informAboutComponentChanges();
						core.informAboutComponentChanges();
					}
					catch (JAXBException | IOException | InterruptedException e)
					{
						// There were some problems so just print stack trace
						e.printStackTrace();
					}
					catch (NullPointerException e)
					{
						Logger.log("Got Null pointer exception caused by this message!\n"
								+ "I can't run further because this could cause some serious "
								+ "damage to data integrity!\n");
						System.exit(1);
					}
				}
			}
		}).start();
	}
}
