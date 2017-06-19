package it.vige.realtime.asynchronousejb.messagebean;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(mappedName = "jms/queue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jms/queue/DLQ"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class OldSpecsWorkingBean implements MessageListener, MessageDrivenBean {

	private static final long serialVersionUID = -2127857668359011883L;
	private static final Logger logger = getLogger(OldSpecsWorkingBean.class.getName());

	private MessageDrivenContext ctx;
	public static boolean received;

	@Override
	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
		this.ctx = ctx;
		received = true;
	}

	@Override
	public void ejbRemove() throws EJBException {
		logger.info("environment: " + ctx.getEnvironment());
		received = false;
	}

	@Override
	public void onMessage(Message message) {
	}

}
