package ro.traistaruandszasz.rssfeed.client.getnews;

public class NewsFromURL {
    private String url;
    private String title;
    private String description;
    
    public NewsFromURL(String url, String title) {
	this.url = url;
	this.title = title;
    }
    
    public String getUrl() {
	return url;
    }
    public void setUrl(String url) {
	this.url = url;
    }
    public String getTitle() {
	return title;
    }
    public void setTitle(String title) {
	this.title = title;
    }
    public String getDescription() {
	return description;
    }
    public void setDescription(String description) {
	this.description = description;
    }
    
}
