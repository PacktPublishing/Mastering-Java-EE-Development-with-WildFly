package it.vige.realtime.batchesworkflow.mail;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Transport.send;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Named
public class MailBatchlet extends AbstractBatchlet {

	private static final Logger logger = getLogger(MailBatchlet.class.getName());

	@Resource(mappedName = "java:jboss/mail/Default")
	private Session mailSession;

	@Inject
	private StepContext stepContext;
	@Inject
	private JobContext jobContext;

	@Override
	public String process() {
		logger.info("Running inside MailBatchlet batchlet ");
		String fromAddress = stepContext.getProperties().getProperty("mail.from");
		String toAddress = stepContext.getProperties().getProperty("mail.to");

		try {
			MimeMessage m = new MimeMessage(mailSession);
			Address from = new InternetAddress(fromAddress);
			Address[] to = new InternetAddress[] { new InternetAddress(toAddress) };

			m.setFrom(from);
			m.setRecipients(TO, to);
			m.setSubject("Batch on wildfly executed");
			m.setSentDate(new java.util.Date());
			m.setContent("Job Execution id " + jobContext.getExecutionId() + " warned disk space getting low!",
					"text/plain");
			send(m);

		} catch (javax.mail.MessagingException e) {
			logger.log(SEVERE, "error send mail", e);

		}
		return COMPLETED.name();
	}

}
