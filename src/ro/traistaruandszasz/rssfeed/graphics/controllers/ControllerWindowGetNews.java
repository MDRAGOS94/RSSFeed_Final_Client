package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.client.getnews.DownloadNews;
import ro.traistaruandszasz.rssfeed.client.getnews.NewsFromURL;
import ro.traistaruandszasz.rssfeed.graphics.model.Category;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;

public class ControllerWindowGetNews {
    @FXML
    Button buttonAddNews;
    @FXML
    ListView<String> listViewOfNewsToAdd;
    @FXML
    TextArea textAreaShowNewsContent;
    @FXML ComboBox<String> comboBoxCategories;
    @FXML Button buttonPercent;
    @FXML TextField textFieldKeyword;
    public static String url;
    private List<Category> listOfCategory;
    
    @FXML
    public void initialize() throws IOException {
	//String url = "http://www.ctvnews.ca/world";
	List<NewsFromURL> listOfNews = DownloadNews.getInformationAboutBigNews(url);
	listOfCategory  = new ArrayList<>();
	for (int i = 0; i < listOfNews.size(); i++) {
	    listViewOfNewsToAdd.getItems().add(listOfNews.get(i).getTitle());

	}

	
	listViewOfNewsToAddSETTINGS();
	loadAllCategories();
	comboBoxSettings();
    }

    public void loadAllCategories()
    {
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryLoadingCategory, "", null, false);
	List<String> categoryList = ClientCore.getLastRecivedSocketStringArrayResult();
	
	for (String newsString : categoryList) {
	    String[] sString = newsString.split("#");
	    comboBoxCategories.getItems().add(sString[1]);
	    listOfCategory.add(new Category(Integer.parseInt(sString[0]),sString[1],""));
	}
    }
    
    
    public void listViewOfNewsToAddSETTINGS() {
	listViewOfNewsToAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		NewsFromURL news = DownloadNews.getNewsFromTitle(listViewOfNewsToAdd.getSelectionModel().getSelectedItem());
		try {
		    news.setDescription(DownloadNews.getInformationAboutSmallNews(news.getUrl()));
		} catch (IOException exception) {
		    exception.printStackTrace();
		}
		textAreaShowNewsContent.setText(news.getDescription());
	    }
	});
    }

    @FXML
    public void buttonAddNewsClicked(ActionEvent actionEvent) {
	if (comboBoxCategories.getSelectionModel().getSelectedItem() == null) {
	   /* Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("WARNING !");
	    alert.setHeaderText("ADD NEWS");
	    alert.setContentText("NO CATEGORY SELECTED !");
	    alert.showAndWait();*/
	} else {
	    String categoryName = comboBoxCategories.getSelectionModel().getSelectedItem();
	    int categoryID = getCategoryIdFromName(categoryName);
	    String title = listViewOfNewsToAdd.getSelectionModel().getSelectedItem();
	    String description = textAreaShowNewsContent.getText();
	    Date date = new Date();
	    String data = ControllerWindowClient.getIdClient() + "#" + title + "#" + date + "#" + description + "#null"+"#"+categoryID;
	    ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryInsertNews,data, null, false);
	   // String title = listViewOfNewsToAdd.getItems()
	    
  
	}
    }
    
    public void comboBoxSettings() {
	comboBoxCategories.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) {
	        
	            String categoryName = comboBoxCategories.getSelectionModel().getSelectedItem();
		    int categoryID = getCategoryIdFromName(categoryName);
		    textFieldKeyword.clear();
		    ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryLoadKeywords,Integer.toString(categoryID), null, false);
	            List<String> categoryList = ClientCore.getLastRecivedSocketStringArrayResult();
	            
	            for(String name : categoryList) {
	        	textFieldKeyword.appendText(name+", ");
	            }   
	        }    
	    });
    }
    
    private int getCategoryIdFromName(String name){
	
	for(Category category: listOfCategory) {
	    if(category.getName().compareTo(name) == 0)
		return category.getId();
	}
	return -1;
    }

    public void buttonPercentClicked(ActionEvent actionEvent) {
	
	String[] keywords = textFieldKeyword.getText().split(", ");
	 String text = textAreaShowNewsContent.getText();
	int count = 0;
	for(int i=0;i<keywords.length;i++)
	{
	   if(text.contains(keywords[i]))
	       count++;
	   
	} 
	JOptionPane.showMessageDialog(null,count*100/keywords.length,"PERCENT",JOptionPane.INFORMATION_MESSAGE);
	// System.out.println(count*100/keywords.length);
      }

}
