package ro.traistaruandszasz.rssfeed.graphics.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import ro.traistaruandszasz.rssfeed.client.ClientCore;
import ro.traistaruandszasz.rssfeed.graphics.model.Comment;
import ro.traistaruandszasz.rssfeed.graphics.model.News;
import ro.traistaruandszasz.rssfeed.socket.handle.SocketMessageType;

public class ControllerWindowTabNews {
    ControllerWindowClient controllerWindowClient;

    static ObservableList<Comment> comments = FXCollections.observableArrayList();
    static ObservableList<News> news = FXCollections.observableArrayList();
    @FXML Button buttonRefresh;

    @FXML
    TreeView<String> treeViewNews;
    @FXML
    ContextMenu contextMenuTreeViewNews;
    @FXML
    TextField textAreaShowNewsTitle;
    @FXML
    TextArea textAreaShowNewsContent;
    @FXML
    TextArea textAreaAddComment;
    @FXML
    Button buttonAddCommentNews;
    @FXML
    ListView<Comment> listViewShowNewsComments;

    TreeItem<String> treeItemSelected;
    static TreeItem<String> item; 
   
    @FXML 
    public void buttonRefreshClicked(ActionEvent event) {
	news.clear();
	comments.clear();
	listViewShowNewsComments.getItems().clear();
	treeViewNews.getRoot().getChildren().clear();
	loadAllCategoriesAndMyNewsAndComments();
    }
    
    // #################################################################### // INITIALIZE
    @FXML
    public void initialize() {
	listViewShowNewsCommentsSETTINGS();
	contextMenuTreeViewNewsSETTINGS();
	
	loadAllCategoriesAndMyNewsAndComments();
	//updatePane();
	
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
    
    public void init(ControllerWindowClient controllerWindowClient) {
	this.controllerWindowClient = controllerWindowClient;
	
	ListView<String> lista = new ListView<>();
    }

    // #################################################################### SETTINGS AND LOADINGS
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

    public void contextMenuTreeViewNewsSETTINGS() {
	contextMenuTreeViewNews.setStyle("-fx-background-color: white; "
		+ "-fx-padding: 0;");
    }

    public void loadAllCategoriesAndMyNewsAndComments() {
	ClientCore.createAndSendNewSocketMessage(
		SocketMessageType.QueryLoadingCategory, "", null, false);
	List<String> categoryList = ClientCore
		.getLastRecivedSocketStringArrayResult();

	TreeItem<String> root = new TreeItem<String>("CATEGORIES");
	for (String newsString : categoryList) {
	    String[] sString = newsString.split("#");
	    root.getChildren().add(new TreeItem<String>(sString[1]));
	}

	ClientCore.createAndSendNewSocketMessage(
		SocketMessageType.QueryLoadingNews,
		Integer.toString(ControllerWindowClient.getIdClient()), null,
		false);
	List<String> newsList = ClientCore
		.getLastRecivedSocketStringArrayResult();

	root.setExpanded(true);

	for (String newsString : newsList) {
	    String[] sString = newsString.split("#");
	    Date date = new Date();
	    news.add(new News(Integer.parseInt(sString[0]), Integer
		    .parseInt(sString[1]), sString[2], date, sString[4],
		    sString[5], sString[6]));
	}
	treeViewNews.setRoot(root);

	for (News currentNews : news) {
	    for (int i = 0; i < treeViewNews.getRoot().getChildren().size(); i++) {
		if (treeViewNews.getRoot().getChildren().get(i).getValue()
			.compareTo(currentNews.getCategoryName()) == 0) {
		    treeViewNews.getRoot().getChildren().get(i).getChildren()
			    .add(new TreeItem<String>(currentNews.getTitle()));

		}
	    }
	}
	
	listViewShowNewsComments.setItems(comments);
    }

    // #################################################################### EVENTS
    @FXML
    public void treeViewNewsMousePressed(MouseEvent mouseEvent) {
	item = treeViewNews.getSelectionModel()
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
	    textAreaShowNewsTitle.setText(news.getTitle());
	    textAreaShowNewsContent.setText(news.getDescription());
	}
    }

    public void contextMenuDeleteNewsClicked(ActionEvent actionEvent) {
	if (treeItemSelected != null) {
	    if (treeItemSelected.isLeaf() == false
		    && treeItemSelected.getParent() != null) {
		for (int i = 0; i < treeItemSelected.getChildren().size(); i++) {
		    treeViewNews
			    .getRoot()
			    .getChildren()
			    .add(new TreeItem<String>(treeItemSelected
				    .getChildren().get(i).getValue()));

		}
	    }
	    treeItemSelected.getParent().getChildren().remove(treeItemSelected);
	    treeViewNews.getSelectionModel().clearSelection();
	}
    }

    public void buttonAddCommentClicked(ActionEvent actionEvent) {
	
	    int friendId = ControllerWindowClient.getIdClient();
	    int newsID = getNewsIdFromTitle(treeViewNews.getSelectionModel().getSelectedItem().getValue());
	    System.out.println(friendId);
	    if(newsID >=0)
	    {
	    String data = friendId + "#" + newsID + "#" + new Date() + "#" + textAreaAddComment.getText();
	    ClientCore.createAndSendNewSocketMessage(SocketMessageType.QueryInsertComment,data, null, false);
	    }
	    else {
		System.out.println("ID stire invalida! ");
	    }
	
	
    }

    // #################################################################### OTHER METHODS
    @SuppressWarnings("unused")
    private static TreeItem<String> treeViewNewsaddChild(String title,
	    TreeItem<String> parent) {
	TreeItem<String> item = new TreeItem<String>(title);
	item.setExpanded(true);
	parent.getChildren().add(item);

	return item;
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
