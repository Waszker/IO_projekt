package GenericCommonClasses;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import GenericCommonClasses.GenericComponent.ComponentType;

public abstract class AbstractMessage implements IMessage
{
	/******************/
	/* FUNCTIONS */
	/******************/
	@Override
	public final List<IMessage> prepareResponse(IServerProtocol serverProtocol,
			Socket socket) throws IOException
	{
		List<IMessage> delayedResponse = new ArrayList<>();
		getMessageResponse(serverProtocol, socket, delayedResponse);

		return delayedResponse;
	}

	/**
	 * <p>
	 * Prepares specific message response for given server and sends it via
	 * socket.
	 * </p>
	 * 
	 * @param serverProtocol
	 * @param socket
	 * @param delayedResponse
	 */
	protected abstract void getMessageResponse(IServerProtocol serverProtocol,
			Socket socket, List<IMessage> delayedResponse) throws IOException;

	/**
	 * <p>
	 * Converts string name of component to enumerator.
	 * </p>
	 * 
	 * @param name
	 *            of component
	 * @return enum containing component type
	 */
	protected GenericComponent.ComponentType getComponentType(String name)
	{
		GenericComponent.ComponentType componentType;

		if (name.contentEquals(GenericComponent.ComponentType.TaskManager.name))
			componentType = ComponentType.TaskManager;
		else if (name
				.contentEquals(GenericComponent.ComponentType.ComputationalNode.name))
			componentType = ComponentType.ComputationalNode;
		else if (name
				.contentEquals(GenericComponent.ComponentType.ComputationalServer.name))
			componentType = ComponentType.ComputationalServer;
		else
			componentType = ComponentType.ComputationalClient;

		return componentType;
	}
}
