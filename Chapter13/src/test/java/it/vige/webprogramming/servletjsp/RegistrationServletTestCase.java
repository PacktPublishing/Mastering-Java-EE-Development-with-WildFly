package it.vige.webprogramming.servletjsp;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

import it.vige.webprogramming.servletjsp.registration.RegistrationContextListener;
import it.vige.webprogramming.servletjsp.registration.RegistrationDynamicServlet;

@RunWith(Arquillian.class)
public class RegistrationServletTestCase {

	@ArquillianResource
	private URL base;

	private WebClient webClient;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = create(WebArchive.class).addClass(RegistrationDynamicServlet.class)
				.addClass(RegistrationContextListener.class);
		return war;
	}

	@Before
	public void setup() {
		webClient = new WebClient();
	}

	@Test
	public void testChildServlet() throws IOException, SAXException {
		TextPage page = webClient.getPage(base + "dynamic");
		assertEquals("dynamic GET", page.getContent());
	}
}
