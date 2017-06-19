package it.vige.webprogramming.servletjsp;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static com.gargoylesoftware.htmlunit.HttpMethod.PUT;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.vige.webprogramming.servletjsp.SecureServletTestCase.SecureResourcesSetupTask;
import it.vige.webprogramming.servletjsp.secure.LoginServlet;
import it.vige.webprogramming.servletjsp.secure.SecureServlet;

@RunWith(Arquillian.class)
@ServerSetup(SecureResourcesSetupTask.class)
public class SecureServletTestCase {

	private static final Logger logger = getLogger(SecureServletTestCase.class.getName());

	static class SecureResourcesSetupTask implements ServerSetupTask {

		@Override
		public void setup(ManagementClient managementClient, String containerId) throws Exception {
			copy(new File("src/test/resources/application-users.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-users.properties")
							.toPath(),
					REPLACE_EXISTING);
			copy(new File("src/test/resources/application-roles.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-roles.properties")
							.toPath(),
					REPLACE_EXISTING);
		}

		@Override
		public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
		}
	}

	@ArquillianResource
	private URL base;

	private WebClient webClient;
	private DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
	private DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = create(WebArchive.class).addClass(SecureServlet.class).addClass(LoginServlet.class)
				.addAsWebInfResource((new File("src/test/resources/web-secure.xml")), "web.xml");
		return war;
	}

	@Before
	public void setup() throws IOException {
		webClient = new WebClient();
		correctCreds.addCredentials("u1", "p1");
		incorrectCreds.addCredentials("random", "random");
	}

	@Test
	public void testGetWithCorrectCredentials() throws Exception {
		webClient.setCredentialsProvider(correctCreds);
		TextPage page = webClient.getPage(base + "SecureServlet");
		assertEquals("my GET", page.getContent());
		try {
			page = webClient.getPage(base + "SecureOmissionServlet");
			fail("GET method could be called even with omission-http-methods");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(404, e.getStatusCode());
		}
		try {
			page = webClient.getPage(base + "SecureDenyUncoveredServlet");
			fail("GET method could be called even with deny-uncovered-http-methods");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(404, e.getStatusCode());
		}
	}

	@Test
	public void testGetWithIncorrectCredentials() throws Exception {
		webClient.setCredentialsProvider(incorrectCreds);
		try {
			webClient.getPage(base + "SecureServlet");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(401, e.getStatusCode());
			return;
		}
		fail("/SecureServlet could be accessed without proper security credentials");
		try {
			webClient.getPage(base + "SecureOmissionServlet");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(401, e.getStatusCode());
			return;
		}
		fail("/SecureOmissionServlet could be accessed without proper security credentials");
	}

	@Test
	public void testPostWithCorrectCredentials() throws Exception {
		logger.info("start post with correct credentials");
		webClient.setCredentialsProvider(correctCreds);
		WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
		TextPage page = webClient.getPage(request);
		assertEquals("my POST", page.getContent());
		request = new WebRequest(new URL(base + "SecureOmissionServlet"), POST);
		try {
			TextPage p = webClient.getPage(request);
			logger.info(p.getContent());
			fail("POST method could be called even with omission-http-methods");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(405, e.getStatusCode());
		}
		assertEquals("my POST", page.getContent());
		request = new WebRequest(new URL(base + "SecureDenyUncoveredServlet"), POST);
		try {
			TextPage p = webClient.getPage(request);
			logger.info(p.getContent());
			fail("POST method could be called even with deny-uncovered-http-methods");
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(405, e.getStatusCode());
			return;
		}
	}

	@Test
	public void testPostWithIncorrectCredentials() throws Exception {
		logger.info("start post with incorrect credentials");
		webClient.setCredentialsProvider(incorrectCreds);
		WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
		try {
			webClient.getPage(request);
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(401, e.getStatusCode());
			return;
		}
		fail("/SecureServlet could be accessed without proper security credentials");
		request = new WebRequest(new URL(base + "SecureOmissionServlet"), POST);
		TextPage page = webClient.getPage(request);
		assertEquals("my POST", page.getContent());
	}

	@Test
	public void testPutWithCorrectCredentials() throws Exception {
		logger.info("start put with correct credentials");
		webClient.setCredentialsProvider(correctCreds);
		WebRequest request = new WebRequest(new URL(base + "SecureDenyUncoveredServlet"), PUT);
		try {
			TextPage p = webClient.getPage(request);
			logger.info(p.getContent());
		} catch (FailingHttpStatusCodeException e) {
			assertNotNull(e);
			assertEquals(405, e.getStatusCode());
			return;
		}
		fail("PUT method could be called even with deny-unocovered-http-methods");
	}

	@Test
	public void testUnauthenticatedRequest() throws IOException, SAXException {
		logger.info("start unauthenticated request");
		HtmlPage page = webClient.getPage(base + "LoginServlet");
		String responseText = page.asText();
		assertTrue("Is User in Role", responseText.contains("isUserInRole?false"));
		assertTrue("Get Remote User", responseText.contains("getRemoteUser?null"));
		assertTrue("Get User Principal", responseText.contains("getUserPrincipal?null"));
		assertTrue("Get Auth Type", responseText.contains("getAuthType?null"));
	}

	@Test
	public void testAuthenticatedRequest() throws IOException, SAXException {
		logger.info("start authenticated request");
		WebRequest request = new WebRequest(new URL(base + "LoginServlet?user=u1&password=p1"), HttpMethod.GET);
		WebResponse response = webClient.getWebConnection().getResponse(request);
		String responseText = response.getContentAsString();
		logger.info(responseText);

		assertTrue("Is user in Role", responseText.contains("isUserInRole?true"));
		assertTrue("Get Remote User", responseText.contains("getRemoteUser?u1"));
		assertTrue("Get User Principal", responseText.contains("getUserPrincipal?u1"));
		assertTrue("Get Auth Type", responseText.contains("getAuthType?BASIC"));
	}
}
