package ch.vbcmalters.fanapp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CheckPermission {
	
	// All static variables
	private final String URLAdmin = "http://hslu.ath.cx/boli/CheckAdminPermission.php?name=";
	private final String URLAuthor = "http://hslu.ath.cx/boli/CheckAuthorPermission.php?name=";
	  // XML node keys
	private final String KEY_ITEM = "entry"; // parent node
	
	XMLParser parser;
	String xml;

	
	public CheckPermission()
	{
			
	}
	
	public boolean CheckAdminPermission(String accountName)
	{
		String isAdmin = "False";
		
		final String tmpAccountName = accountName;
   		parser = new XMLParser();
   		
    	//String xml = parser.getXmlFromUrl(URL + Integer.toString(commentID)); // getting XML
    	

   		xml = parser.getXmlFromUrl(URLAdmin + "'" + tmpAccountName + "'"); // getting XML

        Document doc = parser.getDomElement(xml); // getting DOM element
        
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) 
        {
        	Element e = (Element) nl.item(i);
            
        	isAdmin = parser.getValue(e, "IsAdmin");
        	

        }
        
        return Boolean.parseBoolean(isAdmin);
        
	}
	
	public boolean CheckAuthorPermission(String accountName)
	{
		String isAuthor = "False";
		
		final String tmpAccountName = accountName;
   		parser = new XMLParser();
   		
    	//String xml = parser.getXmlFromUrl(URL + Integer.toString(commentID)); // getting XML
    	
   		xml = parser.getXmlFromUrl(URLAuthor + "'" + tmpAccountName + "'"); // getting XML

    	
        Document doc = parser.getDomElement(xml); // getting DOM element
        
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) 
        {
        	Element e = (Element) nl.item(i);
            
        	isAuthor = parser.getValue(e, "IsAuthor");
        	
        }
        
        return Boolean.parseBoolean(isAuthor);
        

	}

	
}
