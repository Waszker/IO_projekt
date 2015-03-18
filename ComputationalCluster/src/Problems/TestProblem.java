package Problems;

import java.security.InvalidParameterException;

import GenericCommonClasses.IProblem;

/**
 * <p>
 * TestProblem - makes the string written uppercase
 * </p>
 * 
 * @see IProblem
 * @author Filip
 */
public class TestProblem implements IProblem
{
	private String data;
	
	public TestProblem()
	{
		data = "";
	}
	
	public TestProblem(String s)
	{
		data = new String(s);
	}
	
	@Override
	public String getType()
	{
		return "TestProblem";
	}

	@Override
	public byte[] getData()
	{
		return data.getBytes();
	}

	@Override
	public void loadData(byte[] data) throws InvalidParameterException
	{
		this.data = new String(data);
	}

	@Override
	public IProblem[] divide(int nodes)
	{
		double step = ((double)data.length() / (double)nodes) + 0.0000000000001;
		TestProblem[] ret = new TestProblem[nodes];
		for ( int i=0; i<nodes; i++ )
			ret[i] = new TestProblem(data.substring( (int)(i*step), (int)((i+1)*step)));
		return ret;
	}

	@Override
	public Object doComputation()
	{
		data.toUpperCase();
		return data;
	}

	@Override
	public Object mergeSolutions(Object[] partialSolutions)
	{
		StringBuilder result = new StringBuilder();
		for ( int i=0; i<partialSolutions.length; i++ )
			result.append((String)partialSolutions[i]);
		return result.toString();
	}
}
