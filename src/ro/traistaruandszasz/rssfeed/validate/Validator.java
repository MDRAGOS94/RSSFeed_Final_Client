package ro.traistaruandszasz.rssfeed.validate;

import java.util.regex.Pattern;

public class Validator {
    
    public static boolean isAcceptedStringInput(String text)
    {
	Pattern p = Pattern.compile("[^a-zA-Z0-9]");
	return !p.matcher(text).find();
    }
    
    public static boolean isAcceptedTextAreaInput(String text)
    {
	if(text.length() > 1)
	    return true;
	else
	    return false;
	
    }
}
