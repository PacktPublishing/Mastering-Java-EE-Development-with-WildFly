package it.vige.realtime.asynchronousejb;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.asynchronousejb.scheduler.SchedulerBean;

@RunWith(Arquillian.class)
public class SchedulerTestCase {

	private static final Logger logger = getLogger(SchedulerTestCase.class.getName());

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "scheduler-ejb-test.jar");
		jar.addPackage(SchedulerBean.class.getPackage());
		return jar;
	}

	@Inject
	private SchedulerBean schedulerBean;

	@Test
	public void testIgnoreResult() {
		logger.info("start test scheduler");
		Date today = new Date();
		schedulerBean.setTimer(10);
		try {
			sleep(12);
		} catch (InterruptedException e) {
			logger.log(SEVERE, "Interruption", e);
		}
		Date programmaticDate = schedulerBean.getLastProgrammaticTimeout();
		assertNotNull("the programmatic date is created", programmaticDate);
		assertTrue("the programmatic date is 10 milliseconds around instead of the today date",
				today.compareTo(programmaticDate) < 0);
		Date automaticDate = schedulerBean.getLastAutomaticTimeout();
		assertNotNull("the automatic date is created", automaticDate);
		assertTrue("the today date is 1 milliseconds around insted of the automatic date",
				today.compareTo(automaticDate) > 0);
		logger.info("End test scheduler.");
	}

	@Test
	public void testManualConfiguration() {
		Timer timer = schedulerBean.setTimer("10", new Date());
		ScheduleExpression scheduleExpression = timer.getSchedule();
		scheduleExpression.hour(10);
		scheduleExpression.start(new Date());
		assertEquals("The timer will start today at the 10", "10", scheduleExpression.getHour());
		assertEquals("The timer will start each Wednesday too", "Wed", scheduleExpression.getDayOfWeek());
	}

}
