package util.trace.messagebus.clients;



import util.trace.TraceableInfo;

public class JSONUserObjectForwardedToMessageBus extends TraceableInfo {
	public JSONUserObjectForwardedToMessageBus(String aMessage, Object aFinder,
			String aJSONObject) {

		super(aMessage, aFinder);
	}
	public static JSONUserObjectForwardedToMessageBus newCase(			
			Object aFinder,
			String aJSONObject
			) { 
		
		JSONUserObjectForwardedToMessageBus retVal = new JSONUserObjectForwardedToMessageBus(
				aJSONObject, aFinder, aJSONObject);
				
    	retVal.announce();
    	return retVal;
	}
}
