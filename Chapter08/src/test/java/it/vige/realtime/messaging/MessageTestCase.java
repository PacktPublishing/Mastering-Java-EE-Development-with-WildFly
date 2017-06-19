package it.vige.realtime.messaging;

import static it.vige.realtime.messaging.clients.Constants.QUEUE_LOOKUP;
import static it.vige.realtime.messaging.clients.Constants.QUEUE_NAME;
import static it.vige.realtime.messaging.clients.Constants.TOPIC_LOOKUP;
import static it.vige.realtime.messaging.clients.Constants.TOPIC_NAME;
import static java.util.logging.Logger.getLogger;
import static org.jboss.as.test.integration.common.jms.JMSOperationsProvider.getInstance;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.Message;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.common.jms.JMSOperations;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.messaging.MessageTestCase.MessagingResourcesSetupTask;
import it.vige.realtime.messaging.clients.MessageQueueReceiver;
import it.vige.realtime.messaging.clients.MessageQueueSender;
import it.vige.realtime.messaging.clients.MessageTopicReceiver;
import it.vige.realtime.messaging.clients.MessageTopicSender;

@RunWith(Arquillian.class)
@ServerSetup(MessagingResourcesSetupTask.class)
public class MessageTestCase {

	private static final Logger logger = getLogger(MessageTestCase.class.getName());

	@EJB
	private MessageQueueSender messageQueueSender;

	@EJB
	private MessageTopicSender messageTopicSender;

	@EJB
	private MessageQueueReceiver messageQueueReceiver;

	@EJB
	private MessageTopicReceiver messageTopicReceiver;

	static class MessagingResourcesSetupTask implements ServerSetupTask {

		@Override
		public void setup(ManagementClient managementClient, String containerId) throws Exception {
			JMSOperations jmsOperations = getInstance(managementClient.getControllerClient());
			jmsOperations.createJmsQueue(QUEUE_NAME, QUEUE_LOOKUP);
			jmsOperations.createJmsTopic(TOPIC_NAME, TOPIC_LOOKUP);
		}

		@Override
		public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
			JMSOperations jmsOperations = getInstance(managementClient.getControllerClient());
			jmsOperations.removeJmsQueue(QUEUE_NAME);
			jmsOperations.removeJmsTopic(TOPIC_NAME);
		}
	}

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "message.jar");
		jar.addPackage(MessageQueueSender.class.getPackage());
		return jar;
	}

	@Test
	public void testSendQueueMessage() throws JMSException {
		logger.info("Start send message queue test");
		messageQueueSender.sendMessage("hello!");
		Message message = messageQueueReceiver.receiveMessage();
		String body = message.getBody(String.class);
		assertEquals("the message is received: ", "hello!", body);
	}

	@Test
	public void testSendTopicMessage() throws JMSException {
		logger.info("Start send message topic test");
		messageTopicSender.sendMessage("hello!");
		Message message = messageTopicReceiver.receiveMessage();
		assertNull("the message is not received: ", message);
	}
}
