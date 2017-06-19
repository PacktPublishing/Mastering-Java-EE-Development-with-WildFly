package it.vige.realtime.websocket.session;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/session")
public class SessionSocketServer {

	public static Session sessionServer;

	private Logger logger = getLogger(SessionSocketServer.class.getName());

	@OnOpen
	public void onOpen(Session session) throws NamingException {
		logger.info("Open session:" + session.getId());
		sessionServer = session;
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
