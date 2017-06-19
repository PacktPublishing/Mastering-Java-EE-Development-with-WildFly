package it.vige.realtime.asynchronousejb;

import static org.junit.Assert.assertEquals;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.asynchronousejb.bean.AsyncBean;

@RunWith(Arquillian.class)
public class AsyncBeanTestCase {

	private static final Logger logger = getLogger(AsyncBeanTestCase.class.getName());

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "asynch-ejb-test.jar");
		jar.addPackage(AsyncBean.class.getPackage());
		return jar;
	}

	@Inject
	private AsyncBean asyncBean;

	@Test
	public void testIgnoreResult() {
		logger.info("start test ignore result");
		asyncBean.ignoreResult(0, 0);
		logger.info("Proceed without waiting for the async method result.");
	}

	@Test
	public void testLongProcessing() throws InterruptedException, ExecutionException {
		logger.info("start test long processing");
		Future<Integer> futureResult = asyncBean.longProcessing(8, 9);
		logger.info("Proceed to other tasks and check async method result later.");
		Integer intResult = futureResult.get();
		logger.info("The prior async method returned " + intResult);
		assertEquals("result is: ", 72, intResult.intValue());
	}
}
