package ro.traistaruandszasz.rssfeed.graphics.model;

import java.util.Date;

public class Comment {
    private int id;
    private int idNews;
    private int idFriend;
    private Date date;
    private String text;
    private String nameFromIdFriend;

    public Comment(Date date, String text) {
	this.date = date;
	this.text = text;
    }

    public Comment(int id, int idFriend, int idNews, Date date, String text) {
	this.id = id;
	this.idNews = idNews;
	this.idFriend = idFriend;
	this.date = date;
	this.text = text;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getIdNews() {
	return idNews;
    }

    public void setIdNews(int idNews) {
	this.idNews = idNews;
    }

    public int getIdFriend() {
	return idFriend;
    }

    public void setIdFriend(int idFriend) {
	this.idFriend = idFriend;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getNameFromIdFriend() {
	return nameFromIdFriend;
    }

    public void setNameFromIdFriend(String nameFromIdFriend) {
	this.nameFromIdFriend = nameFromIdFriend;
    }

    @Override
    public String toString() {
	return "USER NAME : " + nameFromIdFriend + ", DATA : " + date + "\n"
		+ text;
    }

}
