package util.trace.messagebus.clients;


import util.trace.TraceableInfo;

public class ReceivedJSONObjectForwardedToClientProcess extends TraceableInfo {
	public ReceivedJSONObjectForwardedToClientProcess(String aMessage, Object aFinder,
			String aClient, String aTag ) {

		super(aMessage, aFinder);
	}
	public static ReceivedJSONObjectForwardedToClientProcess newCase(			
			Object aFinder,
			String aClient, String aTag
			) { 
		
		String aMessage = "Client:" + 
		aClient + " matching tag: " + aTag;
		
		ReceivedJSONObjectForwardedToClientProcess retVal = new ReceivedJSONObjectForwardedToClientProcess(
				aMessage, aFinder, aClient, aTag);				
    	retVal.announce();
    	return retVal;
	}
}
