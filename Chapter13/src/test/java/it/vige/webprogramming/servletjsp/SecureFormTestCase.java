package it.vige.webprogramming.servletjsp;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import it.vige.webprogramming.servletjsp.SecureFormTestCase.SecureResourcesSetupTask;

@RunWith(Arquillian.class)
@ServerSetup(SecureResourcesSetupTask.class)
public class SecureFormTestCase {

	private static final String WEBAPP_SRC = "src/main/webapp";

	private static final Logger logger = getLogger(SecureFormTestCase.class.getName());

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

	private HtmlForm loginForm;

	private WebClient webClient;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = create(WebArchive.class)
				.addAsWebResource(new File(WEBAPP_SRC + "/view", "secure-form.jsp"), "view/secure-form.jsp")
				.addAsWebResource(new File(WEBAPP_SRC + "/view", "loginerror.jsp"), "view/loginerror.jsp")
				.addAsWebResource(new File(WEBAPP_SRC + "/view", "loginform.jsp"), "view/loginform.jsp")
				.addAsWebInfResource((new File("src/test/resources/web-form.xml")), "web.xml");
		return war;
	}

	@Before
	public void setup() throws IOException {
		webClient = new WebClient();
		HtmlPage page = webClient.getPage(base + "view/secure-form.jsp");
		loginForm = page.getForms().get(0);
	}

	@Test
	public void testGetWithCorrectCredentials() throws Exception {
		logger.info("start get request with correct credentials");
		loginForm.getInputByName("j_username").setValueAttribute("u1");
		loginForm.getInputByName("j_password").setValueAttribute("p1");
		HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
		HtmlPage page2 = submitButton.click();
		assertEquals("Form-based Security - Success", page2.getTitleText());
	}

	@Test
	public void testGetWithIncorrectCredentials() throws Exception {
		logger.info("start get request with incorrect credentials");
		loginForm.getInputByName("j_username").setValueAttribute("random");
		loginForm.getInputByName("j_password").setValueAttribute("random");
		HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
		HtmlPage page2 = submitButton.click();
		assertEquals("Form-Based Login Error Page", page2.getTitleText());
	}
}
