package UnitTests;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;

import GenericCommonClasses.GenericFlagInterpreter;

public class FlagInterpreterTests
{

	@Test
	public void flagInterpreterTest1()
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

		} catch (NumberFormatException | UnknownHostException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterTest2()
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

		} catch (NumberFormatException | UnknownHostException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterTest3()
	{
		try
		{
			GenericFlagInterpreter.interpretFlags(new String[] { "-port", "40",
					"-address", "__unknown" });
			fail("UnknownHostException expected.");

		} catch (NumberFormatException e)
		{
			fail(e.getMessage());
		} catch (UnknownHostException e)
		{

		}
	}

	@Test
	public void flagInterpreterTest4()
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

		} catch (NumberFormatException | UnknownHostException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterTest5()
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

		} catch (NumberFormatException | UnknownHostException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void flagInterpreterTest6()
	{
		try
		{
			GenericFlagInterpreter
					.interpretFlags(new String[] { "-port", "4l0a" });
			fail("NumberFormatException expected.");

		} catch (UnknownHostException e)
		{
			fail(e.getMessage());
		} catch (NumberFormatException e)
		{

		}
	}
}
