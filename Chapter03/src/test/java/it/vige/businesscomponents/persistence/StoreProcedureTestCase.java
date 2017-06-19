package it.vige.businesscomponents.persistence;

import static javax.persistence.ParameterMode.IN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class StoreProcedureTestCase {

	@PersistenceContext
	private EntityManager entityManager;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "stored-test.jar");
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/persistence-store.xml")),
				"persistence.xml");
		jar.addAsResource(new FileAsset(new File("src/test/resources/store.import.sql")), "store.import.sql");
		return jar;
	}

	@Test
	public void testCallStoreProcedure() {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("my_sum");
		query.registerStoredProcedureParameter("x", Integer.class, IN);
		query.registerStoredProcedureParameter("y", Integer.class, IN);

		query.setParameter("x", 5);
		query.setParameter("y", 4);
		query.execute();
		Integer sum = (Integer) query.getSingleResult();
		assertEquals("the sum in the stored procedure", sum, new Integer(9));
	}
}
