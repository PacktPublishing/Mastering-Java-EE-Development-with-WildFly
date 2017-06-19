package it.vige.realtime.websocket.negotiation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{user}", subprotocols = { "foo", "bar", "configured-proto" })
public class MessageEndpoint {

	public static Session sessionServer;

	public static volatile CloseReason closeReason;
	private static volatile CountDownLatch closeLatch = new CountDownLatch(1);

	@OnOpen
	public void onOpen(Session session) throws NamingException {
		sessionServer = session;
	}

	@OnMessage
	public String handleMessage(Session session, final String message, @PathParam("user") String user) {
		String proto = session.getNegotiatedSubprotocol();
		return message + " " + user + (proto.isEmpty() ? "" : " (protocol=" + proto + ")");
	}

	@OnClose
	public void close(CloseReason c) {
		closeReason = c;
		closeLatch.countDown();
	}

	public static CloseReason getReason() throws InterruptedException {
		closeLatch.await(10, TimeUnit.SECONDS);
		return closeReason;
	}

	public static void reset() {
		closeLatch = new CountDownLatch(1);
		closeReason = null;
	}

}
