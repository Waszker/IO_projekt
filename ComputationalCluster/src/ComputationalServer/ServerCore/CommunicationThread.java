package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.IServerProtocol;
import GenericCommonClasses.Parser.MessageType;
import XMLMessages.DivideProblem;
import XMLMessages.NoOperation;
import XMLMessages.RegisterResponse.BackupCommunicationServers;
import XMLMessages.RegisterResponse.BackupCommunicationServers.BackupCommunicationServer;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;

/**
 * <p>
 * Class used by IMessages to get and set information associated with
 * computational server.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * @version 1.1 complete refactoring for the more object-oriented approach
 * 
 */
class CommunicationThread implements IServerProtocol
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;

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
	public CommunicationThread(ComputationalServerCore core)
	{
		this.core = core;
	}

	@Override
	public BigInteger registerComponent(
			GenericComponent.ComponentType componentType,
			List<String> solvableProblems, Integer remotePort,
			String remoteAddress)
	{
		return core.registerComponent(componentType, solvableProblems,
				remotePort, remoteAddress);
	}

	@Override
	public BigInteger registerProblem(byte[] data, String problemType,
			BigInteger solvingTimeout)
	{
		BigInteger id = core.getCurrentFreeProblemId();
		core.problemsToSolve.put(id, new ProblemInfo(id, data, problemType,
				solvingTimeout));

		return id;
	}

	@Override
	public NoOperation getNoOperationMessage()
	{
		NoOperation noOperation = new NoOperation();
		XMLMessages.NoOperation.BackupCommunicationServers backupServers = new XMLMessages.NoOperation.BackupCommunicationServers();

		if (null != core.backupServer)
		{
			backupServers
					.setBackupCommunicationServer(new XMLMessages.NoOperation.BackupCommunicationServers.BackupCommunicationServer());
			backupServers.getBackupCommunicationServer().setAddress(
					core.backupServer.address);
			backupServers.getBackupCommunicationServer().setPort(
					core.backupServer.port);
		}
		noOperation.setBackupCommunicationServers(backupServers);

		return noOperation;
	}

	@Override
	public List<IMessage> getStatusResponseMessages(BigInteger id,
			int freeThreads)
	{
		List<IMessage> messages = new ArrayList<>(freeThreads);
		Iterator<IMessage> it = core.delayedMessages.iterator();

		switch (getComponentTypeFromId(id))
		{
			case TaskManager:
				TaskManagerInfo taskManager = core.taskManagers.get(id);

				while ((freeThreads--) > 0 && it.hasNext())
				{
					IMessage m = it.next();
					if (taskManager.isProblemSupported(m))
					{
						// For DIVIDE_PROBLEM we have to set some specific
						// options
						if (m.getMessageType() == MessageType.DIVIDE_PROBLEM
								&& core.computationalNodes.size() > 0)
						{
							DivideProblem message = (DivideProblem) m;
							message.setNodeID(id);
							message.setComputationalNodes(new BigInteger(String
									.valueOf(core.computationalNodes.size())));
						}
						messages.add(m);
					}

				}
				taskManager.assignedMessages.addAll(messages);

				break;

			case ComputationalNode:
				ComputationalNodeInfo computationalNode = core.computationalNodes
						.get(id);

				while ((freeThreads--) > 0 && it.hasNext())
				{
					IMessage m = it.next();
					if (computationalNode.isProblemSupported(m))
						messages.add(m);
				}
				computationalNode.assignedMessages.addAll(messages);

				break;

			default:
				break;
		}
		core.delayedMessages.removeAll(messages);

		return messages;
	}

	@Override
	public List<IMessage> getBackupServerSynchronizationMessages()
	{
		List<IMessage> messages = new ArrayList<>();

		if (core.backupServer.indexOfLastUnSynchronizedMessage < core.listOfMessagesForBackupServer
				.size())
			messages.addAll(core.listOfMessagesForBackupServer.subList(
					core.backupServer.indexOfLastUnSynchronizedMessage,
					core.listOfMessagesForBackupServer.size()));
		core.backupServer.indexOfLastUnSynchronizedMessage = core.listOfMessagesForBackupServer
				.size();

		return messages;
	}

	@Override
	public Solution getProblemSolution(BigInteger id)
	{
		Solution solution = new Solution();
		solution.setType(ProblemInfo.ProblemStatus.Ongoing.text);

		if (core.problemsToSolve.get(id) != null
				&& core.problemsToSolve.get(id).finalSolution != null)
		{
			solution = core.problemsToSolve.get(id).finalSolution;
			solution.setType(ProblemInfo.ProblemStatus.Final.text);
		}

		return solution;
	}

	@Override
	public int getServerTimeout()
	{
		return core.timeout;
	}

	@Override
	public BackupCommunicationServers getBackupServer()
	{
		BackupCommunicationServers backupServers = new BackupCommunicationServers();
		if (null != core.backupServer)
		{
			backupServers
					.setBackupCommunicationServer(new BackupCommunicationServer());
			backupServers.getBackupCommunicationServer().setAddress(
					core.backupServer.address);
			backupServers.getBackupCommunicationServer().setPort(
					core.backupServer.port);
		}
		else
			backupServers = null;

		return backupServers;
	}

	@Override
	public void addBackupServerMessage(IMessage message)
	{
		core.listOfMessagesForBackupServer.add(message);
	}

	@Override
	public ComponentType getComponentTypeFromId(BigInteger id)
	{
		ComponentType type = ComponentType.ComputationalClient;

		if (core.taskManagers.containsKey(id))
			type = ComponentType.TaskManager;
		if (core.computationalNodes.containsKey(id))
			type = ComponentType.ComputationalNode;
		if (null != core.backupServer && core.backupServer.id.equals(id))
			type = ComponentType.ComputationalServer;

		return type;
	}

	@Override
	public void setProblemPartsInfo(BigInteger id, byte[] commonData,
			int numberOfParts)
	{
		ProblemInfo problem = core.problemsToSolve.get(id);
		problem.data = commonData.clone();
		problem.parts = numberOfParts;
		problem.isProblemDivided = true;
		problem.isProblemCurrentlyDelegated = false;
	}

	@Override
	public void removeTaskManagerSpecificMessage(BigInteger problemId)
	{
		LOOP: for (Map.Entry<BigInteger, TaskManagerInfo> entry : core.taskManagers
				.entrySet())
		{
			TaskManagerInfo taskManager = entry.getValue();

			for (int i = 0; i < taskManager.assignedMessages.size(); i++)
				if (taskManager.assignedMessages.get(i).getProblemId()
						.equals(problemId))
				{
					taskManager.assignedMessages.remove(i);
					break LOOP;
				}
		}

	}

	@Override
	public void removeComputationalNodeSpecificMessage(BigInteger problemId,
			BigInteger taskId)
	{
		LOOP: for (Map.Entry<BigInteger, ComputationalNodeInfo> entry : core.computationalNodes
				.entrySet())
		{
			ComputationalNodeInfo computationalNode = entry.getValue();

			for (int i = 0; i < computationalNode.assignedMessages.size(); i++)
				if (computationalNode.assignedMessages.get(i).getProblemId()
						.equals(problemId)
						&& ((SolvePartialProblems) computationalNode.assignedMessages
								.get(i)).getPartialProblems()
								.getPartialProblem().get(0).getTaskId()
								.equals(taskId))
				{
					computationalNode.assignedMessages.remove(i);
					break LOOP;
				}
		}
	}

	@Override
	public List<Solution> informAboutProblemSolution(BigInteger problemId,
			Solution solution)
	{
		ProblemInfo problem = core.problemsToSolve.get(problemId);

		// If it was final solution
		if (problem.parts == 0)
		{
			problem.parts = -1;
			problem.finalSolution = solution;
			removeTaskManagerSpecificMessage(problemId);
		}
		else
		// Received partial solution
		{
			problem.partialSolutions.add(solution);
			problem.parts--;
			removeComputationalNodeSpecificMessage(problemId,
					solution.getTaskId());
		}

		return (problem.parts == 0 ? problem.partialSolutions : null);
	}

	@Override
	public void informAboutComponentStatusMessage(BigInteger componentId)
	{
		core.componentMonitorThread.informaAboutConnectedComponent(componentId);
	}
}
