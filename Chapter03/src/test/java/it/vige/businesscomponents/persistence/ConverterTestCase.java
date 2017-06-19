package it.vige.businesscomponents.persistence;

import static it.vige.businesscomponents.persistence.Default.createJavaArchive;
import static it.vige.businesscomponents.persistence.converter.PasswordConverter.NEVER_DO_IT;
import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import it.vige.businesscomponents.persistence.converter.Citizen;

@RunWith(Arquillian.class)
public class ConverterTestCase {

	private static final Logger logger = getLogger(ConverterTestCase.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserTransaction userTransaction;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		return createJavaArchive("converter-test.jar", Citizen.class.getPackage());
	}

	@Test
	public void testPasswordConverter() {
		logger.info("starting persistence converter test");
		Citizen citizen = new Citizen();
		citizen.setPassword("prova");
		try {
			userTransaction.begin();
			entityManager.persist(citizen);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		Citizen citizenFromDB = (Citizen) entityManager.createQuery("from citizen").getSingleResult();
		assertEquals("the password is always converted by the converter", "prova", citizenFromDB.getPassword());
		assertEquals("this is the password we have in the database", "cHJvdmE=", NEVER_DO_IT);
	}
}
