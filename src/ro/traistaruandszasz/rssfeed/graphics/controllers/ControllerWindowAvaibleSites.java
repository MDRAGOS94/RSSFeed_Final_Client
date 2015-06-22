package ro.traistaruandszasz.rssfeed.graphics.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ControllerWindowAvaibleSites {
    @FXML TextArea textAreaAvaibleSites;
    
    @FXML
    public void initialize() {
	textAreaAvaibleSites.appendText("- http://www.digisport.ro/");
	textAreaAvaibleSites.appendText("\n");
	textAreaAvaibleSites.appendText("- http://www.ctvnews.ca/world/");
	textAreaAvaibleSites.appendText("\n");
	textAreaAvaibleSites.appendText("- http://adevarul.ro/news/");
	textAreaAvaibleSites.appendText("\n");
    }
    
}
