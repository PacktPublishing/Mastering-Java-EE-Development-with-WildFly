package it.vige.businesscomponents.persistence;

import static it.vige.businesscomponents.persistence.Default.createJavaArchive;
import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
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

import it.vige.businesscomponents.persistence.inheritance.Car;
import it.vige.businesscomponents.persistence.inheritance.Dog;
import it.vige.businesscomponents.persistence.inheritance.Pet;
import it.vige.businesscomponents.persistence.inheritance.Vehicle;

@RunWith(Arquillian.class)
public class InheritanceTestCase {

	private static final Logger logger = getLogger(InheritanceTestCase.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserTransaction userTransaction;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		return createJavaArchive("inheritance-test.jar", Pet.class.getPackage(), Forum.class.getPackage());
	}

	/**
	 * Tests annotation literals in a jar archive
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testSingleTable() {
		logger.info("starting single table inheritance test");
		Pet pet = new Pet("jackrussell", "dog");
		Dog dog = new Dog("mastino", "dog");
		try {
			userTransaction.begin();
			entityManager.persist(pet);
			entityManager.persist(dog);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		List<Pet> petsFromDB = entityManager.createNativeQuery("select * from pet").getResultList();
		assertEquals("only the pet table exists", 2, petsFromDB.size());
	}

	/**
	 * Tests annotation literals in a jar archive
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testTablePerClass() {
		logger.info("starting table per class inheritance test");
		Vehicle vehicle = new Vehicle("peugeot", "car");
		Car car = new Car("fiat", "car");
		try {
			userTransaction.begin();
			entityManager.persist(vehicle);
			entityManager.persist(car);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		List<Vehicle> vehiclesFromDB = entityManager.createNativeQuery("select * from vehicle").getResultList();
		assertEquals("the vehicle table exists", 1, vehiclesFromDB.size());
		List<Car> carsFromDB = entityManager.createNativeQuery("select * from car").getResultList();
		assertEquals("the car table exists", 1, carsFromDB.size());
	}

	/**
	 * Tests annotation literals in a jar archive
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testJoin() {
		logger.info("starting joined inheritance event test");
		Watch watch = new Watch();
		TopicWatch topicWatch = new TopicWatch();
		try {
			userTransaction.begin();
			entityManager.persist(watch);
			entityManager.persist(topicWatch);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		List<Watch> watchesFromDB = entityManager.createNativeQuery("select * from JBP_FORUMS_WATCH").getResultList();
		assertEquals("the watch table exists", 2, watchesFromDB.size());
		List<TopicWatch> topicWatchesFromDB = entityManager.createNativeQuery("select * from JBP_FORUMS_TOPICSWATCH")
				.getResultList();
		assertEquals("the topic watch table exists", 1, topicWatchesFromDB.size());
	}
}
