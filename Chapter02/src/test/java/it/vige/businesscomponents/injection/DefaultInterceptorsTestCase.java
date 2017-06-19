package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.interceptor.DefaultInterceptor;
import it.vige.businesscomponents.injection.interceptor.service.SimpleHome;
import it.vige.businesscomponents.injection.interceptor.service.SimpleInterface;
import it.vige.businesscomponents.injection.interceptor.service.SimpleStatelessBean;

@RunWith(Arquillian.class)
public class DefaultInterceptorsTestCase {

	private static final Logger logger = getLogger(DefaultInterceptorsTestCase.class.getName());

	@ArquillianResource
	private InitialContext iniCtx;

	@Deployment
	public static WebArchive createEJBDeployment() {
		final WebArchive war = create(WebArchive.class, "interceptors-ejb-test.war");
		war.addClasses(DefaultInterceptor.class, SimpleStatelessBean.class, SimpleInterface.class, SimpleHome.class,
				DefaultInterceptorsTestCase.class);
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/META-INF/ejb-jar.xml")), "ejb-jar.xml");
		return war;
	}

	@Test
	public void testWithDefaultInterceptor() throws NamingException {
		final String hello = "Hello World";
		final SimpleHome home = (SimpleHome) iniCtx.lookup("java:module/SimpleStateless!" + SimpleHome.class.getName());
		final SimpleInterface ejbInstance = home.createSimple();
		logger.info("executed jndi call");
		ejbInstance.setText(hello);
		assertEquals(SimpleStatelessBean.executed, false);
		assertEquals(hello, ejbInstance.getText());
		assertEquals(SimpleStatelessBean.executed, true);
	}

}
