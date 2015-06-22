package ro.traistaruandszasz.rssfeed.socket.handle;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.client.ClientSender;
import ro.traistaruandszasz.rssfeed.graphics.controllers.ControllerWindowTabFriends;
import ro.traistaruandszasz.rssfeed.graphics.controllers.ControllerWindowTabNews;

public class SendAndReceiveMessagePacket {
    private ClientSender clientSender;

    public SendAndReceiveMessagePacket(ClientSender clientSender) {
	this.clientSender = clientSender;
    }

    public void sendNewMesageToServer(SocketMessage socketMessage) {
	clientSender.sendMessageToServer(socketMessage);
    }

    public void receiveNewMessageFromServer(SocketMessage socketMessage) {
	//System.out.println(socketMessage.getMessageToServer());
	//System.out.println(socketMessage.getListMessageToServer().size());
	SocketMessageType messageType = socketMessage.getSocketMessageType();
	
	
	switch(messageType) {
	
	case QueryUpdateCommentList:
	    ControllerWindowTabFriends.updateCommentList(socketMessage.getMessageToServer());
	    ControllerWindowTabNews.updateCommentList(socketMessage.getMessageToServer());
	default:
	    ClientCore.setLastSocketMessage(socketMessage);
	 break;  
	
	}
	
	}
	
    }


