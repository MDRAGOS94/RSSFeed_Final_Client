package ro.traistaruandszasz.rssfeed.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import ro.traistaruandszasz.rssfeed.socket.handle.SendAndReceiveMessagePacket;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessage;

public class ClientListener extends Thread {
    private ObjectInputStream objectInputStream;
    private SendAndReceiveMessagePacket sendAndReceiveMessagePacket;
    private SocketMessage messageFromServer;

    public ClientListener(ObjectInputStream objectInputStream,
	    SendAndReceiveMessagePacket sendAndReceiveMessagePacket) {
	this.objectInputStream = objectInputStream;
	this.sendAndReceiveMessagePacket = sendAndReceiveMessagePacket;
    }

    public void run() {
	try {
	    while ((messageFromServer = (SocketMessage) objectInputStream.readObject()) != null)
		sendAndReceiveMessagePacket.receiveNewMessageFromServer(messageFromServer);
	} catch (ClassNotFoundException exception) {
	    exception.printStackTrace();
	} catch (IOException exception) {
	    exception.printStackTrace();
	}
    }
    
}
