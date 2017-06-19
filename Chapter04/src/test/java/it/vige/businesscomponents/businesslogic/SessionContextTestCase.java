package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.businesslogic.context.MyData;
import it.vige.businesscomponents.businesslogic.context.nnn.EngineLocal;
import it.vige.businesscomponents.businesslogic.context.nnn.EngineRemote;
import it.vige.businesscomponents.businesslogic.context.nnn.StateEngineLocal;
import it.vige.businesscomponents.businesslogic.context.nnn.StateEngineRemote;
import it.vige.businesscomponents.businesslogic.context.old.BeanCallingOtherBeans;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21Local;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21LocalHome;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21Remote;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21RemoteHome;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateLocal;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateLocalHome;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateRemote;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateRemoteHome;

@RunWith(Arquillian.class)
public class SessionContextTestCase {

	private static final Logger logger = getLogger(SessionContextTestCase.class.getName());

	@Inject
	private UserTransaction userTransaction;

	@EJB
	private BeanCallingOtherBeans beanCallingOtherBeans;

	@EJB
	private EngineRemote engineRemote;

	@EJB
	private StateEngineRemote stateEngineRemote;

	@EJB
	private Ejb21Remote ejb21EngineRemote;

	@EJB
	private Ejb21RemoteHome ejb21EngineRemoteHome;

	@EJB
	private Ejb21StateRemote ejb21StateEngineRemote;

	@EJB
	private Ejb21StateRemoteHome ejb21StateEngineRemoteHome;

	@EJB
	private EngineLocal engineLocal;

	@EJB
	private StateEngineLocal stateEngineLocal;

	@EJB
	private Ejb21Local ejb21EngineLocal;

	@EJB
	private Ejb21LocalHome ejb21EngineLocalHome;

	@EJB
	private Ejb21StateLocal ejb21StateEngineLocal;

