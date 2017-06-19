package it.vige.businesscomponents.services;

import static it.vige.businesscomponents.services.RegisterOperation.calledMethod;
import static java.util.Arrays.asList;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RestTestCase {

	private static final Logger logger = getLogger(RestTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "rest-test.war");
		war.addPackage(Calculator.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		return war;
	}

	@Test
	public void testJaxRSGet() throws Exception {
		logger.info("start JaxRS Get test");
		Client client = newClient();
		WebTarget target = client.target(url + "services/calculator/sum?value=4&value=6");
		Response response = target.request().get();
		double value = response.readEntity(Double.class);
		response.close();
		assertEquals("sum implemented: ", 10.0, value, 0.0);
		assertEquals("The filter registerOperation is called", GET, calledMethod);
	}

	@Test
	public void testJaxRSPost() throws Exception {
		logger.info("start JaxRS Post test");
		Client client = newClient();
		WebTarget target = client.target(url + "services/calculator/multi");
		double value = 0.0;
		Response response = null;
		try {
			Entity<double[]> valuesAsArray = entity(new double[] { 4.5, 6.7 }, TEXT_PLAIN);
			target.request().post(valuesAsArray);
			fail();
		} catch (ProcessingException ex) {
			assertEquals("Arrays not supported", "RESTEASY004655: Unable to invoke request", ex.getMessage());
		}
		try {
			Entity<List<Double>> valuesAsList = entity(asList(new Double[] { 4.5, 6.7 }), TEXT_PLAIN);
			response = target.request().post(valuesAsList);
			value = response.readEntity(Double.class);
			fail();
		} catch (ProcessingException ex) {
			assertEquals("TEXT_PLAIN not supported",
					"RESTEASY003145: Unable to find a MessageBodyReader of content-type */* and type class java.lang.Double",
					ex.getMessage());
		} finally {
			response.close();
		}

		Entity<List<Double>> valuesAsList = entity(asList(new Double[] { 4.5, 6.7 }), APPLICATION_JSON);
		response = target.request().post(valuesAsList);
		value = response.readEntity(Double.class);
		response.close();
		assertEquals("sum implemented: ", 30.150000000000002, value, 0.0);
		assertEquals("The filter registerOperation is called", POST, calledMethod);
	}

	@Test
	public void testRestEasyGet() throws Exception {
		logger.info("start REST Easy Get test");
		ResteasyClient client = new ResteasyClientBuilder().build();
		WebTarget target = client.target(url + "services/calculator/sub?value=40&value=6");
		Response response = target.request().get();
		double value = response.readEntity(Double.class);
		response.close();
		assertEquals("subtract implemented: ", 34.0, value, 0.0);
		assertEquals("The filter registerOperation is called", GET, calledMethod);
	}

	@Test
	public void testJaxRSPut() throws Exception {
		logger.info("start JaxRS other request types test");
		Client client = newClient();
		WebTarget target = client.target(url + "services/calculator/div");
		Entity<List<Double>> valuesAsList = entity(asList(new Double[] { 4.5, 6.7 }), APPLICATION_JSON);
		Response response = target.request(APPLICATION_JSON_TYPE).put(valuesAsList);
		double value = response.readEntity(Double.class);
		response.close();
		client.close();
		assertEquals("sum implemented: ", 0.6716417910447761, value, 0.0);
		assertEquals("The filter registerOperation is called", PUT, calledMethod);
	}

	@Test
	public void testSSLContext() throws Exception {
		logger.info("start SSL Context test");
		Client client = newClient();
		HostnameVerifier hostnameVerifier = client.getHostnameVerifier();
		assertNotNull("Java Utility", hostnameVerifier);
		assertNull("no SSL by default", client.getSslContext());
	}

}
