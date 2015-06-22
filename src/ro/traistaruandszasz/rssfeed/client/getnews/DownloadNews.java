package ro.traistaruandszasz.rssfeed.client.getnews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadNews {
    static List<NewsFromURL> listOfNews = new ArrayList<>();

    public static List<NewsFromURL> getInformationAboutBigNews(String url)
	    throws IOException {
	String html = "<html><head></head>"
		+ "<body><p>Parsed HTML into a doc.</p></body></html>";
	Document doc = Jsoup.parse(html);
	doc = Jsoup.connect(url).get();

	HashSet<String> listOfLinks = new HashSet<String>();
	
	String containsString = "";
	if (url.contains("digisport.ro")) {
	    containsString = "digisport.ro/";
	} else {
	    if (url.contains("adevarul.ro")) {
		containsString = "adevarul.ro/";
	    } else {
		if (url.contains("ctvnews.ca")) {
		    containsString = "ctvnews.ca/";
		}
	    }
	}

	Elements links = doc.select("a[href]");
	for (Element link : links) {
	    String linkString = link.attr("abs:href");
	    if (linkString.length() != 0
		    && linkString.contains(containsString)) {
		if (linkString.charAt(linkString.length() - 1) != '/'
			&& linkString.length() > 47) {
		    if (link.attr("title").length() == 0
			    || link.attr("title").length() < 25) {
			continue;
		    }
		    listOfLinks.add("TITLE : " + link.attr("title") + "\n"
			    + linkString);
		}
	    }
	}

	List<String> listOfLinksString = new ArrayList<>(listOfLinks);
	for (int i = 0; i < listOfLinksString.size(); i++) {
	    int index = listOfLinksString.get(i).indexOf("\n");
	    
	    listOfNews.add(new NewsFromURL(listOfLinksString.get(i)
		    .substring(index), listOfLinksString.get(i)
		    .substring(0, listOfLinksString.get(i).indexOf("\n"))));
	}

	return listOfNews;
    }

    public static String getInformationAboutSmallNews(String url) throws IOException {
	String html = "<html><head></head>"
		+ "<body><p>Parsed HTML into a doc.</p></body></html>";
	Document doc = Jsoup.parse(html);
	doc = Jsoup.connect(url).get();

	// DESCRIPTION
	String classString = "";
	if (url.contains("digisport.ro")) {
	    classString = "article";
	} else {
	    if (url.contains("adevarul.ro")) {
		classString = "article-body";
	    } else {
		if (url.contains("ctvnews.ca")) {
		    classString = "articleBody";
		}
	    }
	}
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("\t");
	Elements paragraphs = doc.getElementsByClass(classString);
	for (Element p : paragraphs) {
	    for (int i = 0; i < p.text().length(); i++) {
		if (i != p.text().length() - 1) {
		    if (p.text().charAt(i) == '.'
			    && p.text().charAt(i + 1) == ' ') {
			stringBuilder.append(p.text().charAt(i) + "\n\t");
		    } else {
			stringBuilder.append(p.text().charAt(i));
		    }
		} else {
		    stringBuilder.append(p.text().charAt(i));
		}
	    }
	}	
	
	System.out.println(stringBuilder.length());
	if (stringBuilder.length() > 10) {
	    if (stringBuilder.charAt(stringBuilder.length() - 1) != '.') {
		stringBuilder.append('.');
	    }
	} else {
	    stringBuilder.append("NO DESCRIPTION AVAIBLE !");
	}
	
	return stringBuilder.toString();
    }

    public static NewsFromURL getNewsFromTitle(String title) {
	for (int i = 0; i < listOfNews.size(); i++) {
	    if (listOfNews.get(i).getTitle().equals(title) == true) {
		return listOfNews.get(i);
	    }
	}

	return null;
    }
    
}
