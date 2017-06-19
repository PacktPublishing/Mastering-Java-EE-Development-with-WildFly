package it.vige.businesscomponents.injection;

import static it.vige.businesscomponents.injection.common.CommonBean.HELLO_GREETING_PREFIX;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.common.CommonBean;
import it.vige.businesscomponents.injection.common.CommonManagedBean;
import it.vige.businesscomponents.injection.common.NamedBean;

/**
 * Tests that the Resource injection as specified by Java EE spec works as
 * expected
 * <p/>
 * User: Luca Stancapiano
 */
@RunWith(Arquillian.class)
public class ContainerInjectionTestCase {

	private static final Logger logger = getLogger(ContainerInjectionTestCase.class.getName());

	final String user = "Charlie Sheen";

	@Inject
	private CommonBean cb;

	@Inject
	private CommonManagedBean cmb;

	@Inject
	@Named("my_named_test")
	private NamedBean nb;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "resource-injection-test.jar");
		jar.addPackage(CommonBean.class.getPackage());
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	/**
	 * Tests simple resource injection in a jar archive
	 */
	@Test
	public void testResourceInjection() {
		logger.info("starting a weld engine in container mode");
		final String greeting = cb.sayHello(user);
		assertEquals("Unexpected greeting received from bean", HELLO_GREETING_PREFIX + user, greeting);
	}

	/**
	 * Tests simple resource injection in a jar archive
	 */
	@Test
	public void testBeanManager() {
		logger.info("starting a weld engine in container mode");
		BeanManager beanManager = cb.getBeanManager();
		assertNotNull("verify there is ever a bean manager", beanManager);
		Set<Bean<?>> beans = beanManager.getBeans(CommonBean.class);
		assertEquals("One injected common bean", 1, beans.size());
		@SuppressWarnings("unchecked")
		Bean<CommonBean> bean = (Bean<CommonBean>) beanManager.resolve(beans);
		CommonBean cb = beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
		final String greeting = cb.sayHello(user);
		assertEquals("Unepxected greeting received from bean", HELLO_GREETING_PREFIX + user, greeting);
	}

	@Test
	public void testManagedInjection() {
		logger.info("starting a weld engine with a managed bean");
		final String greeting = cmb.sayHello(user);
		assertEquals("Unexpected greeting received from bean", HELLO_GREETING_PREFIX + user, greeting);
	}

	@Test
	public void testNamedInjection() {
		logger.info("starting a weld engine with a managed bean");
		final double prize = nb.giveMeThePrize();
		assertEquals("Unexpected greeting received from bean", 5.6, prize, 0.0);
	}
}
