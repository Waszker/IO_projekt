package UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;

import GenericCommonClasses.GenericFlagInterpreter;

public class FlagInterpreterTests
{

	@Test
	public void flagInterpreterWithPortTest1() throws UnknownHostException
	{
		try
		{
			Map<String, Object> flagsMap;
			flagsMap = GenericFlagInterpreter.interpretFlags(new String[] {
					"-port", "40" });
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_PORT), 40);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_TIMEOUT),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_BACKUP),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI), null);

		}
		catch (NumberFormatException | IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterWithPortAndGoodAddressTest2()
	{
		try
		{
			Map<String, Object> flagsMap;
			flagsMap = GenericFlagInterpreter.interpretFlags(new String[] {
					"-port", "40", "-address", "127.0.0.1" });
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_PORT), 40);
			assert (((String) flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS))
					.contentEquals("127.0.0.1"));
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_TIMEOUT),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_BACKUP),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI), null);

		}
		catch (NumberFormatException | IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterWithPortAndBadAddressTest3()
	{
		try
		{
			GenericFlagInterpreter.interpretFlags(new String[] { "-port", "40",
					"-address", "__unknown" });
			fail("UnknownHostException expected.");

		}
		catch (NumberFormatException e)
		{
			fail(e.getMessage());
		}
		catch (UnknownHostException e)
		{

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void flagInterpreterWithPortAddressGuiAndBackupTest4()
	{
		try
		{
			Map<String, Object> flagsMap;
			flagsMap = GenericFlagInterpreter
					.interpretFlags(new String[] { "-port", "40", "-address",
							"127.0.0.1", "-gui", "-backup" });
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_PORT), 40);
			assert (((String) flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS))
					.contentEquals("127.0.0.1"));
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_TIMEOUT),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_BACKUP),
					true);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI), true);

		}
		catch (NumberFormatException | IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterWithTimeoutTest5()
	{
		try
		{
			Map<String, Object> flagsMap;
			flagsMap = GenericFlagInterpreter.interpretFlags(new String[] {
					"-t", "2015" });
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_PORT), null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_ADDRESS),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_TIMEOUT),
					2015);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_BACKUP),
					null);
			assertEquals(flagsMap.get(GenericFlagInterpreter.FLAG_IS_GUI), null);

		}
		catch (NumberFormatException | IOException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterWithBadPortTest6()
	{
		try
		{
			GenericFlagInterpreter.interpretFlags(new String[] { "-port",
					"4l0a" });
			fail("NumberFormatException expected.");

		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
		catch (NumberFormatException e)
		{

		}
	}
}
