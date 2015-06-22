package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerWindowClient {
    private static int idClient;
    
    @FXML ControllerWindowTabNews tabNewsController;
    @FXML ControllerWindowTabFriends tabFriendsController;
    @FXML ControllerWindowTabAdminOnly tabAdminOnlyController;
    
    @FXML TextField textFieldUrlNews;
    @FXML Button buttonSubmitUrl;
    
    @FXML MenuItem menuItemAvaibleSites;
    @FXML Label labelErrorMessage;
    
    @FXML TabPane tabPane;
    
    public static int getIdClient() {
        return idClient;
    }
    public static void setIdClient(int idClient) {
        ControllerWindowClient.idClient = idClient;
    }
    
    @FXML 
    public void initialize() {
	tabNewsController.init(this);
	tabFriendsController.init(this);
	tabAdminOnlyController.init(this);
	updatePane();
	if (idClient!=79) {
	    tabPane.getTabs().get(2).setDisable(true);
	}

 }

    public void updatePane() {
	 tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

	            @Override
	            public void changed(ObservableValue<? extends Tab> arg0,
	                    Tab arg1, Tab arg2) {
	                    
	                 System.out.println("Changed");
	            
	        }});
	
    }
    
    @FXML 
    public void buttonSubmitUrlClicked(ActionEvent actionEvent) throws IOException {	
	if (textFieldUrlNews.getText().equals("http://www.digisport.ro/") == false
		&& textFieldUrlNews.getText().equals("http://adevarul.ro/news/") == false
		&& textFieldUrlNews.getText().equals("http://www.ctvnews.ca/world/") == false) {
	    labelErrorMessage.setText("  ERROR : INVALID URL !");
	    /*
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("WARNING !");
	    alert.setHeaderText("SUBMIT URL");
	    alert.setContentText("INVALID URL !");
	    alert.showAndWait();*/
	    return;
	}
	
	ControllerWindowGetNews.url = textFieldUrlNews.getText();
		
	Parent root = FXMLLoader.load(getClass().getResource("/ro/traistaruandszasz/rssfeed/graphics/windows/WindowGetNews.fxml"));
        Stage stage = new Stage();
        //stage.getIcons().add(new Image("/ro/traistaruandszasz/rssfeed/graphics/icons/iconWindowSignUp.png"));
        stage.setTitle("GET NEWS");
        stage.setScene(new Scene(root));  
        stage.show();
    }

    @FXML
    public void menuItemAvaibleSitesClicked(ActionEvent actionEvent) throws IOException {
	Parent root = FXMLLoader
		.load(getClass()
			.getResource(
				"/ro/traistaruandszasz/rssfeed/graphics/windows/WindowAvaibleSites.fxml"));
	Stage stage = new Stage();
	stage.setTitle("AVAIBLE SITES");
	stage.setScene(new Scene(root));
	stage.show();
    }

    public void menuItemSendMessageClicked(ActionEvent actionEvent) throws IOException {
	Parent root = FXMLLoader
		.load(getClass()
			.getResource(
				"/ro/traistaruandszasz/rssfeed/graphics/windows/WindowSendMessage.fxml"));
	Stage stage = new Stage();
	stage.setTitle("SEND MESSAGE");
	stage.setScene(new Scene(root));
	stage.show();
    }

}
