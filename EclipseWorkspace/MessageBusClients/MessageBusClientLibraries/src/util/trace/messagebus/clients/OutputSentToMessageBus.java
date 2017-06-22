package util.trace.messagebus.clients;



import util.trace.TraceableInfo;

public class OutputSentToMessageBus extends TraceableInfo {
	public OutputSentToMessageBus(String aMessage, Object aFinder,
			String aJSONObject) {

		super(aMessage, aFinder);
	}
	public static OutputSentToMessageBus newCase(			
			Object aFinder,
			String aJSONObject
			) { 
		
		OutputSentToMessageBus retVal = new OutputSentToMessageBus(
				aJSONObject, aFinder, aJSONObject);
				
    	retVal.announce();
    	return retVal;
	}
}
