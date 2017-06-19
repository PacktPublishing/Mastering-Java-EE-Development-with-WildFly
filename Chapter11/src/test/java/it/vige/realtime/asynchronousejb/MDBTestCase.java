package it.vige.realtime.asynchronousejb;

import static it.vige.realtime.asynchronousejb.messagebean.OldSpecsWorkingBean.received;
import static it.vige.realtime.asynchronousejb.messagebean.WorkingBean.taken;
import static java.util.logging.Logger.getLogger;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.asynchronousejb.messagebean.WorkingBean;

@RunWith(Arquillian.class)
public class MDBTestCase {

	private static final Logger logger = getLogger(MDBTestCase.class.getName());

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "mdb-ejb-test.jar");
		jar.addPackage(WorkingBean.class.getPackage());
		return jar;
	}

	private final static String QUEUE_WORKING_LOOKUP = "java:/jms/queue/ExpiryQueue";
	private final static String QUEUE_OLDSPECS_LOOKUP = "java:/jms/queue/DLQ";

	@EJB
	private WorkingBean workingBean;

	@Resource(mappedName = "/ConnectionFactory")
	private ConnectionFactory cf;

	@Resource(mappedName = QUEUE_WORKING_LOOKUP)
	private Queue queueWorking;

	@Resource(mappedName = QUEUE_OLDSPECS_LOOKUP)
	private Queue queueOldSpecs;

	@Test
	public void testWorkingBean() throws Exception {
		logger.info("starting message driven bean test");
		JMSContext context = cf.createContext(AUTO_ACKNOWLEDGE);
		context.createProducer().send(queueWorking, "need a pause");
		Message message = context.createConsumer(queueWorking).receive(5000);
		assertNull("the message is received by the mdb: ", message);
		assertTrue("the mdb was executed", taken);
		context.close();
	}

	@Test
	public void testOldSpecs() throws Exception {
		logger.info("starting old specs test");
		JMSContext context = cf.createContext(AUTO_ACKNOWLEDGE);
		context.createProducer().send(queueOldSpecs, "need a pause");
		Message message = context.createConsumer(queueOldSpecs).receive(5000);
		assertNull("the message is received by the mdb: ", message);
		assertTrue("created oldSpecsWorkingBean", received);
		context.close();
	}
}
