package it.vige.realtime.javamail;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.smtp.server.SMTPServer;

@RunWith(Arquillian.class)
public class SendMailTestCase {

	private static final Logger logger = getLogger(SendMailTestCase.class.getName());

	private MyMessageHandlerFactory myFactory = new MyMessageHandlerFactory();
	private SMTPServer smtpServer = new SMTPServer(myFactory);

	@Deployment
	public static EnterpriseArchive createEnterpriseDeployment() {
		final EnterpriseArchive ear = create(EnterpriseArchive.class, "sendmail.ear");
		final JavaArchive jar = create(JavaArchive.class, "sendmail.jar");
		File[] files = resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
				.resolve("org.subethamail:subethasmtp:3.1.7").withTransitivity().asFile();
		jar.addPackage(SendMail.class.getPackage());
		ear.addAsModule(jar);
		ear.addAsLibraries(files);
		return ear;
	}

	@Before
	public void init() {
		smtpServer.setPort(25000);
		smtpServer.start();
	}

	@After
	public void end() {
		smtpServer.stop();
	}

	@EJB
	private SendMailWithResource sendMailWithResource;

	@Test
	@RunAsClient
	public void send() throws AddressException, MessagingException {
		SendMail sendMail = new SendMail();
		sendMail.completeGoogleClientSend();
		logger.info("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
		sendMail.completeLocalClientSend("", "");
		logger.info("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
		assertTrue("message received", myFactory.getBody().contains("Test email by Vige.it JavaMail API example"));
	}

	@Test
	public void sendFromResource() throws AddressException, MessagingException {
		sendMailWithResource.send("luca.stancapiano@vige.it", "subject", "text");
		logger.info("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
		assertTrue("message received", myFactory.getBody().contains("text"));
	}
}
