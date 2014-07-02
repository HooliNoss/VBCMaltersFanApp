package ch.vbcmalters.fanapp;

import org.droidsoapclient.client.SoapClient;

public class SOAPConnection2 
{
	
	public String getSoapClient()
	{
		String resposta = "";
	    try{                            
	            
	            /***********DROIDSOAPCLIENT CALL - START**********/
	            SoapClient client = new SoapClient("http://myvolley.swissvolley.ch/getGamesTeam", "getGamesTeam", "http://schemas.xmlsoap.org/soap/encoding/", 
	                            "http://80.74.154.73/SwissVolley.wsdl",false); //create cliente
	            client.addParameter("team_ID", 17481); //set parameters
	            //client.addParameter("valor2", num2);
	            String resp = (String) client.executeCallResponse(); //get response
	            /***********DROIDSOAPCLIENT CALL - FINISH**********/
	            
	            resposta = resp;
	            
	            
	    }catch(Exception e){
	            resposta = "dini muetter";
	    }
	    
	    return resposta;
	}

}
