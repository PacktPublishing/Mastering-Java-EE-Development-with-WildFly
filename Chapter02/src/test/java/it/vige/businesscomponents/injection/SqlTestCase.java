package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.sql.DataBean;

@RunWith(Arquillian.class)
public class SqlTestCase {

	private static final Logger logger = getLogger(SqlTestCase.class.getName());

	@Inject
	private DataBean dataBean;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "sql-test.jar");
		jar.addPackage(DataBean.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/main/resources/META-INF/MANIFEST.MF")), "MANIFEST.MF");
		return jar;
	}

	/**
	 * Tests simple sql injection in a jar archive
	 */
	@Test
	public void testSqlData1() {
		doConnection(dataBean.getMyData1());
	}

	/**
	 * Tests simple sql injection in a jar archive
	 */
	@Test
	public void testSqlData2() {
		doConnection(dataBean.getMyData2());
	}

	private void doConnection(DataSource dataSource) {
		try (Connection connection = dataSource.getConnection()) {
		} catch (SQLException e) {
			fail("We need a working connection here");
			logger.info("I cannot connect");
		}
	}
}
