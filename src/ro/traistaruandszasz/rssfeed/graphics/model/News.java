package ro.traistaruandszasz.rssfeed.graphics.model;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class News {
  
    private int id;
    private int idUser;
    private String title;
    private Date date;
    private String description;
    private String source;
    private String categoryName;

    public News(int id, int idUser,String title) {
	this.id = id;
	this.idUser = idUser;
	this.title = title;
	
    }
    public News(int id, int idUser, String title, Date date,String description, String source, String categoryName) {
	this.id = id;
	this.idUser = idUser;
	this.title = title;
	this.date = date;
	this.description = description;
	this.source = source;
	this.setCategoryName(categoryName);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdUser() {
        return idUser;
    }
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getCategoryName() {
	return categoryName;
    }
    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

}
