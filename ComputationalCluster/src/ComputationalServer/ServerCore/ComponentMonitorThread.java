package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import DebugTools.Logger;
import XMLMessages.Register;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;

/**
 * <p>
 * ComponentMonitorThread class is used by ComputationalServer to monitor lease
 * time of connected components. In case some component fails to deliver its
 * Status message on time it is removed from known list.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
class ComponentMonitorThread extends Thread
{
	/******************/
	/* VARIABLES */
	/******************/
	ComputationalServerCore core;
	private ScheduledThreadPoolExecutor pool;
	private HashMap<BigInteger, ScheduledFuture<?>> scheduledRemovals;
	private List<BigInteger> invalidId;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Create thread with reference to server's core.
	 * </p>
	 * 
	 * @param core
	 */
	public ComponentMonitorThread(ComputationalServerCore core)
	{
		this.core = core;
		pool = new ScheduledThreadPoolExecutor(1024);
		scheduledRemovals = new HashMap<BigInteger, ScheduledFuture<?>>();
		invalidId = new ArrayList<>();
		invalidId.add(new BigInteger("-1"));
	}

	/**
	 * <p>
	 * React to connected id. It can involve creating new component's lease or
	 * reseting counter for it. This method returns true if any of those actions
	 * were successful. Returning false can mean that component is not valid
	 * anymore.
	 * </p>
	 * 
	 * @param id
	 * @return is component valid
	 */
	boolean informaAboutConnectedComponent(final BigInteger id)
	{
		boolean isValid = true;

		if (invalidId.contains(id))
		{
			isValid = false;
		}
		else
		{
			if (null != scheduledRemovals.get(id))
			{
				scheduledRemovals.get(id).cancel(true);
				scheduledRemovals.remove(id);
			}

			scheduledRemovals.put(id, (pool.schedule(new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					Logger.log("Timeout for component id: " + id
							+ " has passed!\nDropping lease\n");
					dropComponent(id);
					core.informAboutComponentChanges();
				}
			}), core.timeout + 1, TimeUnit.SECONDS)));
		}

		return isValid;
	}

	void dropComponent(BigInteger id)
	{
		if (core.taskManagers.containsKey(id))
		{
			reactToTaskManagerFailure(id);
		}
		else if (core.computationalNodes.containsKey(id))
		{
			reactToComputationalNodeFailure(id);
		}

		if (null != core.backupServer && core.backupServer.id == id)
		{
			core.backupServer = null;
		}
		else
		{
			invalidId.add(id);
			informBackupServerAboutComponentFailure(id);
		}
	}

	private void reactToTaskManagerFailure(BigInteger id)
	{
		// TaskManager failure is easy to serve - just restore assigned problems
		// (their states are still kept inside ProblemInfo object)
		TaskManagerInfo info = core.taskManagers.get(id);

		for (BigInteger problemId : info.assignedProblems)
		{
			core.problemsToSolve.get(problemId).isProblemCurrentlyDelegated = false;
		}

		core.taskManagers.remove(id);
	}

	private void reactToComputationalNodeFailure(BigInteger id)
	{
		// ComputationalNode failure involves restoring partial problems
		ComputationalNodeInfo info = core.computationalNodes.get(id);

		for (Map.Entry<BigInteger, List<PartialProblem>> entry : info.assignedPartialProblems
				.entrySet())
		{
			ProblemInfo problem = core.problemsToSolve.get(entry.getKey());
			List<PartialProblem> partialProblems = entry.getValue();

			for (PartialProblem p : partialProblems)
			{
				problem.partialProblems.add(p);
			}
		}

		core.computationalNodes.remove(id);
	}

	private void informBackupServerAboutComponentFailure(BigInteger id)
	{
		Register message = new Register();
		message.setDeregister(true);
		message.setId(id);

		core.listOfMessagesForBackupServer.add(message);
	}
}
