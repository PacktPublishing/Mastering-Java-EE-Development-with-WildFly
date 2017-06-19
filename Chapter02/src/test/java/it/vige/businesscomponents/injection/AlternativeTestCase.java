package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.alternative.CoderBrutalImpl;
import it.vige.businesscomponents.injection.decorator.Coder;

@RunWith(Arquillian.class)
public class AlternativeTestCase {

	private static final Logger logger = getLogger(AlternativeTestCase.class.getName());

	@Inject
	private Coder coder;

	@Inject
	private BeanManager beanManager;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "alternative-test.jar");
		jar.addPackage(Coder.class.getPackage());
		jar.addPackage(CoderBrutalImpl.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/beans-alternative.xml")),
				"beans.xml");
		return jar;
	}

	/**
	 * Tests simple decorator injection in a jar archive
	 */
	@Test
	public void testDecorator() {
		logger.info("starting a weld engine in container mode");
		String value = coder.codeString("Hello", 3);
		assertEquals("Unexpected greeting received from bean", "hiiiiiiiii", value);
	}

	/**
	 * Tests specializes injection in a jar archive
	 */
	@Test
	public void testSpecializes() {
		logger.info("starting a weld engine in container mode with specializes");
		Bean<?> bean = beanManager.getBeans(CoderBrutalImpl.class).iterator().next();
		assertEquals(
				"CoderBrutalImpl should inherit the SessionScope because has the Specializes annotation. Specializes works only for the alternative",
				3, bean.getQualifiers().size());
	}
}
