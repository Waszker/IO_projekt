package UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ComputationalNode.ComputationalNode;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;

public class ComputationalNodeTests
{
	@Test
	public void ComputationalNodeTest1()
	{
		ComputationalNode node = new ComputationalNode(null, null, false);
		assertEquals(node.getType(), ComponentType.ComputationalNode);
		assertEquals(node.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(node.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}
	
	@Test
	public void ComputationalNodeTest2()
	{
		ComputationalNode node = new ComputationalNode("127.0.0.1", null, false);
		assertEquals(node.getType(), ComponentType.ComputationalNode);
		assertEquals(node.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(node.getIpAddress(), "127.0.0.1");
	}
	
	@Test
	public void ComputationalNodeTest3()
	{
		ComputationalNode node = new ComputationalNode(null, 1234, false);
		assertEquals(node.getType(), ComponentType.ComputationalNode);
		assertEquals(node.getPort(), 1234);
		assertEquals(node.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}
	
	@Test
	public void ComputationalNodeTest4()
	{
		ComputationalNode node = new ComputationalNode("127.0.0.1", 1234, false);
		assertEquals(node.getType(), ComponentType.ComputationalNode);
		assertEquals(node.getPort(), 1234);
		assertEquals(node.getIpAddress(), "127.0.0.1");
	}
	
}
