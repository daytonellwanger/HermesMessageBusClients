package util.trace.json;



import util.trace.TraceableInfo;

public class JSONObjectSentToUser extends TraceableInfo {
	public JSONObjectSentToUser(String aMessage, Object aFinder,
			String aJSONObject) {

		super(aMessage, aFinder);
	}
	public static JSONObjectSentToUser newCase(			
			Object aFinder,
			String aJSONObject
			) { 
		
		JSONObjectSentToUser retVal = new JSONObjectSentToUser(
				aJSONObject.toString(), aFinder, aJSONObject);
				
    	retVal.announce();
    	return retVal;
	}
}
