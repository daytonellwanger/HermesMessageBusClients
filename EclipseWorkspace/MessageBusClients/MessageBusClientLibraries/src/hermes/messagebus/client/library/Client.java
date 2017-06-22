package hermes.messagebus.client.library;

import java.util.Scanner;

import util.trace.messagebus.clients.JSONObjectSentToMessageBus;
import util.trace.messagebus.clients.JSONUserObjectForwardedToMessageBus;
import util.trace.messagebus.clients.OutputSentToMessageBus;
import util.trace.messagebus.clients.StringPipedToMessageBus;
import util.trace.messagebus.clients.TagsSentToMessageBus;

public class Client {
	
	private static final String TAG_START = "<TAG>";
	private static final String MSG_START = "<MSG>";
	private static final String OUTPUT_START = "<OUTPUT>";
	private static final String MSG_SEND = "<SEND>";

	private InputThread inputThread;
	
	public Client(String tagsRegex, MessageReceiver messageReceiver) {
		sendTags(tagsRegex);
		inputThread = new InputThread(messageReceiver);
		(new Thread(inputThread)).start();
	}
	
	private void sendToCentral(String startString, String content) {
		String fullMessage = startString + content;
		StringPipedToMessageBus.newCase(this, fullMessage.length(), fullMessage);
		System.out.println(fullMessage.length());
		System.out.println(fullMessage);
	}
	
	private void sendTags(String tagsRegex) {
		TagsSentToMessageBus.newCase(this, tagsRegex);
		sendToCentral(TAG_START, tagsRegex);
	}
	
	public void sendMessage(String message) {
		JSONObjectSentToMessageBus.newCase(this, message);
		sendToCentral(MSG_START, message);
	}
	
	public void sendMessageToUser(String message) {
		JSONUserObjectForwardedToMessageBus.newCase(this, message);
		sendToCentral(MSG_SEND, message);
	}
	
	public void output(String output) {
		OutputSentToMessageBus.newCase(this, output);
		sendToCentral(OUTPUT_START, output);
	}
	
	public void stop() {
		inputThread.stop();
	}
	
	
	class InputThread implements Runnable {

		private boolean running;
		private MessageReceiver messageReceiver;
		
		public InputThread(MessageReceiver messageReceiver) {
			running = true;
			this.messageReceiver = messageReceiver;
		}
		
		public void stop() {
			running = false;
		}
		
		public void run() {
			Scanner scanner = new Scanner(System.in);
			while(running) {
				int lineLength = scanner.nextInt();
				scanner.nextLine();
				String next = "";
				while(lineLength > 0) {
					String nextLine = scanner.nextLine();
					next += nextLine;
					lineLength -= nextLine.length();
					if(lineLength > 0) {
						next += "\n";
						lineLength--;
					}
				}
				messageReceiver.receiveMessage(next);
			}
			scanner.close();
		}
		
	}
	
}