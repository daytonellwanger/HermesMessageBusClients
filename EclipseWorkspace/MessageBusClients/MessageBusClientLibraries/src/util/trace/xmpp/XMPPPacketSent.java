package util.trace.xmpp;



import util.trace.TraceableInfo;

public class XMPPPacketSent extends TraceableInfo {
	public XMPPPacketSent(String aMessage, Object aFinder,
			String aPacket) {

		super(aMessage, aFinder);
	}
	public static XMPPPacketSent newCase(			
			Object aFinder,
			String aPacket
			) { 
		
		XMPPPacketSent retVal = new XMPPPacketSent(
				aPacket.toString(), aFinder, aPacket);
				
    	retVal.announce();
    	return retVal;
	}
}
