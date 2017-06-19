package it.vige.realtime.asynchronousejb;

import static java.lang.Thread.sleep;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.NoSuchEJBException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.asynchronousejb.timer.OldSpecsLocal;
import it.vige.realtime.asynchronousejb.timer.TimerBean;
import it.vige.realtime.asynchronousejb.timer.User;

@RunWith(Arquillian.class)
public class TimerTestCase {

	private static final Logger logger = getLogger(TimerTestCase.class.getName());

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "timer-ejb-test.jar");
		jar.addPackage(TimerBean.class.getPackage());
		return jar;
	}

	@Inject
	private TimerBean timerBean;

	@EJB
	private OldSpecsLocal oldSpecsBean;

	@EJB
	private User user;

	@Test
	public void testTimeout() throws InterruptedException {
		logger.info("start test timeout");
		timerBean.programmaticTimeout();
		sleep(66);
		assertTrue("executed timeout", timerBean.isTimeoutDone());
		timerBean.timerConfig();
		sleep(66);
		assertTrue("executed timeout", timerBean.isTimeoutDone());
		logger.info("Timeout end.");
	}

	@Test
	public void testOldSpecsTimeout() throws InterruptedException {
		logger.info("start test old specs timeout");
		oldSpecsBean.fireInThirtySeconds();
		sleep(310);
		assertEquals("the ejb timout was invoked", "30SecondTimeout", oldSpecsBean.getWhyWasICalled());
		logger.info("Timeout end.");
	}

	@Test
	public void testStatefulTimeout() throws InterruptedException {
		logger.info("start test stateful timeout");
		assertFalse("The stateful session is not registered", user.isRegistered());
		user.register();
		assertTrue("The stateful session is registered", user.isRegistered());
		sleep(160);
		try {
			user.isRegistered();
			fail();
		} catch (NoSuchEJBException e) {
			logger.info("The ejb is expired");
		}
		logger.info("Timeout end.");
	}
}
