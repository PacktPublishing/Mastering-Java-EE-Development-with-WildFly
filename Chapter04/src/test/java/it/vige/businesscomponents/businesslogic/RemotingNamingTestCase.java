package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.SECURITY_CREDENTIALS;
import static javax.naming.Context.SECURITY_PRINCIPAL;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.businesslogic.remote.Machine;
import it.vige.businesscomponents.businesslogic.remote.StateMachine;

@RunWith(Arquillian.class)
public class RemotingNamingTestCase {

	private static final Logger logger = getLogger(RemotingNamingTestCase.class.getName());

	private Context context = null;

	@Deployment
	public static EnterpriseArchive createEJBDeployment() {
		final EnterpriseArchive ear = create(EnterpriseArchive.class, "remoting-remote-naming-test.ear");
		final JavaArchive jar = create(JavaArchive.class, "remoting-remote-naming-test.jar");
		jar.addPackage(Machine.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/ejb-jar-remoting.xml")),
				"ejb-jar.xml");
		ear.addAsModule(jar);
		return ear;
	}

	@Test
	@RunAsClient
	public void testStatelessRemoteNaming() throws Exception {
		logger.info("starting remoting ejb client test");

		try {
			createInitialContext();
			Machine machine = lookup(Machine.class, "machine");
			logger.info(machine + "");
			int result = machine.go(1);
			assertEquals(machine.getSpeed(), 1);
			logger.info(result + "");
			machine = lookup(Machine.class, "machine");
			logger.info(machine + "");
			assertEquals(machine.getSpeed(), 1);
		} finally {
			closeContext();
		}
	}

	@Test
	@RunAsClient
	public void testStatefulRemoteNaming() throws Exception {
		logger.info("starting remoting ejb client test");

		try {
			createInitialContext();
			StateMachine machine = lookup(StateMachine.class, "stateMachine");
			logger.info(machine + "");
			int result = machine.go(1);
			assertEquals(machine.getSpeed(), 1);
			logger.info(result + "");
			machine = lookup(StateMachine.class, "stateMachine");
			logger.info(machine + "");
			assertEquals(machine.getSpeed(), 0);
		} finally {
			closeContext();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T lookup(Class<T> machine, String beanName) throws NamingException {

		final String appName = "remoting-remote-naming-test";
		final String moduleName = "remoting";
		final String distinctName = "";
		final String viewClassName = machine.getName();

		return (T) context
				.lookup("" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
	}

	private void createInitialContext() throws NamingException {
		Properties prop = new Properties();

		prop.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		prop.put(PROVIDER_URL, "http-remoting://127.0.0.1:8080");
		prop.put(SECURITY_PRINCIPAL, "admin");
		prop.put(SECURITY_CREDENTIALS, "secret123!");
		prop.put("jboss.naming.client.ejb.context", true);

		context = new InitialContext(prop);
	}

	private void closeContext() throws NamingException {
		if (context != null) {
			context.close();
		}
	}
}
