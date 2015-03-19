package UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import org.junit.Test;

import ComputationalServer.ComputationalServer;
import ComputationalServer.ServerCore.ComputationalServerCore;
import DebugTools.Logger;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;
import GenericCommonClasses.GenericProtocol;
import GenericCommonClasses.IMessage;
import GenericCommonClasses.Parser;
import XMLMessages.Register;
import XMLMessages.RegisterResponse;
import XMLMessages.SolveRequest;
import XMLMessages.SolveRequestResponse;

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
	public void ComputationalServerCoreGoodConnectionTest1()
	{
		Logger.setDebug(false);
		int port = 9999, timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		Register message = new Register();
		message.setType(GenericComponent.ComponentType.TaskManager.name);

		try
		{
			core.startListening(port, timeout);
			Socket socket = new Socket("127.0.0.1", port);

			GenericProtocol.sendMessages(socket, message);
			IMessage received = GenericProtocol.receiveMessage(socket).get(0);

			assertEquals(received.getMessageType(),
					Parser.MessageType.REGISTER_RESPONSE);
			assertEquals(((RegisterResponse) received).getTimeout(), timeout);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}

	}
	

	@Test
	public void ComputationalServerCoreBadConnectionTest1()
	{
		Logger.setDebug(false);
		int port = 9999, timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		Register message = new Register();
		message.setType(GenericComponent.ComponentType.TaskManager.name);

		try
		{
			core.startListening(port, timeout);
			Socket socket = new Socket("127.0.0.1", port);

			GenericProtocol.sendMessages(socket, message);
			IMessage received = GenericProtocol.receiveMessage(socket).get(0);

			assertEquals(received.getMessageType(),
					Parser.MessageType.REGISTER_RESPONSE);
			assertEquals(((RegisterResponse) received).getTimeout(), timeout);
		}
		catch (IOException e)
		{
		}

	}
	
	@Test
	public void ComputationalServerSolverRequestTest1()
	{
		Logger.setDebug(false);
		int port = 9999, timeout = 5;
		byte[] data = {55, 45, 63};
		ComputationalServerCore core = new ComputationalServerCore(null);
		SolveRequest message = new SolveRequest();
		message.setProblemType("TestProblem");
		message.setData(data);

		try
		{
			core.startListening(port, timeout);
			Socket socket = new Socket("127.0.0.1", port);

			GenericProtocol.sendMessages(socket, message);
			IMessage received = GenericProtocol.receiveMessage(socket).get(0);

			assertEquals(received.getMessageType(),
					Parser.MessageType.SOLVE_REQUEST_RESPONSE);
			assertEquals(((SolveRequestResponse) received).getId(), new BigInteger("1"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}

	}
}