	@EJB
	private Ejb21StateLocalHome ejb21StateEngineLocalHome;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "session-context-test.jar");
		jar.addPackages(true, MyData.class.getPackage());
		return jar;
	}

	@Test
	public void testStatelessRemoteNaming() throws Exception {
		logger.info("starting session remote stateless test");

		logger.info(engineRemote + "");
		int result = engineRemote.go(1);
		assertEquals(engineRemote.getSpeed(), 1);
		logger.info(result + "");
		logger.info(engineRemote + "");
		assertEquals(engineRemote.getSpeed(), 1);
		engineRemote.add(new MyData());
		engineRemote.log();
	}

	@Test
	public void testStatefulRemoteNaming() throws Exception {
		logger.info("starting session remote stateful test");

		logger.info(stateEngineRemote + "");
		int result = stateEngineRemote.go(1);
		assertEquals(stateEngineRemote.getSpeed(), 1);
		logger.info(result + "");
		logger.info(stateEngineRemote + "");
		assertEquals(stateEngineRemote.getSpeed(), 1);
		stateEngineRemote.add(new MyData());
		stateEngineRemote.log();
	}

	@Test
	public void testEjb21StatelessRemoteNaming() throws Exception {
		logger.info("starting ejb21 remote stateless test");

		logger.info(ejb21EngineRemote + "");
		int result = ejb21EngineRemote.go(1);
		assertEquals(ejb21EngineRemote.getSpeed(), 1);
		logger.info(result + "");
		logger.info(ejb21EngineRemote + "");
		assertEquals(ejb21EngineRemote.getSpeed(), 1);
		ejb21EngineRemote.add(new MyData());
		ejb21EngineRemote.log();
		Ejb21Remote ejb21EngineRemote2 = ejb21EngineRemoteHome.create();
		assertEquals("interfaces are the same", ejb21EngineRemote, ejb21EngineRemote2);
	}

	@Test
	public void testEjb21StatefulRemoteNaming() throws Exception {
		logger.info("starting ejb21 remote stateful test");

		logger.info(ejb21StateEngineRemote + "");
		int result = ejb21StateEngineRemote.go(1);
		assertEquals(ejb21StateEngineRemote.getSpeed(), 1);
		logger.info(result + "");
		logger.info(ejb21StateEngineRemote + "");
		assertEquals(ejb21StateEngineRemote.getSpeed(), 1);
		ejb21StateEngineRemote.add(new MyData());
		ejb21StateEngineRemote.log();
		Ejb21StateRemote ejb21StateEngineRemote2 = ejb21StateEngineRemoteHome.create();
		assertNotEquals("interfaces are not the same", ejb21StateEngineRemote, ejb21StateEngineRemote2);
		Ejb21StateRemote ejb21StateEngineRemote3 = ejb21StateEngineRemoteHome.create("input data");
		assertNotEquals("interfaces are not the same", ejb21StateEngineRemote2, ejb21StateEngineRemote3);
		Ejb21StateRemote ejb21StateEngineRemote4 = ejb21StateEngineRemoteHome.create(new ArrayList<String>());
		assertNotEquals("interfaces are not the same", ejb21StateEngineRemote3, ejb21StateEngineRemote4);
	}

	@Test
	public void testStatelessLocalNaming() throws Exception {
		logger.info("starting session local stateless test");

		logger.info(engineLocal + "");
		int result = engineLocal.go(1);
		assertEquals(engineLocal.getSpeed(), 1);
		logger.info(result + "");
		logger.info(engineLocal + "");
		assertEquals(engineLocal.getSpeed(), 1);
		engineLocal.add(new MyData());
		engineLocal.log();
	}

	@Test
	public void testStatefulLocalNaming() {
		logger.info("starting session local stateful test");
		try {
			userTransaction.begin();
			logger.info(stateEngineLocal + "");
			int result = stateEngineLocal.go(1);
			assertEquals(stateEngineLocal.getSpeed(), 1);
			logger.info(result + "");
			logger.info(stateEngineLocal + "");
			assertEquals(stateEngineLocal.getSpeed(), 1);
			stateEngineLocal.add(new MyData());
			stateEngineLocal.log();
			userTransaction.commit();
		} catch (Exception ex) {
			try {
				userTransaction.rollback();
			} catch (IllegalStateException | SecurityException | SystemException e) {
				logger.severe("extreme error. The rollback doesn't work");
			}
		}
	}

	@Test
	public void testEjb21StatelessLocalNaming() throws Exception {
		logger.info("starting ejb21 local stateless test");

		logger.info(ejb21EngineLocal + "");
		int result = ejb21EngineLocal.go(1);
		assertEquals(ejb21EngineLocal.getSpeed(), 1);
		logger.info(result + "");
		logger.info(ejb21EngineLocal + "");
		assertEquals(ejb21EngineLocal.getSpeed(), 1);
		ejb21EngineLocal.add(new MyData());
		ejb21EngineLocal.log();
		Ejb21Local ejb21EngineLocal2 = ejb21EngineLocalHome.create();
		assertEquals("interfaces are the same", ejb21EngineLocal, ejb21EngineLocal2);
	}

	@Test
	public void testEjb21StatefulLocalNaming() throws Exception {
		logger.info("starting ejb21 local stateful test");

		logger.info(ejb21StateEngineLocal + "");
		int result = ejb21StateEngineLocal.go(1);
		assertEquals(ejb21StateEngineLocal.getSpeed(), 1);
		logger.info(result + "");
		logger.info(ejb21StateEngineLocal + "");
		assertEquals(ejb21StateEngineLocal.getSpeed(), 1);
		ejb21StateEngineLocal.add(new MyData());
		ejb21StateEngineLocal.log();
		Ejb21StateLocal ejb21StateEngineLocal2 = ejb21StateEngineLocalHome.create();
		assertNotEquals("interfaces are not the same", ejb21StateEngineLocal, ejb21StateEngineLocal2);
		Ejb21StateLocal ejb21StateEngineLocal3 = ejb21StateEngineLocalHome.create("input data");
		assertNotEquals("interfaces are not the same", ejb21StateEngineLocal2, ejb21StateEngineLocal3);
		Ejb21StateLocal ejb21StateEngineLocal4 = ejb21StateEngineLocalHome.create(new ArrayList<String>());
		assertNotEquals("interfaces are not the same", ejb21StateEngineLocal3, ejb21StateEngineLocal4);
	}

	public void testEJBs() throws Exception {
		logger.info("starting ejbs test");
		assertNotNull(beanCallingOtherBeans.getEjb21Local());
		assertNotNull(beanCallingOtherBeans.getEjb21StateLocal());
	}
}
