package UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ComputationalServer.ComputationalServer;
import ComputationalServer.ServerCore.ClientMessage;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;

public class ComputationalServerTests
{
	@Test
	public void ComputationalServerTest1()
	{
		ComputationalServer server = new ComputationalServer(false, null, null,
				null, false);
		assertEquals(server.getType(), ComponentType.ComputationalServer);
		assertEquals(server.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(server.getTimeout(), ComputationalServer.DEFAULT_TIMEOUT);
		assertEquals(server.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
	}
	
	@Test
	public void ComputationalServerTest2()
	{
		ComputationalServer server = new ComputationalServer(false, 777, null,
				"127.0.0.1", false);
		assertEquals(server.getType(), ComponentType.ComputationalServer);
		assertEquals(server.getPort(), 777);
		assertEquals(server.getTimeout(), ComputationalServer.DEFAULT_TIMEOUT);
		assertEquals(server.getIpAddress(), "127.0.0.1");
	}
	

	@Test
	public void ComputationalServerTest3()
	{
		ClientMessage message = new ClientMessage("test", null);
		assertEquals(message.getMessageContent(), "test");
		assertEquals(message.getClientSocket(), null);
	}
}
