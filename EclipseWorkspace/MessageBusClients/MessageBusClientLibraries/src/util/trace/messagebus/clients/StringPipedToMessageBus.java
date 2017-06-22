package util.trace.messagebus.clients;



import util.trace.TraceableInfo;

public class StringPipedToMessageBus extends TraceableInfo {
	public StringPipedToMessageBus(String aMessage, Object aFinder,
			int aSize,
			String aString) {

		super(aMessage, aFinder);
	}
	public static StringPipedToMessageBus newCase(			
			Object aFinder,
			int aSize,
			String aString
			) { 
		
		StringPipedToMessageBus retVal = new StringPipedToMessageBus(
				aString + "(" + aSize + " bytes)", aFinder, aSize, aString);
				
    	retVal.announce();
    	return retVal;
	}
}
