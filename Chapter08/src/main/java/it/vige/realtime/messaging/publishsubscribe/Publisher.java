package it.vige.realtime.messaging.publishsubscribe;

import static it.vige.realtime.messaging.clients.Constants.TOPIC_LOOKUP;
import static java.util.logging.Logger.getLogger;
import static javax.jms.DeliveryMode.PERSISTENT;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

@Stateless
public class Publisher {

	private static final Logger logger = getLogger(Publisher.class.getName());

	@Resource(mappedName = TOPIC_LOOKUP)
	private Topic topic;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	public void createConnectionAndSendMessage(String ipAddress) throws JMSException {

		Connection connection = connectionFactory.createConnection();
		connection.start();

		Session topicSession = connection.createSession(false, AUTO_ACKNOWLEDGE);

		MessageProducer producer = topicSession.createProducer(topic);
		producer.setDeliveryMode(PERSISTENT);

		ObjectMessage message = topicSession.createObjectMessage();

		BUSStop busStop = new BUSStop();
		busStop.setName("Rome");

		message.setStringProperty("s_id", "Sample");
		message.setObject((Serializable) busStop);

		producer.send(message);
		logger.info("message sent successfully");
	}
}
