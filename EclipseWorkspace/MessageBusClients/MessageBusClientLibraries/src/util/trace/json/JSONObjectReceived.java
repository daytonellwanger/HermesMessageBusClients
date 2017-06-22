package util.trace.json;



import util.trace.TraceableInfo;

public class JSONObjectReceived extends TraceableInfo {
	public JSONObjectReceived(String aMessage, Object aFinder,
			String aJSONObject) {

		super(aMessage, aFinder);
	}
	public static JSONObjectReceived newCase(			
			Object aFinder,
			String aJSONObject
			) { 
		
		JSONObjectReceived retVal = new JSONObjectReceived(
				aJSONObject.toString(), aFinder, aJSONObject);
				
    	retVal.announce();
    	return retVal;
	}
}
