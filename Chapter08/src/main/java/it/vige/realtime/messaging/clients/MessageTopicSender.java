package it.vige.realtime.messaging.clients;

import static it.vige.realtime.messaging.clients.Constants.TOPIC_LOOKUP;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;

@Stateless
public class MessageTopicSender {

	@Inject
	private JMSContext context;

	@Resource(mappedName = TOPIC_LOOKUP)
	private Topic topic;

	public void sendMessage(String message) {
		context.createProducer().send(topic, message);
	}

	public JMSProducer getProducer() {
		return context.createProducer();
	}
}
