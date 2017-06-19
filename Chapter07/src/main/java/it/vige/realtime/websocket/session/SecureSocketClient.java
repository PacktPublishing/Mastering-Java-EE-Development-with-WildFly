package it.vige.realtime.websocket.session;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class SecureSocketClient extends Endpoint {

	public static Session sessionClient;

	private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();

	@Override
	public void onClose(final Session session, CloseReason closeReason) {
	}

	public static void awake() throws InterruptedException {
		queue.poll(5, SECONDS);
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		sessionClient = session;
		session.getAsyncRemote().sendText("hi");
	}
}