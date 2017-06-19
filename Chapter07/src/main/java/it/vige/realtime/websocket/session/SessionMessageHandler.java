package it.vige.realtime.websocket.session;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.websocket.MessageHandler.Whole;
import javax.websocket.PongMessage;

public class SessionMessageHandler implements Whole<PongMessage> {

	private Logger logger = getLogger(SessionMessageHandler.class.getName());
	
	public static boolean sentPong;

	@Override
	public void onMessage(PongMessage message) {
		logger.info("message: " + message);
		sentPong = true;
	}

}
