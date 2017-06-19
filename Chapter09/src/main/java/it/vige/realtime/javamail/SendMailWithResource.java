package it.vige.realtime.javamail;

import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static javax.mail.Message.RecipientType.TO;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class SendMailWithResource {

	private static final Logger logger = getLogger(SendMailWithResource.class.getName());

	@Resource(name = "java:jboss/mail/Default")
	private Session session;

	public void send(String addresses, String topic, String textMessage) {

		try {
			session.getProperties().put("mail.smtp.port", 25000);
			Message message = new MimeMessage(session);
			message.setRecipients(TO, InternetAddress.parse(addresses));
			message.setSubject(topic);
			message.setText(textMessage);

			Transport.send(message);
			logger.info("sent mail with resource!");

		} catch (MessagingException e) {
			logger.log(WARNING, "Cannot send mail", e);
		}
	}
}
