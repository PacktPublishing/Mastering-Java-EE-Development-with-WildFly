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

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

import it.vige.webprogramming.servletjsp.simple.SimpleServlet;

@RunWith(Arquillian.class)
public class SimpleServletTestCase {

	@ArquillianResource
	private URL base;

	private WebClient webClient;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = create(WebArchive.class).addClass(SimpleServlet.class);
		return war;
	}

	@Before
	public void setup() {
		webClient = new WebClient();
	}

	@Test
	public void testGet() throws IOException, SAXException {
		TextPage page = webClient.getPage(base + "SimpleServlet");
		assertEquals("my GET", page.getContent());
	}

	@Test
	public void testPost() throws IOException, SAXException {
		WebRequest request = new WebRequest(new URL(base + "SimpleServlet"), HttpMethod.POST);
		TextPage page = webClient.getPage(request);
		assertEquals("my POST", page.getContent());
	}
}
