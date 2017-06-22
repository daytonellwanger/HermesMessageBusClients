package util.trace.messagebus.clients;



import util.trace.TraceableInfo;

public class JSONObjectSentToMessageBus extends TraceableInfo {
	public JSONObjectSentToMessageBus(String aMessage, Object aFinder,
			String aJSONObject) {

		super(aMessage, aFinder);
	}
	public static JSONObjectSentToMessageBus newCase(			
			Object aFinder,
			String aJSONObject
			) { 
		
		JSONObjectSentToMessageBus retVal = new JSONObjectSentToMessageBus(
				aJSONObject, aFinder, aJSONObject);
				
    	retVal.announce();
    	return retVal;
	}
}
