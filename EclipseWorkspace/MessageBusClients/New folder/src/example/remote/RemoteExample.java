package example.remote;

import org.json.JSONObject;
//
import hermes.messagebus.client.library.MessageReceiver;
import hermes.messagebus.client.library.Client;

public class RemoteExample 
	implements MessageReceiver 
	{
	
	Client clientThread;
	private static final String TAGS = ".*";

	public static void main(String[] args) {
		new RemoteExample().go();
	}
	
	private void go() {
	    clientThread = new Client(TAGS, this);
	}
	
	@Override
	public void receiveMessage(String message) {
		JSONObject msgObj = new JSONObject(message);
		String from = msgObj.getString("from");
		JSONObject messageToSend = new JSONObject();
		messageToSend.put("to", from);
		messageToSend.put("status", "received");
		clientThread.sendMessageToUser(messageToSend.toString());
	}

}