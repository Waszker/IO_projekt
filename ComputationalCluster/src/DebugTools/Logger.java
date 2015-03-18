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
	public static void log(String message)
	{
		if (isDebugOn)
		{
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			System.out
					.println("/-------------------------------------------\\");
			System.out.print(df.format(today) + ":\n" + message);
			System.out
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
