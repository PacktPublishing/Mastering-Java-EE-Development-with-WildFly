package it.vige.realtime.messaging.publishsubscribe;

import static it.vige.realtime.messaging.clients.Constants.TOPIC_LOOKUP;
import static java.util.logging.Logger.getLogger;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

@Stateless
public class Subscriber {

	@Resource(mappedName = TOPIC_LOOKUP)
	private Topic topic;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	private TopicMessageListener topicMessageListener = new TopicMessageListener();

	private static final Logger logger = getLogger(Subscriber.class.getName());

	public void createConnectionAndReceiveMessage(String clientId, String ipAddress) throws JMSException {
		Connection connection = connectionFactory.createConnection();
		connection.setClientID(clientId);
		connection.start();
		Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);

		String selector = "s_id = 'Sample'";
		logger.info("selector : '" + selector + "'....");
		TopicSubscriber consumer = session.createDurableSubscriber(topic, "Sub1", selector, true);

		consumer.setMessageListener(topicMessageListener);
	}

	public Message getLastReceived() {
		return topicMessageListener.getLastReceived();
	}
}
