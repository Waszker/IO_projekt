package UnitTests;

import org.junit.Test;

import Problems.TestProblem;

public class TestProblemTests
{
	private boolean compareArrays(byte[] arr1, byte[] arr2)
	{
		if (arr1.length != arr2.length)
			return false;
		for (int i = 0; i < arr1.length; i++)
			if (arr1[i] != arr2[i])
				return false;
		return true;
	}

	@Test
	public void divideShortProblemAndSolveSolutionTest1()
	{
		final String testString = "abcdefgh";
		final int parts = 3;

		byte[] inputData = testString.getBytes();
		TestProblem tp = new TestProblem(inputData);
		byte[][] d = tp.DivideProblem(parts);
		for (int i = 0; i < d.length; i++)
			d[i] = tp.Solve(d[i], 0);
		byte[] result_m = tp.MergeSolution(d);
		byte[] result_1 = tp.Solve(inputData, 0);
		byte[] resultBytesByString = testString.toUpperCase().getBytes();

		assert (compareArrays(result_m, result_1));
		assert (compareArrays(result_1, resultBytesByString));
	}
	
	@Test
	public void divideLongProblemAndMergeSolutionTest2()
	{
		final String testString = "abcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefghabcdefgh";
		final int parts = 3;

		byte[] inputData = testString.getBytes();
		TestProblem tp = new TestProblem(inputData);
		byte[][] d = tp.DivideProblem(parts);
		for (int i = 0; i < d.length; i++)
			d[i] = tp.Solve(d[i], 0);
		byte[] result_m = tp.MergeSolution(d);
		byte[] result_1 = tp.Solve(inputData, 0);
		byte[] resultBytesByString = testString.toUpperCase().getBytes();

		assert (compareArrays(result_m, result_1));
		assert (compareArrays(result_1, resultBytesByString));
	}
	
	@Test
	public void divideShortProblemIntoManyPartsTest3()
	{
		final String testString = "aaaaaa";
		final int parts = 3000;

		byte[] inputData = testString.getBytes();
		TestProblem tp = new TestProblem(inputData);
		byte[][] d = tp.DivideProblem(parts);
		for (int i = 0; i < d.length; i++)
			d[i] = tp.Solve(d[i], 0);
		byte[] result_m = tp.MergeSolution(d);
		byte[] result_1 = tp.Solve(inputData, 0);
		byte[] resultBytesByString = testString.toUpperCase().getBytes();

		assert (compareArrays(result_m, result_1));
		assert (compareArrays(result_1, resultBytesByString));
	}
}
