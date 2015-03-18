package UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

import org.junit.Test;

import ComputationalServer.ComputationalServer;
import ComputationalServer.ServerCore.ClientMessage;
import ComputationalServer.ServerCore.ComputationalServerCore;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;

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
	
	@Test
	public void ComputationalServerCoreTest1()
	{
		int port = 9999, timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		Register message = new Register();		
		message.setType(GenericComponent.ComponentType.TaskManager.name);
		
		try
		{
			core.startListening(port, timeout);
			Socket socket = new Socket("127.0.0.1", port);
			
			GenericProtocol.sendMessages(socket, message);
			IMessage received = GenericProtocol.receiveMessage(socket);
			
			assertEquals(received.getMessageType(), Parser.MessageType.REGISTER_RESPONSE);			
			assertEquals(((RegisterResponse)received).getTimeout(), timeout);			
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}
		
	}
}
