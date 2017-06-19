package it.vige.realtime.websocket.intro;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.naming.InitialContext.doLookup;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.NamingException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
public class WebSocketServer {

	private Logger logger = getLogger(WebSocketServer.class.getName());

	@OnOpen
	public void onOpen(Session session) throws NamingException {
		logger.info("Open session:" + session.getId());
		ManagedExecutorService mes = doLookup("java:comp/DefaultManagedExecutorService");
		final Session s = session;
		mes.execute(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < 3; i++) {
						sleep(10000);
						s.getBasicRemote().sendText("Message from server");
					}
				} catch (InterruptedException | IOException e) {
					logger.log(SEVERE, "connection error", e);
				}
			}
		});
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		return "Server received [" + message + "]";
	}

	@OnClose
	public void onClose(Session session) {
		logger.info("Closing:" + session.getId());
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		logger.log(SEVERE, "error sending message", exception);
	}
}
