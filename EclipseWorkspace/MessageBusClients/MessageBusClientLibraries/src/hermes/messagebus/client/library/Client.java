package hermes.messagebus.client.library;

import java.util.Scanner;

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
		System.out.println(fullMessage.length());
		System.out.println(fullMessage);
	}
	
	private void sendTags(String tagsRegex) {
		sendToCentral(TAG_START, tagsRegex);
	}
	
	public void sendMessage(String message) {
		sendToCentral(MSG_START, message);
	}
	
	public void sendMessageToUser(String message) {
		sendToCentral(MSG_SEND, message);
	}
	
	public void output(String output) {
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