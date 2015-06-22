package ro.traistaruandszasz.rssfeed.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.traistaruandszasz.rssfeed.socket.handle.SendAndReceiveMessagePacket;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessage;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;



public  class ClientCore {
  
    private static final String SERVER_HOSTNAME = "localhost"; // "25.32.112.231";
    private static final int SERVER_PORT = 2002;
    private static SendAndReceiveMessagePacket messageHandle;
    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static SocketMessage lastSocketMessageRecived;
    private static long timeOutTime_ms = 10000;
    
    
    public static void createAndSendNewSocketMessage(SocketMessageType socketType, String stringMessage,List<String> listStringMessage, boolean booleanMessage) {
	
	SocketMessage socketMessage = new SocketMessage();
	socketMessage.setSocketMessageType(socketType);
	socketMessage.setMessageToServer(stringMessage);
	socketMessage.setBooleanToServer(booleanMessage);
	socketMessage.setListMessageToServer(listStringMessage);
	messageHandle.sendNewMesageToServer(socketMessage);
	lastSocketMessageRecived = null;
	socketMessage = null;
   }
    public static void setLastSocketMessage(SocketMessage socketMessage) {
	lastSocketMessageRecived = socketMessage;
    }
    public static boolean getLastRecivedSocketBooleanResult() {
	
	long time = System.currentTimeMillis();
	
	while(lastSocketMessageRecived == null) {
	    if(System.currentTimeMillis()-time > timeOutTime_ms) {
		System.out.println("Timed out waiting the server to respond !");
		return false;
	    }
		try {
		    TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	  return lastSocketMessageRecived.isBooleanToServer();	
    }
    public static String getLastRecivedSocketStringResult() {
	long time = System.currentTimeMillis();
	
	while(lastSocketMessageRecived == null) {
	    if(System.currentTimeMillis()-time > timeOutTime_ms) {
		System.out.println("Timed out waiting the server to respond !");
		return "";
	    }
		try {
		    TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	  return lastSocketMessageRecived.getMessageToServer();	
    }
    public static List<String>getLastRecivedSocketStringArrayResult() {
	long time = System.currentTimeMillis();
	
	while(lastSocketMessageRecived == null) {
	    if(System.currentTimeMillis()-time > timeOutTime_ms) {
		System.out.println("Timed out waiting the server to respond !");
		return null;
	    }
		try {
		    TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	  return lastSocketMessageRecived.getListMessageToServer();	
    }
    public static SendAndReceiveMessagePacket getMessageHandle() {
        return messageHandle;
    }
    public static void setMessageHandle(SendAndReceiveMessagePacket messageHandle) {
        ClientCore.messageHandle = messageHandle;
    }
    public static Socket getSocket() {
        return socket;
    }
    public static void setSocket(Socket socket) {
        ClientCore.socket = socket;
    }
    public static ObjectOutputStream getOutputStream() {
        return outputStream;
    }
    public static void setOutputStream(ObjectOutputStream outputStream) {
        ClientCore.outputStream = outputStream;
    }
    public static ObjectInputStream getInputStream() {
        return inputStream;
    }
    public static void setInputStream(ObjectInputStream inputStream) {
        ClientCore.inputStream = inputStream;
    }
    public static String getServerHostname() {
        return SERVER_HOSTNAME;
    }
    public static int getServerPort() {
        return SERVER_PORT;
    }


    public static void connectToServer() {
	try {
	    socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
	    outputStream = new ObjectOutputStream(socket.getOutputStream());
	    inputStream = new ObjectInputStream(socket.getInputStream());

	    System.out.println("Connected to server " + SERVER_HOSTNAME + ":"
		    + SERVER_PORT);

	    // Create and start Sender and Listener threads.
	    ClientSender sender = new ClientSender(outputStream);
	    messageHandle = new SendAndReceiveMessagePacket(sender);
	    ClientListener listener = new ClientListener(inputStream, messageHandle);
	    listener.setDaemon(true);
	    listener.start();
	} catch (IOException exception) {
	    System.err.println("Can not establish connection to "
		    + SERVER_HOSTNAME + ":" + SERVER_PORT);
	    exception.printStackTrace();
	    System.exit(-1);
	}
    }

    
}
