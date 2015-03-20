package UnitTests;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.Test;

import pl.edu.pw.mini.se2.TaskSolver;
import Problems.TestProblem;
import TaskManager.TaskManager;
import XMLMessages.DivideProblem;
import XMLMessages.Solutiones;
import XMLMessages.Solutiones.Solutions;
import XMLMessages.Solutiones.Solutions.Solution;
import XMLMessages.SolvePartialProblems;
import XMLMessages.SolvePartialProblems.PartialProblems.PartialProblem;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.ProblemHelper;
import GenericCommonClasses.GenericComponent.ComponentType;

public class TaskManagerTests
{
	@Test
	public void TaskManagerTest1()
	{
		TaskManager manager = new TaskManager(null, null, false);
		assertEquals(manager.getType(), ComponentType.TaskManager);
		assertEquals(manager.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(manager.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}

	@Test
	public void TaskManagerTest2()
	{
		TaskManager manager = new TaskManager("127.0.0.1", null, false);
		assertEquals(manager.getType(), ComponentType.TaskManager);
		assertEquals(manager.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(manager.getIpAddress(), "127.0.0.1");
	}

	@Test
	public void TaskManagerTest3()
	{
		TaskManager manager = new TaskManager(null, 1234, false);
		assertEquals(manager.getType(), ComponentType.TaskManager);
		assertEquals(manager.getPort(), 1234);
		assertEquals(manager.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}

	@Test
	public void TaskManagerTest4()
	{
		TaskManager manager = new TaskManager("127.0.0.1", 1234, false);
		assertEquals(manager.getType(), ComponentType.TaskManager);
		assertEquals(manager.getPort(), 1234);
		assertEquals(manager.getIpAddress(), "127.0.0.1");
	}

	
	static TaskSolver theTaskSolver = null;
	
	@Test
	public void responseOnDivideProblem() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		String testString = "loggjfde4392sdf";
		final int TMID = 100;
		final String problemType = "TestProblem";
		final int numNodes = 5;
		final int problemID = 120;
		
		//prepare DivideProblem message
		DivideProblem dvm = new DivideProblem();
		dvm.setId(BigInteger.valueOf(problemID));
		dvm.setNodeID(BigInteger.valueOf(TMID));
		dvm.setComputationalNodes(BigInteger.valueOf(numNodes));
		dvm.setProblemType(problemType);
		dvm.setData(testString.getBytes());
		
		//extract problem type, instantinate TaskSolver
		theTaskSolver = ProblemHelper.instantinateTaskSolver(dvm);
		
		//invoke private static method in TaskManager
		//-------------------------------------------
		Method _generateResponse = TaskManager.class.getDeclaredMethod(
				"generateResponse", DivideProblem.class, TaskSolver.class,
				BigInteger.class);
		_generateResponse.setAccessible(true);
		SolvePartialProblems response = (SolvePartialProblems) _generateResponse
				.invoke(TaskManager.class, dvm, theTaskSolver, BigInteger.valueOf(TMID));
		//-------------------------------------------
		
		
		//expected result data (actual result will be compared with it)
		byte[][] divided = new TestProblem(testString).DivideProblem(numNodes);
		
		//comparsion
		assertEquals(problemID, response.getId().intValue());
		assert(response.getProblemType().contentEquals(problemType));
		List<PartialProblem> list = response.getPartialProblems().getPartialProblem();
		
		assertEquals(divided.length, list.size());
		
		for ( int i=0; i< divided.length; i++ )
		{
			byte[] t1 = list.get(i).getData();
			byte[] t2 = divided[i];
			
			assertEquals(t2.length, t1.length);
			
			for ( int j=0; j<t2.length; j++ )
				assertEquals(t2[j], t1[j]);
		}
	}
	
	@Test
	public void responseOnSolutions() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		String[] testResults = { "AB", "CDE", "FGH" };
		final String problemType = "TestProblem";
		final int problemID = 120;
		
		//prepare DivideProblem message
		Solutiones sm = new Solutiones();
		sm.setId(BigInteger.valueOf(problemID));
		sm.setProblemType(problemType);
		sm.setCommonData(null);
		Solutions s = new Solutions();
		sm.setSolutions(s);
		List<Solution> list = s.getSolution();
		for ( int i=0; i<testResults.length; i++ )
		{
			Solution sol = new Solution();
			sol.setTaskId(BigInteger.valueOf(i));
			sol.setType(problemType);
			sol.setData(testResults[i].getBytes());
			list.add(sol);
		}
		
		//invoke private static method in TaskManager
		//-------------------------------------------
		Method _generateResponse = TaskManager.class.getDeclaredMethod(
				"generateResponse", Solutiones.class, TaskSolver.class);
		_generateResponse.setAccessible(true);
		Solutiones response = (Solutiones) _generateResponse
				.invoke(TaskManager.class, sm, theTaskSolver);
		//-------------------------------------------
		
		
		//expected result data (actual result will be compared with it)
		String expected = "";
		for ( int i=0; i<testResults.length; i++ )
			expected += testResults[i];
		
		//comparsion
		assertEquals(problemID, response.getId().intValue());
		assert(response.getProblemType().contentEquals(problemType));
		assertEquals(1, response.getSolutions().getSolution().size());
		
		Solution result = response.getSolutions().getSolution().get(0);
		
		assert(result.getTaskId()==null);
		assert(result.getType().contentEquals(problemType));

		byte[] resultData = result.getData();
		byte[] expectedData = expected.getBytes();
		assertEquals(expectedData.length, resultData.length);
		
		for ( int i=0; i<expectedData.length; i++ )
			assertEquals(expectedData[i], resultData[i]);
	}
}
