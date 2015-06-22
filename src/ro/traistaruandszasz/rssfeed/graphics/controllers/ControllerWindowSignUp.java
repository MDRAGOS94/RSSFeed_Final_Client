package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;
import ro.traistaruandszasz.rssfeed.validate.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerWindowSignUp implements Initializable {
    @FXML
    Label labelRegister;
    @FXML
    Label labelNickRegister;
    @FXML
    Label labelPasswordRegister;
    @FXML
    Label labelRepeatPassRegister;
    @FXML
    TextField textFieldNickRegister;
    @FXML
    PasswordField passwordFieldPasswordRegister;
    @FXML
    PasswordField passwordFieldRepeatPassRegister;
    @FXML
    Button buttonRegister;
    @FXML
    Button buttonBackToLogin;
    @FXML
    Label labelErrorRegisterMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	System.out.println("A NEW SIGN UP WINDOW IS NOW OPENED !");
    }

    @FXML
    public void buttonRegisterClicked(ActionEvent actionEvent)
	    throws IOException {
	if (passwordFieldPasswordRegister.getText().equals(
		passwordFieldRepeatPassRegister.getText()) == true) {
	    String userName = textFieldNickRegister.getText();
	    String userPassword = passwordFieldPasswordRegister.getText();

	    if(!Validator.isAcceptedStringInput(userName) || !Validator.isAcceptedStringInput(userPassword)) {
		labelErrorRegisterMessage.setText("INVALID CHARACTERS!");
		return;
	    }
	    
	    ClientCore.createAndSendNewSocketMessage(
		    SocketMessageType.Register, userName + "," + userPassword,
		    null, false);

	    // IF REGISTER WAS SUCCESSFUL OR NOT
	    if (ClientCore.getLastRecivedSocketBooleanResult()) { 
		// CLOSE REGISTER INTERFACE
		Stage stageCurrent = (Stage) buttonBackToLogin.getScene()
			.getWindow();
		stageCurrent.close();

		// OPEN LOGIN INTERFACE
		Parent root = FXMLLoader
			.load(getClass()
				.getResource(
					"/ro/traistaruandszasz/rssfeed/graphics/windows/WindowLogin.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	    } else {
		labelErrorRegisterMessage.setText("INVALID !");
	    }
	} else {
	    labelErrorRegisterMessage.setText("INVALID !");
	}
    }

    @FXML
    public void buttonBackToLoginClicked(ActionEvent actionEvent)
	    throws IOException {
	Stage stageCurrent = (Stage) buttonBackToLogin.getScene().getWindow();
	stageCurrent.close();

	Parent root = FXMLLoader
		.load(getClass()
			.getResource(
				"/ro/traistaruandszasz/rssfeed/graphics/windows/WindowLogin.fxml"));
	Stage stage = new Stage();
	stage.setScene(new Scene(root));
	stage.show();
    }

}
