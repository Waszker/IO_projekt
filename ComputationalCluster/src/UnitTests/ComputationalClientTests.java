package UnitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigInteger;

import org.junit.Test;

import ComputationalClient.ComputationalClient;
import GenericCommonClasses.GenericComponent;
import GenericCommonClasses.GenericComponent.ComponentType;

public class ComputationalClientTests
{
	@Test
	public void ComputationalClientWithDefaultParametersTest1()
	{
		ComputationalClient client = new ComputationalClient(null, null, false,
				null, null);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
		assertEquals(client.getTimeout(), new BigInteger(
				ComputationalClient.DEFAULT_TIMEOUT));
	}

	@Test
	public void ComputationalClientWithIPAddressTest2()
	{
		ComputationalClient client = new ComputationalClient("127.0.0.1", null,
				false, null, null);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), "127.0.0.1");
		assertEquals(client.getTimeout(), new BigInteger(
				ComputationalClient.DEFAULT_TIMEOUT));
	}

	@Test
	public void ComputationalClientWithPortTest3()
	{
		ComputationalClient client = new ComputationalClient(null, 1234, false,
				null, null);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), 1234);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
		assertEquals(client.getTimeout(), new BigInteger(
				ComputationalClient.DEFAULT_TIMEOUT));
	}

	@Test
	public void ComputationalClientWithFilePathTest4()
	{
		ComputationalClient client = new ComputationalClient(null, null, false,
				"D:/a.txt", null);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
		assertEquals(client.getDataFile(), new File("D:/a.txt"));
		assertEquals(client.getTimeout(), new BigInteger(
				ComputationalClient.DEFAULT_TIMEOUT));
	}

	@Test
	public void ComputationalClientWithTimeoutTest5()
	{
		ComputationalClient client = new ComputationalClient(null, null, false,
				null, 123456);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), GenericComponent.DEFAULT_PORT);
		assertEquals(client.getIpAddress(), GenericComponent.DEFAUL_IP_ADDRESS);
		assertEquals(client.getTimeout(), new BigInteger("123456"));
	}

	@Test
	public void ComputationalClientWithAllParametersTest6()
	{
		ComputationalClient client = new ComputationalClient("127.0.0.1", 1234,
				false, "D:/a.txt", 5555);
		assertEquals(client.getType(), ComponentType.ComputationalClient);
		assertEquals(client.getPort(), 1234);
		assertEquals(client.getIpAddress(), "127.0.0.1");
		assertEquals(client.getDataFile(), new File("D:/a.txt"));
		assertEquals(client.getTimeout(), new BigInteger("5555"));
	}

}
