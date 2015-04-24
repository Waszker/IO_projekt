package GenericCommonClasses;

import java.math.BigInteger;
import java.util.List;

import XMLMessages.RegisterResponse.BackupCommunicationServers;

/**
 * <p>
 * Interface for server protocol.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public interface IServerProtocol
{
	/**
	 * <p>
	 * Returns timeout specified by server.
	 * </p>
	 * 
	 * @return
	 */
	public int getServerTimeout();

	/**
	 * <p>
	 * Returns connected backup server or null if none exists.
	 * </p>
	 * 
	 * @return
	 */
	public BackupCommunicationServers getBackupServer();

	/**
	 * <p>
	 * Tries to register selected component. Function returns id of newly
	 * registered component or returns -1 in case of failure.
	 * </p>
	 * 
	 * @param componentType
	 * @return
	 */
	public BigInteger registerComponent(
			GenericComponent.ComponentType componentType,
			List<String> solvableProblems, Integer port, String address);
}
