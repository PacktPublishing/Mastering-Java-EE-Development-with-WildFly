package it.vige.realtime.messaging.publishsubscribe;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.jms.Message;
import javax.jms.MessageListener;

public class TopicMessageListener implements MessageListener {

	private static final Logger logger = getLogger(TopicMessageListener.class.getName());
	
	private Message lastReceived;

	@Override
	public void onMessage(Message message) {
		logger.info("the received message: " + message);
		lastReceived = message;
	}

	public Message getLastReceived() {
		return lastReceived;
	}

}
