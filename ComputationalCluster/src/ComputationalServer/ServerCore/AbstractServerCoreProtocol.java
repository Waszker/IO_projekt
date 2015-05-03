package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.Map;

import XMLMessages.NoOperation;
import XMLMessages.SolvePartialProblems;
import XMLMessages.RegisterResponse.BackupCommunicationServers;
import XMLMessages.RegisterResponse.BackupCommunicationServers.BackupCommunicationServer;
import GenericCommonClasses.IServerProtocol;
import GenericCommonClasses.GenericComponent.ComponentType;

public abstract class AbstractServerCoreProtocol implements IServerProtocol
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;

	/******************/
	/* FUNCTIONS */
	/******************/
	public AbstractServerCoreProtocol(ComputationalServerCore core)
	{
		this.core = core;
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
}
