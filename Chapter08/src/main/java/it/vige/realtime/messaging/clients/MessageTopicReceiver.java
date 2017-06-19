package it.vige.realtime.messaging.clients;

import static it.vige.realtime.messaging.clients.Constants.TOPIC_LOOKUP;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

@Stateless
public class MessageTopicReceiver {

	@Inject
	private JMSContext context;

	@Resource(mappedName = TOPIC_LOOKUP)
	private Topic topic;

	public Message receiveMessage() {
		return context.createConsumer(topic).receive(10);
	}

	public JMSConsumer getConsumer() {
		return context.createConsumer(topic);
	}

}
