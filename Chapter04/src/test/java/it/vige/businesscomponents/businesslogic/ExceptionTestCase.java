package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.businesslogic.exception.RequiredBean;
import it.vige.businesscomponents.businesslogic.exception.RequiredException;

@RunWith(Arquillian.class)
public class ExceptionTestCase {

	private static final Logger logger = getLogger(ExceptionTestCase.class.getName());

	@Inject
	private UserTransaction userTransaction;
	
	@EJB
	private RequiredBean requiredBean;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "exception-test.jar");
		jar.addPackage(RequiredException.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/persistence-test.xml")),
				"persistence.xml");
		return jar;
	}

	@Test(expected = EJBException.class)
	public void testCounterViaRemoteInterface() throws Exception {
		logger.info("starting exception test");
		try {
			userTransaction.begin();
			requiredBean.setValue(3);
			requiredBean.throwRequiredException();
			userTransaction.commit();
			fail("an exception should be throwed");
		} catch (RequiredException ex) {
		}
		requiredBean.getValue(3);
	}
}
