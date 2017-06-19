package it.vige.realtime.websocket.session;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SessionSocketClient {

	public static Session sessionClient;

	private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();

	@OnOpen
	public void open(Session session) throws IOException {
		sessionClient = session;
	}

	@OnClose
	public void close(final Session session) throws IOException {
	}

	@OnMessage
	public void message(final String message) {
		queue.add(message);
	}

	public static void awake() throws InterruptedException {
		queue.poll(5, SECONDS);
	}
}