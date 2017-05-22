package logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import hermes.messagebus.client.library.Client;
import hermes.messagebus.client.library.MessageReceiver;


public class Logger implements MessageReceiver {
	
	Client clientThread;
	private static final String TAGS = ".*";
	PrintWriter outputFile;
	private static String MESSAGE_SEPARATOR = "--------------------------------------------------------------------------------";

	public static void main(String[] args) {
		new Logger().go();
	}
	
	private void go() {
	    clientThread = new Client(TAGS, this);
	    try {
	    	outputFile = new PrintWriter(new File ("log.txt"));
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}
	
	@Override
	public void receiveMessage(String message) {
		outputFile.println(MESSAGE_SEPARATOR);
		outputFile.println((new Date()).toString());
		outputFile.println();
		outputFile.println(message);
		outputFile.flush();
	}

}