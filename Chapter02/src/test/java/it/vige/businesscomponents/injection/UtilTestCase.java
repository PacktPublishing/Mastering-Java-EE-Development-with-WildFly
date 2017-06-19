package it.vige.businesscomponents.injection;

import static it.vige.businesscomponents.injection.util.ConfigurationKey.DEFAULT_DIRECTORY;
import static java.util.Arrays.asList;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.util.ConfigurationBean;
import it.vige.businesscomponents.injection.util.ConfigurationKey;
import it.vige.businesscomponents.injection.util.ConfigurationValue;

@RunWith(Arquillian.class)
public class UtilTestCase {

	private static final Logger logger = getLogger(UtilTestCase.class.getName());

	@Inject
	private BeanManager beanManager;

	@Inject
	@ConfigurationValue(key = DEFAULT_DIRECTORY)
	private String defaultDirectory;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "event-test.jar");
		jar.addPackage(ConfigurationKey.class.getPackage());
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	/**
	 * Tests annotation literals in a jar archive
	 */
	@Test
	public void testAnnotationLiteral() {
		logger.info("starting util event test");
		Set<Bean<?>> beans = beanManager.getBeans(ConfigurationBean.class, new AnnotationLiteral<Default>() {

			private static final long serialVersionUID = -4378964126487759035L;
		});
		assertEquals("The configuration bean has by default the @Default qualifier and it is a ManagedBean", 1,
				beans.size());
	}

	/**
	 * Tests type literals in a jar archive
	 */
	@Test
	public void testTypeLiteral() {
		Type listBooleans = new TypeLiteral<List<Boolean>>() {

			private static final long serialVersionUID = 1228070460716823527L;
		}.getType();
		List<Type> types = asList(Integer.class, String.class, listBooleans);
		assertEquals("The configuration bean has by default the @Default qualifier and it is a ManagedBean", 3,
				types.size());
		assertEquals("This type is a list of booleans", listBooleans.toString(), types.get(2).getTypeName());
	}

	/**
	 * Tests non binding in a jar archive
	 */
	@Test
	public void testNonBinding() throws NoSuchMethodException {
		assertEquals(
				"Verify if the NonBinding works. Through the NonBinding, the container will consider the injection with the ConfigurationValue equals to the producer ConfigurationBean. Without the NonBinding, the producer is not actived",
				"/user/test", defaultDirectory);
	}
}
