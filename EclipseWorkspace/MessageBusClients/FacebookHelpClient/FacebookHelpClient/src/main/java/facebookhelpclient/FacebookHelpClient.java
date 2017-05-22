package facebookhelpclient;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hermes.messagebus.client.library.Client;
import hermes.messagebus.client.library.MessageReceiver;


public class FacebookHelpClient implements MessageReceiver {
	
	Client clientThread;
	private static final String TAGS = "HELP_MESSAGE|RETURN_FILE_DATA";

	public static void main(String[] args) {
		new FacebookHelpClient().go();
	}
	
	private void go() {
	    clientThread = new Client(TAGS, this);	    
	}
	
	protected void postFileLessMessage(JSONObject aReceivedMessage) {
		String aFacebookPostString = 
				aReceivedMessage.getString("help");
			     			
		JSONObject toFacebook = new JSONObject();

		toFacebook.put("postMessage", aFacebookPostString);						
				
		JSONArray tags = new JSONArray();
		tags.put("FACEBOOK_POST");
		toFacebook.put("tags", tags);				
		clientThread.sendMessage(toFacebook.toString());
	}
	
	public void receiveHelpMessage(JSONObject aReceivedMessage) {
		try {
			
			JSONObject toGoogle = new JSONObject();
			try {
				String aFileName =  aReceivedMessage.getString("filename");
				if (aFileName.equals("")) {
					postFileLessMessage(aReceivedMessage);
					return;
				} 
				toGoogle.put("callbackTag", "RETURN_FILE_DATA");
				toGoogle.put("filename", aFileName);
				toGoogle.put("from", aReceivedMessage.getString("from"));
				JSONArray tags = new JSONArray();
				tags.put("FILE_DATA_REQUEST");
				toGoogle.put("tags", tags);
				toGoogle.put("source", aReceivedMessage.getString("help"));
				clientThread.sendMessage(toGoogle.toString());

			} catch (JSONException ex) {}
		
			
			
		} catch (Exception ex) {
			//Received invalid JSON
		}
		
	}
	public void receiveReturnFileData(JSONObject aReceivedMessage) {
		try {
			
			JSONObject toFacebook = new JSONObject();
			try {
				String aFacebookPostString = 
					aReceivedMessage.getString("source") + "\n" +
				     "File URL: " +
					aReceivedMessage.getString("fileUrl");			
						
				toFacebook.put("postMessage", aFacebookPostString);						
						
				JSONArray tags = new JSONArray();
				tags.put("FACEBOOK_POST");
				toFacebook.put("tags", tags);				
				clientThread.sendMessage(toFacebook.toString());
				

			} catch (JSONException ex) {}
		
			
			
		} catch (Exception ex) {
			//Received invalid JSON
		}
		
	}
	
	@Override
	public void receiveMessage(String message) {
		try {
			JSONObject aReceivedMessage = new JSONObject(message);
			JSONArray aReceivedTags = aReceivedMessage.getJSONArray("tags");
			for  (Object aReceivedTag:aReceivedTags) {
				if (aReceivedTag.equals("HELP_MESSAGE")) {
					receiveHelpMessage(aReceivedMessage);
					break;
				} 
				if (aReceivedTag.equals("RETURN_FILE_DATA")) {
					receiveReturnFileData(aReceivedMessage);
					break;
				}
			
			}
			
			
		} catch (Exception ex) {
			//Received invalid JSON
		}
		
	}

}