package GenericCommonClasses;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;

public abstract class GenericFlagInterpreter
{

	/******************/
	/* VARIABLES */
	/******************/
	static Map<String, Object> FlagMap;

	/******************/
	/* FUNCTIONS */
	/******************/
	
	public Map interpretFlags(String[] args)
	{
		FlagMap=new HashMap<String, Object>();
		for(String s : args)
		{
			if(args[0].contentEquals("-address"))
			{
			
			}
		}
		return FlagMap;
	}
}
