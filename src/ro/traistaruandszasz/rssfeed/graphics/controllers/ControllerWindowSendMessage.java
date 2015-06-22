package ro.traistaruandszasz.rssfeed.graphics.controllers;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;
import ro.traistaruandszasz.rssfeed.validate.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerWindowSendMessage {

    @FXML TextArea textAreaMessage;
    @FXML Button buttonSendMessage;
    
    @FXML
    public void initialize() {
	
    }
    
    @FXML
    public void buttonSendMessageClicked(ActionEvent actionEvent) {
	
	String message = textAreaMessage.getText();
	
	if(!Validator.isAcceptedStringInput(message) || !Validator.isAcceptedTextAreaInput(message)) {
	    textAreaMessage.setText("MESSAGE IS NULL");
	    return;
	}
	
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QuerySendMessage, message, null,false);
	Stage stageCurrent = (Stage) buttonSendMessage.getScene().getWindow();
	stageCurrent.close();
    }
}
