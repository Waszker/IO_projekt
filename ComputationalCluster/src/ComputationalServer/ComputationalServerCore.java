package ComputationalServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * ComputationalServerCore class consists of two threads named Messaging
 * Processing Module and Messaging Queuing Module. Its main purpose is to
 * provide way to communicate with other components (modules) required to
 * successfully solve problems specified by ComputationalClient.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 *
 */
public class ComputationalServerCore
{
	/******************/
	/* VARIABLES */
	/******************/
	private int port;
	private Socket connectionSocket;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Starts listening for messages on certain port.
	 * </p>
	 * 
	 * @param port
	 *            on which messages are expected
	 * @throws IOException 
	 */
	public void startListening(int port) throws IOException
	{
		this.port = port;

		ServerSocket socket = new ServerSocket(port);
		connectionSocket = socket.accept();
		System.out.println("Connection estabilished!");
		socket.close(); // TODO: This should probably not be here!		
	}
	
//	Thing that not work at the time:
//	private void listenForMessages() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {					
//					while(true) 
//					{
//						ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
//						Message message;
//						if((message = (Message)inFromServer.readObject())!= null)
//						{
//							if(message.getMessageType() == Message.Type.Text)
//							{
//								System.out.println(message.getMessageContent());
//							}
//							else
//							{
//								Files.write(Paths.get(message.getMessageContent()), message.getFileContent(), StandardOpenOption.CREATE);
//							}
//						}
//						Thread.sleep(100);
//					}
//					
//				} catch (IOException | InterruptedException | ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
//	}
//	
//	private void sendMessage(Message message) {
//		try {
//			ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
//			outToClient.writeObject(message);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}

}
