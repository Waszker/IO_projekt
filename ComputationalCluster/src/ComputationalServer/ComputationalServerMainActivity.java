package ComputationalServer;

import java.util.Map;

public class ComputationalServerMainActivity
{
	public static void main(String[] args)
	{
		Map<String, Object> flagsMap = GenericFlagInterpreter
				.interpretFlags(args);

		ComputationalServerWindow window = new ComputationalServerWindow(
				flagsMap.get("isBackup") == null);

		if (flagsMap.get("isGui") != null)
		{
			window.setVisible(true);
		}

	}
}
