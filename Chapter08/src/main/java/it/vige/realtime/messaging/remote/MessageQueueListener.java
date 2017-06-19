package it.vige.realtime.messaging.remote;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class MessageQueueListener implements MessageListener {

	private static final Logger logger = getLogger(MessageQueueListener.class.getName());
	
	private Questionary questionary;

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Questionary questionary = objectMessage.getBody(Questionary.class);
			questionary.setApproved(true);
			this.questionary = questionary;
		} catch (JMSException e) {
			logger.log(SEVERE, "jms error", e);
		}
	}

	public Questionary getQuestionary() {
		return questionary;
	}

}
