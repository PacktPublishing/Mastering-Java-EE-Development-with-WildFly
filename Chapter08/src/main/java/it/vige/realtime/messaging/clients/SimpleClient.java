package it.vige.realtime.messaging.clients;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

public class SimpleClient {

	private static final Logger logger = getLogger(SimpleClient.class.getName());

	public void sendMessage(ConnectionFactory factory, Queue queue, String message) {
		try (JMSContext context = factory.createContext()) {

			context.createProducer().send(queue, message);

		} catch (JMSRuntimeException e) {
			logger.log(SEVERE, "error messaging", e);
		}
	}
}