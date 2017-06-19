package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.event.Bill;
import it.vige.businesscomponents.injection.event.IfExistsObserver;

@RunWith(Arquillian.class)
@Resources({ @Resource(name = "java:comp/BeanManager", type = BeanManager.class, lookup = "java:comp/BeanManager"),
		@Resource(name = "java:comp/UserTransaction", type = UserTransaction.class, lookup = "java:comp/UserTransaction") })
public class EventTestCase {

	private static final Logger logger = getLogger(EventTestCase.class.getName());

	@Resource(lookup = "java:comp/BeanManager")
	private BeanManager beanManager;

	@Resource(lookup = "java:comp/UserTransaction")
	private UserTransaction userTransaction;

	@Inject
	private Event<Bill> billEvent;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "event-test.jar");
		jar.addPackage(Bill.class.getPackage());
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	/**
	 * Tests always observer injection in a jar archive
	 */
	@Test
	public void testAlways() {
		logger.info("starting always event test");
		Bill bill = fire();
		assertEquals("The id generation passes through only the always observer and it is incremented", 5,
				bill.getId());
	}

	/**
	 * Tests if exists observer injection in a jar archive
	 */
	@Test
	public void testIfExists() {
		logger.info("starting if exists event test");
		// To test the IF_EXISTS Reception I need to inject the observer bean so
		// it will be instantiated and ready to use
		Set<Bean<?>> beans = beanManager.getBeans(IfExistsObserver.class);
		assertEquals(beans.size(), 1);
		@SuppressWarnings("unchecked")
		Bean<IfExistsObserver> bean = (Bean<IfExistsObserver>) beans.iterator().next();
		CreationalContext<IfExistsObserver> ctx = beanManager.createCreationalContext(bean);
		beanManager.getReference(bean, IfExistsObserver.class, ctx);
		Bill bill = fire();
		assertEquals("The id generation passes through the always and if_exists observers and it is incremented", 10,
				bill.getId());
	}

	/**
	 * Tests the size of observer methods in a jar archive
	 */
	@Test
	public void testSize() {
		logger.info("starting size event test");
		Set<ObserverMethod<? super Bill>> observers = beanManager.resolveObserverMethods(new Bill());
		assertEquals(observers.size(), 10);
	}

	/**
	 * Tests the fire of the event inside a transaction
	 */
	@Test
	public void testSimpleTransaction() {
		try {
			userTransaction.begin();
			Bill bill = fire();
			assertEquals(
					"The id generation passes through the always and it is incremented only by inprogess always observer method",
					1, bill.getId());
			userTransaction.commit();
			assertEquals(
					"The id generation passes through the always and it is incremented only by transactional observer methods",
					4, bill.getId());
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			fail("no fail for the transaction");
		}

	}

	/**
	 * Tests the fire of the event inside a failed transaction
	 */
	@Test
	public void testFailedTransaction() {
		Bill bill = null;
		try {
			userTransaction.begin();
			bill = fire();
			assertEquals(
					"The id generation passes through the always and it is incremented only by inprogess always observer method",
					1, bill.getId());
			throw new RollbackException();
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException
				| RollbackException e) {
			try {
				userTransaction.rollback();
				assertEquals(
						"The id generation passes through the always and it is incremented only by transactional failure observer methods",
						3, bill.getId());
			} catch (SystemException se) {

			}
		}

	}

	private Bill fire() {
		Bill bill = new Bill();
		bill.setId(0);
		bill.setTitle("ticket for the concert of Franco Battiato");
		billEvent.fire(bill);
		return bill;
	}
}
