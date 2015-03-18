package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import DebugTools.Logger;

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
					Logger.log("Timeout for " + id
							+ " has passed!\nDropping lease\n");
					dropComponent(id);
				}
			}), core.timeout + 1, TimeUnit.SECONDS)));
		}

		return isValid;
	}

	private void dropComponent(BigInteger id)
	{
		if (core.backupServer.id == id)
		{
			core.backupServer = null;
		}
		else if (core.taskManagers.containsKey(id))
		{
			core.taskManagers.remove(id);
		}
		else if (core.computationalNodes.containsKey(id))
		{
			core.computationalNodes.remove(id);
		}
		invalidId.add(id);
	}
}
