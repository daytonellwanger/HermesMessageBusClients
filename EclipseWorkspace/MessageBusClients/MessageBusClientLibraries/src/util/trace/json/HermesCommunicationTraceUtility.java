package util.trace.json;



import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;
import util.trace.xmpp.XMPPPacketReceived;
import util.trace.xmpp.XMPPPacketSent;


public class HermesCommunicationTraceUtility {

	public static void setTracing() {
		Tracer.showInfo(true);
		Tracer.setDisplayThreadName(true); 
		TraceableInfo.setPrintTraceable(true);
		TraceableInfo.setPrintSource(true);
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		
	
		Tracer.setKeywordPrintStatus(JSONObjectReceived.class, true);
		Tracer.setKeywordPrintStatus(JSONObjectSentToUser.class, true);
		Tracer.setKeywordPrintStatus(XMPPPacketReceived.class, true);
		Tracer.setKeywordPrintStatus(XMPPPacketSent.class, true);

	}

}
