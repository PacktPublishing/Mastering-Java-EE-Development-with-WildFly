package it.vige.realtime.messaging;

import static it.vige.realtime.messaging.clients.Constants.CONNECTION_FACTORY;
import static it.vige.realtime.messaging.clients.Constants.REMOTE_EXPORTED_QUEUE_LOOKUP;
import static it.vige.realtime.messaging.clients.Constants.REMOTE_QUEUE_LOOKUP;
import static it.vige.realtime.messaging.clients.Constants.REMOTE_QUEUE_NAME;
import static it.vige.realtime.messaging.clients.Constants.USER_NAME;
import static it.vige.realtime.messaging.clients.Constants.USER_PASSWORD;
import static java.lang.Thread.sleep;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.logging.Logger.getLogger;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static org.jboss.as.test.integration.common.jms.JMSOperationsProvider.getInstance;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.test.integration.common.jms.JMSOperations;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.messaging.RemoteTestCase.MessagingResourcesSetupTask;
import it.vige.realtime.messaging.remote.MessageQueueListener;
import it.vige.realtime.messaging.remote.Questionary;

@RunWith(Arquillian.class)
@ServerSetup(MessagingResourcesSetupTask.class)
public class RemoteTestCase {

	private static final Logger logger = getLogger(RemoteTestCase.class.getName());

	static class MessagingResourcesSetupTask implements ServerSetupTask {

		@Override
		public void setup(ManagementClient managementClient, String containerId) throws Exception {
			copy(new File("src/test/resources/application-users.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-users.properties")
							.toPath(),
					REPLACE_EXISTING);
			copy(new File("src/test/resources/application-roles.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-roles.properties")
							.toPath(),
					REPLACE_EXISTING);
			ModelControllerClient modelControllerClient = managementClient.getControllerClient();
			JMSOperations jmsOperations = getInstance(modelControllerClient);
			jmsOperations.createJmsQueue(REMOTE_QUEUE_NAME, REMOTE_EXPORTED_QUEUE_LOOKUP);
		}

		@Override
		public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
			ModelControllerClient modelControllerClient = managementClient.getControllerClient();
			JMSOperations jmsOperations = getInstance(modelControllerClient);
			jmsOperations.removeJmsQueue(REMOTE_QUEUE_NAME);
		}
	}

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "remoting-message.jar");
		jar.addAsResource(new FileAsset(new File("src/test/resources/application-users.properties")),
				"users.properties");
		jar.addAsResource(new FileAsset(new File("src/test/resources/application-roles.properties")),
				"roles.properties");
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	@Test
	@RunAsClient
	public void testListener() throws JMSException, NamingException, InterruptedException {
		final Properties env = new Properties();
		env.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		env.put(PROVIDER_URL, "http-remoting://127.0.0.1:8080");

		Context namingContext = new InitialContext(env);

		ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(CONNECTION_FACTORY);
		logger.info("Got ConnectionFactory " + CONNECTION_FACTORY);

		Destination destination = (Destination) namingContext.lookup(REMOTE_QUEUE_LOOKUP);
		logger.info("Got JMS Endpoint " + REMOTE_QUEUE_LOOKUP);

		String question = "how many components in your family?";
		String response = "they are four";

		// Create the JMS context
		JMSContext context = connectionFactory.createContext(USER_NAME, USER_PASSWORD);
		Questionary questionary = new Questionary();
		questionary.setQuestion(question);
		questionary.setResponse(response);
		assertFalse("questionary is not approved", questionary.isApproved());
		JMSProducer producer = context.createProducer();
		producer.send(destination, questionary);

		JMSConsumer consumer = context.createConsumer(destination);
		MessageQueueListener messageQueueListener = new MessageQueueListener();
		consumer.setMessageListener(messageQueueListener);
		sleep(100);
		questionary = messageQueueListener.getQuestionary();
		assertEquals("the question is: ", question, questionary.getQuestion());
		assertEquals("the response is: ", response, questionary.getResponse());
		assertTrue("the message is approved: ", questionary.isApproved());
	}

}
