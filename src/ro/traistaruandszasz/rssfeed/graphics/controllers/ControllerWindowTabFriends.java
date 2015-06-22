package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.graphics.model.Comment;
import ro.traistaruandszasz.rssfeed.graphics.model.News;
import ro.traistaruandszasz.rssfeed.graphics.model.User;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ControllerWindowTabFriends {
    ControllerWindowClient controllerWindowClient;

    @FXML
    ListView<String> listViewFriends;
    @FXML
    TreeView<String> treeViewFriendsNews;
    @FXML
    TextField textAreaShowFriendsNewsTitle;
    @FXML
    TextArea textAreaShowFriendsNewsContent;
    @FXML
    TextArea textAreaFriendsAddComment;
    @FXML
    Button buttonAddFriendsCommentNews;
    @FXML
    ListView<Comment> listViewShowNewsComments;
    List<User> listOfAllUsers;
    List<String> categoryList;
    static ObservableList<Comment> comments = FXCollections.observableArrayList();
    static ObservableList<News> news = FXCollections.observableArrayList();
   @FXML
    Button buttonAcceptFriendRequest;
   @FXML
    Button buttonAddFriendCommentNews;
   @FXML
    Button buttonSendFriendRequest;
    TreeItem<String> root;
    static TreeItem<String> item;
    
    @FXML Button buttonBlockUser;
    
    @FXML 
    public void buttonBlockUserClicked(ActionEvent actionEvent) {
	int selectedUserID = getUserIdFromName(listViewFriends.getSelectionModel().getSelectedItem());
	String message = ControllerWindowClient.getIdClient() + "#"+selectedUserID;	
        ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryFriendshipSetRestriction, message, null,false);
        buttonBlockUser.setDisable(true);
    }
    
    public static synchronized void updateCommentList(String comment) {
	if (item != null) {
	    //comments.clear();
          int id = getNewsIdFromTitle(item.getValue());
	    if (id >= 0) {
		
		String[] stringArray = comment.split("#");
		
		 if(id == Integer.parseInt(stringArray[1])) {
		     Comment newComment = new Comment(0,Integer.parseInt(stringArray[0]),id,new Date(),stringArray[3]);
		     newComment.setNameFromIdFriend(stringArray[4]);
		     comments.add(0,newComment);
		  }
	    }
	}
    }
    
    @FXML
    public void initialize() {
	//buttonAddFriendCommentNews.setDisable(true);
	
	loadAllFriends();
	loadAllCategories();
	listViewFriendsSettings();
	listViewShowNewsCommentsSETTINGS();
	buttonAcceptFriendRequest.setDisable(true);
	
    }

    public void loadAllFriends() {

	ClientCore.createAndSendNewSocketMessage(
		SocketMessageType.QueryLoadingAllUsers, "", null, false);
	List<String> friendList = ClientCore
		.getLastRecivedSocketStringArrayResult();
	listOfAllUsers = new ArrayList<>();
	for (String string : friendList) {
	    int id = Integer.parseInt(string.split("#")[0]);
	    
	    if(id == ControllerWindowClient.getIdClient())
		continue;
	    String name = string.split("#")[1];
	    listOfAllUsers.add(new User(id, name));
	    listViewFriends.getItems().add(name);
	}

    }

    public void loadAllCategories() {
	ClientCore.createAndSendNewSocketMessage(
  		SocketMessageType.QueryLoadingCategory, "", null, false);
  	categoryList = ClientCore.getLastRecivedSocketStringArrayResult();
  	
  	root = new TreeItem<String>("CATEGORIES");
  	for (String newsString : categoryList) {
  	    String[] sString = newsString.split("#");
  	    root.getChildren().add(new TreeItem<String>(sString[1]));
  	}
  	treeViewFriendsNews.setRoot(root);
    }
    
    public void listViewFriendsSettings() {
	listViewFriends.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {

	  int selectedUserID = getUserIdFromName(listViewFriends.getSelectionModel().getSelectedItem());
	  String message = ControllerWindowClient.getIdClient() + "#"+selectedUserID;	
	  ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryFriendshipExists, message, null,false);
	
	   boolean isFriend = ClientCore.getLastRecivedSocketBooleanResult();
		
	   for (int i = 0; i < treeViewFriendsNews.getRoot().getChildren().size(); i++) {
		treeViewFriendsNews.getRoot().getChildren().get(i).getChildren().clear();
	   }
	     if(isFriend) {
	  	 loadAllCategoriesAndMyNewsAndComments(selectedUserID);
	  	 buttonAcceptFriendRequest.setDisable(true);
	  	 buttonSendFriendRequest.setDisable(true);
	  	 
	  	buttonBlockUser.setDisable(false);
	  	
	  	// ########################################################################################################
	  	ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryFriendshipRestriction, message, null,false);
	 	
		   boolean isRestricted = ClientCore.getLastRecivedSocketBooleanResult();
	  	  
		   System.out.println("BOOLEAN " + isRestricted);
	  	   if(isRestricted) {
	  	       
	  	     buttonAddFriendsCommentNews.setDisable(true);
	  	   }
	  	   else {
	  	     buttonAddFriendsCommentNews.setDisable(false);
	  	   }
	  	 
	     }
	     else{
		 
		 buttonBlockUser.setDisable(true);
		
		  ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryExistsFriendRequest, message, null,false);
		   boolean exists = ClientCore.getLastRecivedSocketBooleanResult();
		  if(exists) {
		      buttonAcceptFriendRequest.setDisable(false);
		      buttonSendFriendRequest.setDisable(true);
		  }
		  else {
		      buttonAcceptFriendRequest.setDisable(true);
		      buttonSendFriendRequest.setDisable(false);
		  }
		      
	     }
	    }
	});
    }

    public void listViewShowNewsCommentsSETTINGS() {
	listViewShowNewsComments.setBackground(new Background(
		new BackgroundFill(Color.web("#162252"), CornerRadii.EMPTY,
			Insets.EMPTY)));
	listViewShowNewsComments
		.setCellFactory(new Callback<ListView<Comment>, ListCell<Comment>>() {
		    @Override
		    public ListCell<Comment> call(ListView<Comment> list) {
			final ListCell<Comment> cell = new ListCell<Comment>() {
			    private Text text;

			    @Override
			    public void updateItem(Comment comment,
				    boolean empty) {
				super.updateItem(comment, empty);
				if (!isEmpty()) {
				    text = new Text(comment.toString());
				    text.setFont(Font.font("Comic SANS MS", 14));
				    text.setFill(Color.WHITE);

				    text.setWrappingWidth(listViewShowNewsComments
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
    
    public void loadAllCategoriesAndMyNewsAndComments(int userID) {
  	
     root = treeViewFriendsNews.getRoot();
  	ClientCore.createAndSendNewSocketMessage(
  		SocketMessageType.QueryLoadingNews,
  		Integer.toString(userID), null,
  		false);
  	List<String> newsList = ClientCore
  		.getLastRecivedSocketStringArrayResult();

  	root.setExpanded(true);
        news.clear();
   	
  	for (String newsString : newsList) {
  	    String[] sString = newsString.split("#");
  	    Date date = new Date();
  	    news.add(new News(Integer.parseInt(sString[0]), Integer
  		    .parseInt(sString[1]), sString[2], date, sString[4],
  		    sString[5], sString[6]));
  	}
  	treeViewFriendsNews.setRoot(root);

  	for (News currentNews : news) {
  	    for (int i = 0; i < treeViewFriendsNews.getRoot().getChildren().size(); i++) {
  		if (treeViewFriendsNews.getRoot().getChildren().get(i).getValue()
  			.compareTo(currentNews.getCategoryName()) == 0) {
  		  treeViewFriendsNews.getRoot().getChildren().get(i).getChildren()
  			    .add(new TreeItem<String>(currentNews.getTitle()));

  		}
  	    }
  	}
  	
  	listViewShowNewsComments.setItems(comments);
      }

    @FXML
    public void treeViewFriendsNewsMousePressed(MouseEvent mouseEvent) {
	item= treeViewFriendsNews.getSelectionModel()
		.getSelectedItem();
	if (item != null) {
	    comments.clear();

	    int id = getNewsIdFromTitle(item.getValue());
	    if (id >= 0) {
		// SERVER-CLIENT
		ClientCore.createAndSendNewSocketMessage(
			SocketMessageType.QueryLoadingComment,
			Integer.toString(id), null, false);
		List<String> commentList = ClientCore
			.getLastRecivedSocketStringArrayResult();
		
		for (String newsString : commentList) {
		    String[] sString = newsString.split("#");
		    Date date = new Date();
		    Comment comment = new Comment(Integer.parseInt(sString[0]),
			    Integer.parseInt(sString[1]),
			    Integer.parseInt(sString[2]), date, sString[4]);
		    comment.setNameFromIdFriend(sString[5]);
		    comments.add(comment);
		}
	    }

	    News news = getNewsByTitle(item.getValue());
	    if(news !=null)
	    {
	    System.out.println(news.getDescription());
	    textAreaShowFriendsNewsTitle.setText(news.getTitle());
	    textAreaShowFriendsNewsContent.setText(news.getDescription());
	    }
	}
    }
    
    public void buttonAcceptFriendRequestClicked(ActionEvent actionEvent) {
	 int selectedUserID = getUserIdFromName(listViewFriends.getSelectionModel().getSelectedItem());
	  String message = ControllerWindowClient.getIdClient() + "#"+selectedUserID;	
	  ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryAcceptFriendRequest, message, null,false);
	  buttonAcceptFriendRequest.setDisable(true);
    }
    
    public void buttonAddFriendsCommentNewsClicked(ActionEvent actionEvent) {
    
	  int friendId = ControllerWindowClient.getIdClient();
	    int newsID = getNewsIdFromTitle(treeViewFriendsNews.getSelectionModel().getSelectedItem().getValue());
	    
	    if(newsID >=0)
	    {
	    String data = friendId + "#" + newsID + "#" + new Date() + "#" + textAreaFriendsAddComment.getText();
	    ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryInsertComment,data, null, false);
	    }
	    else {
		System.out.println("ID stire invalida! ");
	    }
	
	
	
    }
    
    public void buttonSendFriendRequestClicked(ActionEvent actionEvent) {
	  int selectedUserID = getUserIdFromName(listViewFriends.getSelectionModel().getSelectedItem());
	  String message = ControllerWindowClient.getIdClient() +"#"+ selectedUserID;	
	  ClientCore.createAndSendNewSocketMessage(SocketMessageType.QuerySendFriendRequest, message, null,false);
	  buttonAcceptFriendRequest.setDisable(true);
	  buttonSendFriendRequest.setDisable(true);
    
    }
    
    public void init(ControllerWindowClient controllerWindowClient) {
	this.controllerWindowClient = controllerWindowClient;
    }

    public int getUserIdFromName(String name) {
	for (User user : listOfAllUsers) {
	    if (user.getName() == name)
		return user.getId();
	}
      return -1;
    }
   
    private static int getNewsIdFromTitle(String title) {
	for (News nextNews : news) {
	    if (nextNews.getTitle().compareTo(title) == 0) {
		return nextNews.getId();
	    }
	}

	return -1;
    }
    
    private News getNewsByTitle(String title) {
	for (News nextNews : news) {
	    if (nextNews.getTitle().compareTo(title) == 0) {
		return nextNews;
	    }
	}

	return null;
    }
  
 }

