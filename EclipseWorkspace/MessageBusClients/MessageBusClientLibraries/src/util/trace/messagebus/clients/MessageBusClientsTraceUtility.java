package util.trace.messagebus.clients;



import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;
//import util.trace.hermes.connectionmanager.ForwardedJSONObjectReceivedByConnectionManager;
//import util.trace.hermes.connectionmanager.JSONObjectForwardedToConnectionManager;


public class MessageBusClientsTraceUtility {

	public static void setTracing() {
		Tracer.showInfo(true);
		Tracer.setDisplayThreadName(false); 
		TraceableInfo.setPrintTraceable(true);
		TraceableInfo.setPrintSource(true);
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		
	
//		Tracer.setKeywordPrintStatus(CallInitiated.class, true);
//		Tracer.setKeywordPrintStatus(ForwardedJSONObjectReceivedByConnectionManager.class, true);
//
//		Tracer.setKeywordPrintStatus(JSONObjectForwardedToConnectionManager.class, true);
		Tracer.setKeywordPrintStatus(JSONObjectSentToMessageBus.class, true);
		Tracer.setKeywordPrintStatus(JSONUserObjectForwardedToMessageBus.class, true);
		Tracer.setKeywordPrintStatus(OutputSentToMessageBus.class, true);
		Tracer.setKeywordPrintStatus(ReceivedJSONObjectForwardedToClientProcess.class, true);
		Tracer.setKeywordPrintStatus(StringPipedToMessageBus.class, true);
		Tracer.setKeywordPrintStatus(TagsSentToMessageBus.class, true);		








	}

}
