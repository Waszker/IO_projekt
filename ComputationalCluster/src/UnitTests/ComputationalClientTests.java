package UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ComputationalClient.ComputationalClient;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;

public class ComputationalClientTests
{
	@Test
	public void ComputationalClientTest1()
	{
		ComputationalClient client = new ComputationalClient(null, null, false);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}
	
	@Test
	public void ComputationalClientTest2()
	{
		ComputationalClient client = new ComputationalClient("127.0.0.1", null, false);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), "127.0.0.1");
	}
	
	@Test
	public void ComputationalClientTest3()
	{
		ComputationalClient client = new ComputationalClient(null, 1234, false);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), 1234);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}
	
	@Test
	public void ComputationalClientTest4()
	{
		ComputationalClient client = new ComputationalClient("127.0.0.1", 1234, false);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), 1234);
		assertEquals(client.getIpAddress(), "127.0.0.1");
	}
	
}
