package it.vige.businesscomponents.transactions;

import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.foundTransactionScopedBean;
import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.latch;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus;
import it.vige.businesscomponents.transactions.concurrent.MyCallableTask;
import it.vige.businesscomponents.transactions.concurrent.MyTaskWithTransaction;
import it.vige.businesscomponents.transactions.concurrent.MyTransactionScopedBean;
import it.vige.businesscomponents.transactions.concurrent.Product;

@RunWith(Arquillian.class)
public class ConcurrentTestCase {

	private static final Logger logger = getLogger(ConcurrentTestCase.class.getName());

	@Resource(name = "DefaultManagedExecutorService")
	private ManagedExecutorService defaultExecutor;

	private Callable<Product> callableTask;

	private Collection<Callable<Product>> callableTasks = new ArrayList<>();

	@Inject
	private MyTaskWithTransaction taskWithTransaction;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "concurrent-test.jar");
		jar.addClass(MyCallableTask.class);
		jar.addClass(MyTaskWithTransaction.class);
		jar.addClass(Product.class);
		jar.addClass(MyTransactionScopedBean.class);
		jar.addClass(ConcurrentStatus.class);
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	@Before
	public void setup() {
		callableTask = new MyCallableTask(1);
		for (int i = 0; i < 5; i++) {
			callableTasks.add(new MyCallableTask(i));
		}
	}

	@Test
	public void testSubmitWithCallableDefault() throws Exception {
		logger.info("start concurrent test");
		latch = new CountDownLatch(1);
		Future<Product> future = defaultExecutor.submit(callableTask);
		assertTrue(latch.await(2000, MILLISECONDS));
		assertEquals(1, future.get().getId());
	}

	@Test
	public void testInvokeAllWithCallableDefault() throws Exception {
		List<Future<Product>> results = defaultExecutor.invokeAll(callableTasks);
		int count = 0;
		for (Future<Product> f : results) {
			assertEquals(count++, f.get().getId());
		}
	}

	@Test
	public void testSubmitWithTransaction() throws Exception {
		latch = new CountDownLatch(1);
		defaultExecutor.submit(taskWithTransaction);
		assertTrue(latch.await(2000, MILLISECONDS));
		assertTrue(foundTransactionScopedBean);
	}
}
