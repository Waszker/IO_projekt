package Problems;

import pl.edu.pw.mini.se2.TaskSolver;
import pl.edu.pw.mini.se2.TaskSolverState;

/**
 * <p>
 * TestProblem - makes the string uppercase
 * </p>
 * 
 * @see IProblem
 * @author Filip
 */
public class TestProblem extends TaskSolver
{
	public TestProblem(String sdata)
	{
		super(sdata.getBytes());
	}
	
	public TestProblem(byte[] problemData)
	{
		super(problemData);
	}

	@Override
	public byte[][] DivideProblem(int nodes)
	{
		double step = ((double)_problemData.length / (double)nodes) + 0.0000000000001;
		byte[][] ret = new byte[nodes][];
		String theString = new String(_problemData);
		
		for ( int i=0; i<nodes; i++ )
			ret[i] = theString.substring((int)(i*step), (int)((i+1)*step)).getBytes();
		
		return ret;
	}

	@Override
	public byte[] MergeSolution(byte[][] ps)
	{
		StringBuilder b = new StringBuilder();
		for ( int i=0; i<ps.length; i++ )
			b.append(new String(ps[i]));
		return b.toString().getBytes();
	}

	@Override
	public byte[] Solve(byte[] partialData, long timeout)
	{
		return new String(partialData).toUpperCase().getBytes();
	}

	@Override
	public Exception getException()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName()
	{
		return "TestProblem";
	}

	@Override
	public TaskSolverState getState()
	{
		return TaskSolverState.OK;
	}

	@Override
	protected void setException(Exception arg0)
	{
		throw new UnsupportedOperationException();
		
	}

	@Override
	protected void setName(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void setState(TaskSolverState arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
