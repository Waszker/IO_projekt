package DebugTools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Logger class cannot be instantiated. It's only purpose is to provide
 * easy-to-use logging interface.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public final class Logger
{
	/******************/
	/* VARIABLES */
	/******************/
	private static boolean isDebugOn = true;

	/******************/
	/* FUNCTIONS */
	/******************/
	private Logger()
	{
	}

	/**
	 * <p>
	 * Appends date to message and sends it to stdout.
	 * </p>
	 * 
	 * @param message
	 */
	public static void log(String message) // NOPMD by waszka on 6/8/15 12:05 AM
	{
		if (isDebugOn)
		{
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			System.out // NOPMD by waszka on 6/8/15 12:06 AM
					.println("/-------------------------------------------\\");
			System.out // NOPMD by waszka on 6/8/15 12:06 AM
					.print(df.format(today) + ":\n" + message);
			System.out // NOPMD by waszka on 6/8/15 12:06 AM
					.println("\\-------------------------------------------/\n");
		}
	}

	/**
	 * <p>
	 * Sets debug mode.
	 * </p>
	 * 
	 * @param isOn
	 */
	public static void setDebug(boolean isOn)
	{
		isDebugOn = isOn;
	}
}
