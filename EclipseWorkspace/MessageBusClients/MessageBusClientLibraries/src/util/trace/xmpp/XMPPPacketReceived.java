package util.trace.xmpp;



import util.trace.TraceableInfo;

public class XMPPPacketReceived extends TraceableInfo {
	public XMPPPacketReceived(String aMessage, Object aFinder,
			String aPacket) {

		super(aMessage, aFinder);
	}
	public static XMPPPacketReceived newCase(			
			Object aFinder,
			String aPacket
			) { 
		
		XMPPPacketReceived retVal = new XMPPPacketReceived(
				aPacket.toString(), aFinder, aPacket);
				
    	retVal.announce();
    	return retVal;
	}
}
