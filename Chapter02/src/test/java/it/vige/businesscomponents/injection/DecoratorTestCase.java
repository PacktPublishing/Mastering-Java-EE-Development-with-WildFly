package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.decorator.Coder;
import it.vige.businesscomponents.injection.decorator.CoderImpl;

@RunWith(Arquillian.class)
public class DecoratorTestCase {

	private static final Logger logger = getLogger(DecoratorTestCase.class.getName());

	@Inject
	private Coder coder;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "decorator-test.jar");
		jar.addPackage(Coder.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/beans-decorator.xml")),
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
		assertEquals("Unexpected greeting received from bean", "\"Hello\" becomes \"hi\", 5 characters in length",
				value);
		assertEquals("A decorator may inject metadata about the bean it is decorating", CoderImpl.class,
				coder.getBean().getBeanClass());

	}
}
