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
		
		String mergedResult = (String)tp.extractSolution(tp.mergeSolutions(partialSolutions));
		String oneResult = (String)tp.extractSolution(tp.doComputation());
		assert(mergedResult.contentEquals(oneResult));
		assertEquals("ABCDEFGH",oneResult);
	}
	
	@Test
	public void test2()
	{
		TestProblem tp = new TestProblem("abcdefgh");
		TestProblem[] _tp = (TestProblem[])tp.divide(300);
		List<byte[]> partialSolutions = new LinkedList<byte[]>();
		
		for ( int i=0; i<_tp.length; i++ )
			partialSolutions.add(_tp[i].doComputation());
		
		String mergedResult = (String)tp.extractSolution(tp.mergeSolutions(partialSolutions));
		String oneResult = (String)tp.extractSolution(tp.doComputation());
		assert(mergedResult.contentEquals(oneResult));
		assertEquals("ABCDEFGH",oneResult);
		
	}
	
	@Test
	public void test3()
	{
		TestProblem tp = new TestProblem("abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh");
		TestProblem[] _tp = (TestProblem[])tp.divide(2);
		List<byte[]> partialSolutions = new LinkedList<byte[]>();
		
		for ( int i=0; i<_tp.length; i++ )
			partialSolutions.add(_tp[i].doComputation());
		
		String mergedResult = (String)tp.extractSolution(tp.mergeSolutions(partialSolutions));
		String oneResult = (String)tp.extractSolution(tp.doComputation());
		assert(mergedResult.contentEquals(oneResult));
		assertEquals("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH",oneResult);
		
	}

}
