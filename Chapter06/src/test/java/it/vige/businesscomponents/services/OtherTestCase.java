package it.vige.businesscomponents.services;

import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.HEAD;
import static javax.ws.rs.HttpMethod.OPTIONS;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.URL;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.services.otherhttp.HttpApplication;

@RunWith(Arquillian.class)
public class OtherTestCase {

	private static final Logger logger = getLogger(OtherTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "other-test.war");
		war.addPackage(HttpApplication.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		return war;
	}

	@Test
	@RunAsClient
	public void testJaxRSOptionsDeleteTypes() throws Exception {
		logger.info("start JaxRS options delete test");
		Client client = newClient();
		WebTarget target = client.target(url + "services/receiver/options");
		Response response = target.request().options();
		String calledMethod = response.getHeaderString("calledMethod");
		double value = response.readEntity(Double.class);
		assertEquals("options implemented: ", 88.99, value, 0.0);
		client.close();
		assertEquals("The filter registerCall is called only for @Logged services", OPTIONS, calledMethod);
		client = newClient();
		target = client.target(url + "services/receiver/delete");
		response = target.request().delete();
		calledMethod = response.getHeaderString("calledMethod");
		value = response.readEntity(Double.class);
		assertEquals("delete implemented: ", 99.66, value, 0.0);
		client.close();
		assertEquals("The filter registerCall is called only for @Logged services", DELETE, calledMethod);
		client = newClient();
		target = client.target(url + "services/receiver/header");
		Builder builder = target.request().header("my_new_header", "Hi all");
		response = builder.get();
		calledMethod = response.getHeaderString("calledMethod");
		String valueStr = response.readEntity(String.class);
		assertEquals("head implemented: ", "Hi all", valueStr);
		client.close();
		assertNotEquals("The filter registerCall is called only for @Logged services", HEAD, calledMethod);
		client = newClient();
		target = client.target(url + "services/receiver/headerWithContext");
		builder = target.request(TEXT_PLAIN).header("my_new_header", "Hi allll");
		response = builder.get();
		calledMethod = response.getHeaderString("calledMethod");
		valueStr = response.readEntity(String.class);
		assertEquals("head implemented: ", "Hi allll", valueStr);
		client.close();
		assertNotEquals("The filter registerCall is called only for @Logged services", HEAD, calledMethod);
	}

}
