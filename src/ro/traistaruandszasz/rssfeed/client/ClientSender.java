package ro.traistaruandszasz.rssfeed.client;

import java.io.IOException;
import java.io.ObjectOutputStream;

import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessage;

public class ClientSender {
    private ObjectOutputStream objectOutputStream;

    public ClientSender(ObjectOutputStream objectOutputStream) {
	this.objectOutputStream = objectOutputStream;
    }

    public void sendMessageToServer(SocketMessage socketMessage) {
	try {
	    objectOutputStream.writeObject(socketMessage);
	    //objectOutputStream.flush();
	} catch (IOException exception) {
	    exception.printStackTrace();
	}
    }
    
}
