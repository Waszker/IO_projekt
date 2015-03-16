package UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import TaskManager.TaskManager;
import GenericCommonClasses.GenericComponent;
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
	
}
