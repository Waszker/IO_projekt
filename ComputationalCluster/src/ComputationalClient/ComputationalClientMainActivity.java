package ComputationalClient;

import java.io.IOException;
import java.util.Map;

import DebugTools.Logger;
import GenericCommonClasses.GenericFlagInterpreter;

public class ComputationalClientMainActivity {

	public static void main(String[] args) {
		Map<String, Object> flagsMap;
		try {
			flagsMap = GenericFlagInterpreter.interpretFlags(args);
			boolean isGuiEnabled = (flagsMap
					.get(GenericFlagInterpreter.FLAG_IS_GUI) != null);
			String serverIp = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_ADDRESS);
			Integer serverPort = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_PORT);
			String fileName = (String) flagsMap
					.get(GenericFlagInterpreter.FLAG_FILE);
			Integer timeout = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_TIMEOUT);
			Integer cutOffTime = (Integer) flagsMap
					.get(GenericFlagInterpreter.FLAG_CUTOFF);

			if (isGuiEnabled == false && fileName == null) {
				Logger.log("You need to specify a file\n USAGE: java -jar ComputationalClient.jar [-address [IP address]] [-port [port]] [-t [timeout]] -file [path] [-cutoff [cutofftime]]\n");
				return;
			}

			ComputationalClient client = new ComputationalClient(serverIp,
					serverPort, isGuiEnabled, fileName, timeout, cutOffTime);

			if (isGuiEnabled) {
				ComputationalClientWindow window = new ComputationalClientWindow(
						client);
				window.setVisible(true);
			} else {
				client.sendSolveRequestMessage();
				client.sendSolutionRequestMessage();
			}
		} catch (NumberFormatException | IndexOutOfBoundsException | IOException e) {
			Logger.log("USAGE: java -jar ComputationalClient.jar [-address [IP address]] [-port [port]] [-t [timeout]] -file [path] [-cutoff [cutofftime]]\n");
			
		}
	}

}
