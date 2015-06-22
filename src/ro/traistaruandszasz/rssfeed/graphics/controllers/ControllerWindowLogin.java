package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;

public class ControllerWindowLogin implements Initializable {
    @FXML
    Label labelWelcomeLogin;
    @FXML
    Label labelNickLogin;
    @FXML
    Label labelPasswordLogin;
    @FXML
    TextField textFieldNickLogin;
    @FXML
    TextField passwordFieldPasswordLogin;
    @FXML
    Button buttonLogin;
    @FXML
    Button buttonSignUp;
    @FXML
    Label labelErrorLoginMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	System.out.println("A NEW LOGIN WINDOW IS NOW OPENED !");
	hotKey();
    }

    @FXML
    public void buttonLoginClicked(ActionEvent actionEvent) throws IOException {
	String userName = textFieldNickLogin.getText();
	String userPassword = passwordFieldPasswordLogin.getText();

	// CLIENT-SERVER
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.Logging,
		userName + "," + userPassword, null, false);
	int idClient = Integer.parseInt(ClientCore
		.getLastRecivedSocketStringResult());

	// IF LOGIN WAS SUCCESSFUL OR NOT
	if (idClient != -1) {
	    System.out.println("THE LOGIN WAS A SUCCESS WITH ID : " + idClient
		    + " !");
	    ControllerWindowClient.setIdClient(idClient);

	    // CLOSE LOGIN INTERFACE
	    Stage stageCurrent = (Stage) buttonLogin.getScene().getWindow();
	    stageCurrent.close();

	    // OPEN CLIENT INTERFACE
	    Parent root = FXMLLoader
		    .load(getClass()
			    .getResource(
				    "/ro/traistaruandszasz/rssfeed/graphics/windows/WindowClient.fxml"));
	    Stage stage = new Stage();
	    stage.setScene(new Scene(root));
	    stage.show();

	} else {
	    labelErrorLoginMessage.setText("INVALID !");
	}
    }

    @FXML
    public void buttonSignUpClicked(ActionEvent actionEvent) throws IOException {
	Stage stageCurrent = (Stage) buttonSignUp.getScene().getWindow();
	stageCurrent.close();

	Parent root = FXMLLoader
		.load(getClass()
			.getResource(
				"/ro/traistaruandszasz/rssfeed/graphics/windows/WindowSignUp.fxml"));
	Stage stage = new Stage();
	stage.setTitle("REGISTER");
	stage.setScene(new Scene(root));
	stage.show();
    }

    private void hotKey() {  
    
      buttonLogin.getParent().setOnKeyReleased(new EventHandler<KeyEvent>()
	        {
	            public void handle(KeyEvent keyEvent)
	            {
	        	if(keyEvent.getCode() == KeyCode.ENTER){
	        	    try {
				buttonLoginClicked(new ActionEvent());
			    } catch (IOException e) {
				e.printStackTrace();
			    }
	        	}
	            }
	        });
    }
 
}
