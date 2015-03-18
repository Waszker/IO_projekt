package ComputationalServer.ServerCore;

import java.math.BigInteger;
import java.util.HashMap;
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
	}

	void informaAboutConnectedComponent(final BigInteger id)
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
				Logger.log("Timeout for " + id + " has passed!\n");

			}
		}), core.timeout, TimeUnit.SECONDS)));
	}
}
