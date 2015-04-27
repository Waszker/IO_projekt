package GenericCommonClasses;

import java.math.BigInteger;
import java.util.List;

import GenericCommonClasses.GenericComponent.ComponentType;
import XMLMessages.NoOperation;
import XMLMessages.RegisterResponse.BackupCommunicationServers;
import XMLMessages.Solutiones.Solutions.Solution;

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
	 * Adds message for backup server's state synchronization.
	 * </p>
	 * 
	 * @param message
	 *            for synchronization
	 */
	public void addBackupServerMessage(IMessage message);

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

	/**
	 * <p>
	 * Gets component enum type from given id.
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public ComponentType getComponentTypeFromId(BigInteger id);

	/**
	 * <p>
	 * Returns filled NoOperation message containing information about backup
	 * server.
	 * </p>
	 * 
	 * @return
	 */
	public NoOperation getNoOperationMessage();

	/**
	 * <p>
	 * Returns response messages for Status message.
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public List<IMessage> getStatusResponseMessages(BigInteger id,
			int freeThreads);

	/**
	 * <p>
	 * Returns messages required in backup sever synchronization.
	 * </p>
	 * 
	 * @return
	 */
	public List<IMessage> getBackupServerSynchronizationMessages();

	/**
	 * <p>
	 * Returns solution of computed problem. If there's no final solution yet
	 * empty solution with type "Ongoing" is returned.
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public Solution getProblemSolution(BigInteger id);

	/**
	 * <p>
	 * Registers problem instance and returns its id.
	 * </p>
	 * 
	 * @param data
	 * @param problemType
	 * @param solvingTimeout
	 */
	public BigInteger registerProblem(byte[] data, String problemType,
			BigInteger solvingTimeout);

	/**
	 * <p>
	 * Sets information about problem after its division.
	 * </p>
	 * 
	 * @param id
	 * @param commonData
	 * @param numberOfParts
	 */
	public void setProblemPartsInfo(BigInteger id, byte[] commonData,
			int numberOfParts);

	/**
	 * <p>
	 * Removes message from TaskManager that held information about problem with
	 * specific id.
	 * </p>
	 * 
	 * @param problemId
	 */
	public void removeTaskManagerSpecificMessage(BigInteger problemId);

	/**
	 * <p>
	 * Removes message from ComputationalNode that held information about
	 * specific partial problem.
	 * </p>
	 * 
	 * @param problemId
	 * @param taskId
	 */
	public void removeComputationalNodeSpecificMessage(BigInteger problemId,
			BigInteger taskId);

	/**
	 * <p>
	 * Saves problem (partial) solution and returns list of all partial
	 * solutions that should be merged. Otherwise (if received solution was
	 * final or not all partial solutions are yet available) returns null.
	 * </p>
	 * 
	 * @param problemId
	 * @param solution
	 * @return
	 */
	public List<Solution> informAboutProblemSolution(BigInteger problemId,
			Solution solution);

	/**
	 * <p>
	 * Informs about component status message and refreshes its lease time.
	 * </p>
	 * 
	 * @param componentId
	 */
	public void informAboutComponentStatusMessage(BigInteger componentId);
}
