package it.vige.realtime.websocket.intro;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class WebSocketClient {

	public static boolean postConstructCalled = false;

	private static final BlockingDeque<String> queue = new LinkedBlockingDeque<>();

	@PostConstruct
	private void init() {
		postConstructCalled = true;
	}

	@OnOpen
	public void open(final Session session) throws IOException {
		session.getBasicRemote().sendText("Hello");
	}

	@OnMessage
	public void message(final String message) {
		queue.add(message);
	}

	public static String getMessage() throws InterruptedException {
		return queue.poll(5, SECONDS);
	}

	public static void reset() {
		queue.clear();
		postConstructCalled = false;
	}

	@OnClose
	public void close(final Session session) throws IOException {
	}
}