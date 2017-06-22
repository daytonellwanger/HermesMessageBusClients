package util.trace.messagebus.clients;



import util.trace.TraceableInfo;

public class TagsSentToMessageBus extends TraceableInfo {
	public TagsSentToMessageBus(String aMessage, Object aFinder,
			String aTagRegex) {

		super(aMessage, aFinder);
	}
	public static TagsSentToMessageBus newCase(			
			Object aFinder,
			String aTagRegex
			) { 
		
		TagsSentToMessageBus retVal = new TagsSentToMessageBus(
				aTagRegex, aFinder, aTagRegex);
				
    	retVal.announce();
    	return retVal;
	}
}
