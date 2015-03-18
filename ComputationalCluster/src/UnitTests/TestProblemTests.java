package UnitTests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import Problems.TestProblem;

public class TestProblemTests
{

	@Test
	public void test1()
	{
		TestProblem tp = new TestProblem("abcdefgh");
		TestProblem[] _tp = (TestProblem[])tp.divide(3);
		List<byte[]> partialSolutions = new LinkedList<byte[]>();
		
		for ( int i=0; i<_tp.length; i++ )
			partialSolutions.add(_tp[i].doComputation());
		
		assert(new String(tp.mergeSolutions(partialSolutions)).contentEquals(new String(tp.doComputation())));
		assertEquals("ABCDEFGH",new String(tp.doComputation()));
		
	}
	
	@Test
	public void test2()
	{
		TestProblem tp = new TestProblem("abcdefgh");
		TestProblem[] _tp = (TestProblem[])tp.divide(300);
		List<byte[]> partialSolutions = new LinkedList<byte[]>();
		
		for ( int i=0; i<_tp.length; i++ )
			partialSolutions.add(_tp[i].doComputation());
		
		assert(new String(tp.mergeSolutions(partialSolutions)).contentEquals(new String(tp.doComputation())));
		assertEquals("ABCDEFGH",new String(tp.doComputation()));
		
	}
	
	@Test
	public void test3()
	{
		TestProblem tp = new TestProblem("abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh");
		TestProblem[] _tp = (TestProblem[])tp.divide(2);
		List<byte[]> partialSolutions = new LinkedList<byte[]>();
		
		for ( int i=0; i<_tp.length; i++ )
			partialSolutions.add(_tp[i].doComputation());
		
		assert(new String(tp.mergeSolutions(partialSolutions)).contentEquals(new String(tp.doComputation())));
		assertEquals("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH",new String(tp.doComputation()));
		
	}

}
