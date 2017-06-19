package it.vige.realtime.messaging.clients;

import static it.vige.realtime.messaging.clients.Constants.QUEUE_LOOKUP;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
public class MessageQueueSender {

	@Inject
	private JMSContext context;

	@Resource(mappedName = QUEUE_LOOKUP)
	private Queue queue;

	public void sendMessage(String message) {
		context.createProducer().send(queue, message);
	}
}
