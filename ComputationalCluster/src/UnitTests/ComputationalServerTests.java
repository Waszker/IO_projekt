package UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

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

public class ComputationalServerTests
{
	private static int port = 9999;

	@Test
	public void computationalServerWithDefaultParametersTest1()
	{
		ComputationalServer server = new ComputationalServer(false, null, null,
				111, null, false);
		assertEquals(ComponentType.ComputationalServer, server.getType());
		assertEquals(GenericComponent.DEFAULT_PORT, server.getPort());
		assertEquals(ComputationalServer.DEFAULT_TIMEOUT, server.getTimeout());
		assertEquals(GenericComponent.DEFAUL_IP_ADDRESS, server.getIpAddress());
		assertEquals(ComputationalServer.DEFAULT_PORT,
				server.getMyLocalBackupPort());
		assertEquals(null, server.getCore());
	}

	@Test
	public void computationalServerWithIPAddressTest2()
	{
		ComputationalServer server = new ComputationalServer(false, 777, null,
				null, "127.0.0.1", false);
		assertEquals(ComponentType.ComputationalServer, server.getType());
		assertEquals(777, server.getPort());
		assertEquals(ComputationalServer.DEFAULT_TIMEOUT, server.getTimeout());
		assertEquals("127.0.0.1", server.getIpAddress());
		assertEquals(777, server.getMyLocalBackupPort());
	}

	@Test
	public void computationalServerRegisterTaskManagerGood()
	{
		Logger.setDebug(false);
		int port = getPort(), timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		Register message = new Register();
		message.setType(GenericComponent.ComponentType.TaskManager.name);

		try
		{
			new Thread(() -> {
				try
				{
					core.startListening(port, timeout);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					fail("Should not throw exception!");
				}
			}).start();

			Thread.sleep(500);
			IMessage received = sendMessages("127.0.0.1", port, message).get(0);

			assertEquals(Parser.MessageType.REGISTER_RESPONSE,
					received.getMessageType());
			assertEquals(timeout, ((RegisterResponse) received).getTimeout());
			assertEquals(1, core.getTaskManagers().size());
			assertEquals(0, core.getComputationalNodes().size());
			assertEquals(0, core.getProblemsToSolve().size());
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}
	}

	@Test
	public void computationalServerRegisterUnsupportedComponentGood()
	{
		Logger.setDebug(false);
		int port = getPort(), timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		Register message = new Register();
		message.setType(GenericComponent.ComponentType.ComputationalClient.name);

		try
		{
			new Thread(() -> {
				try
				{
					core.startListening(port, timeout);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					fail("Should not throw exception!");
				}
			}).start();

			Thread.sleep(500);
			IMessage received = sendMessages("127.0.0.1", port, message).get(0);

			assertEquals(Parser.MessageType.ERROR, received.getMessageType());
			assertEquals(0, core.getTaskManagers().size());
			assertEquals(0, core.getComputationalNodes().size());
			assertEquals(0, core.getProblemsToSolve().size());
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}
	}

	@Test
	public void computationalServerRegisterProblemGood()
	{
		Logger.setDebug(false);
		int port = getPort(), timeout = 5;
		ComputationalServerCore core = new ComputationalServerCore(null);
		SolveRequest message = new SolveRequest();
		message.setProblemType("TestProblem");

		try
		{
			new Thread(() -> {
				try
				{
					core.startListening(port, timeout);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					fail("Should not throw exception!");
				}
			}).start();

			Thread.sleep(500);
			IMessage received = sendMessages("127.0.0.1", port, message).get(1);

			assertEquals(Parser.MessageType.SOLVE_REQUEST_RESPONSE,
					received.getMessageType());
			assertEquals(0, core.getTaskManagers().size());
			assertEquals(0, core.getComputationalNodes().size());
			assertEquals(1, core.getProblemsToSolve().size());
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
			fail("Should not throw exception!");
		}
	}

	private List<IMessage> sendMessages(String address, int port,
			IMessage message) throws IOException
	{
		Socket socket = new Socket(address, port);

		GenericProtocol.sendMessages(socket, message);
		return GenericProtocol.receiveMessage(socket);
	}

	private synchronized int getPort()
	{
		return (ComputationalServerTests.port--);
	}
}
