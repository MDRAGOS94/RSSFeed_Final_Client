package ro.traistaruandszasz.rssfeed.graphics.application;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
	try {
	    ClientCore.connectToServer();
	    Parent root = FXMLLoader.load(getClass().getResource(
		    "/ro/traistaruandszasz/rssfeed/graphics/windows/WindowLogin.fxml"));
	    Scene scene = new Scene(root);
	    primaryStage.getIcons().add(new Image("/ro/traistaruandszasz/rssfeed/graphics/icons/iconWindowLogin.png"));
	    primaryStage.setTitle("LOGIN");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	launch(args);
    }
    
}
