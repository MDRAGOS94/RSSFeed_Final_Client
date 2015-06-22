package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.graphics.model.Category;
import ro.traistaruandszasz.rssfeed.graphics.model.Comment;
import ro.traistaruandszasz.rssfeed.graphics.model.Message;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;
import ro.traistaruandszasz.rssfeed.validate.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ControllerWindowTabAdminOnly {
    ControllerWindowClient controllerWindowClient;

    @FXML
    ListView<Message> listViewMessages;
    List<Message> messageList;
    
    @FXML Button buttonAddCategoryAdmin;
    @FXML Button buttonAddKeywordToCategory;
    @FXML TextField textFieldCategory;
    @FXML TextField textFieldKeyword;
    @FXML ComboBox<String> comboBoxCategories;
    private List<Category> listOfCategory;
    
    @FXML Tab tabAdminOnly;
    
    @FXML
    public void initialize() {
	messageList = new ArrayList<>();
	listViewShowMessagesSETTINGS();
	loadAllMessages();
	loadAllCategories();
    }
    
    public void listViewShowMessagesSETTINGS() {
	listViewMessages.setBackground(new Background(
		new BackgroundFill(Color.web("#162252"), CornerRadii.EMPTY,
			Insets.EMPTY)));
	listViewMessages
		.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
		    @Override
		    public ListCell<Message> call(ListView<Message> list) {
			final ListCell<Message> cell = new ListCell<Message>() {
			    private Text text;

			    @Override
			    public void updateItem(Message message,
				    boolean empty) {
				super.updateItem(message, empty);
				if (!isEmpty()) {
				    text = new Text(message.toString());
				    text.setFont(Font.font("Comic SANS MS", 14));
				    text.setFill(Color.WHITE);

				    text.setWrappingWidth(listViewMessages
					    .getWidth());
				    setPrefWidth(0);
				    setGraphic(text);
				}
			    }
			};

			return cell;
		    }

		});
    }
    
    public void loadAllMessages() {
	
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryLoadingMessages, "", null, false);
  	List<String> listOfMessages = ClientCore.getLastRecivedSocketStringArrayResult();
  	
  	for(String string : listOfMessages) {
  	    String[] str = string.split("#");
  	    messageList.add(new Message(Integer.parseInt(str[0]),str[1]));
  	  listViewMessages.getItems().add(messageList.get(messageList.size()-1));
  	}
	
    }
    
    public void loadAllCategories()
    {
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryLoadingCategory, "", null, false);
	List<String> categoryList = ClientCore.getLastRecivedSocketStringArrayResult();
	listOfCategory = new ArrayList<>();
	
	for (String newsString : categoryList) {
	    String[] sString = newsString.split("#");
	    comboBoxCategories.getItems().add(sString[1]);
	    listOfCategory.add(new Category(Integer.parseInt(sString[0]),sString[1],""));
	}
    }
    
    @FXML
    public void buttonAddCategoryAdminClicked(ActionEvent actionEvent) {
	
	  String text = textFieldCategory.getText().toString();
	
          if(!Validator.isAcceptedStringInput(text) || !Validator.isAcceptedTextAreaInput(text)) {
                textFieldCategory.setText("INVALID CHARACTERS!");
		return;
	    }
	
	
	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryInsertCategory,textFieldCategory.getText(), null, false);
	textFieldCategory.setText("");
    }
    
    public void buttonAddKeywordToCategoryClicked(ActionEvent actionEvent) {
	
	if (comboBoxCategories.getSelectionModel().getSelectedItem() == null) {
		   /* Alert alert = new Alert(AlertType.WARNING);
		    alert.setTitle("WARNING !");
		    alert.setHeaderText("ADD NEWS");
		    alert.setContentText("NO CATEGORY SELECTED !");
		    alert.showAndWait();*/

		} else {
		    
		    if(!Validator.isAcceptedStringInput(textFieldKeyword.getText()) || !Validator.isAcceptedTextAreaInput(textFieldKeyword.getText()) || !Validator.isAcceptedTextAreaInput(comboBoxCategories.getSelectionModel().getSelectedItem())) {
	        	 textFieldCategory.setText("INVALID CHARACTERS!");
			return;
		    }
		    
		    
		    String categoryName = comboBoxCategories.getSelectionModel().getSelectedItem(); 
		    int categoryID = getCategoryIdFromName(categoryName);
		    String keywordName = textFieldKeyword.getText();
		
		    String data = categoryID + "#" + keywordName;
		    ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryInsertKeyword,data, null, false);
		  
		    textFieldKeyword.setText("");
		}
	
	
    }
    
    private int getCategoryIdFromName(String name){
	
   	for(Category category: listOfCategory) {
   	    if(category.getName().compareTo(name) == 0)
   		return category.getId();
   	}
   	return -1;
       }
    
    public void init(ControllerWindowClient controllerWindowClient) {
	this.controllerWindowClient = controllerWindowClient;
    }
}
