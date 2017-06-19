package it.vige.webprogramming.servletjsp;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.xpath;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.webprogramming.servletjsp.webfragment.WebFragmentServlet;

@RunWith(Arquillian.class)
public class WebFragmentTestCase {

	private static final Logger logger = getLogger(WebFragmentTestCase.class.getName());

	@Deployment(testable = false)
	public static WebArchive deploy() throws URISyntaxException {
		WebArchive war = create(WebArchive.class).addPackage(WebFragmentServlet.class.getPackage())
				.addAsLibrary(new File("target/servlets-jsp/WEB-INF/lib/web-fragment.jar"), "web-fragment.jar");
		return war;
	}

	@ArquillianResource
	private URL url;

	@Drone
	private WebDriver driver;

	@Test
	public void testWebFragment() throws Exception {
		logger.info("start web fragment test");
		driver.get(url + "logging/WebFragmentServlet");
		String text = driver.findElement(xpath("html/body")).getText();
		assertTrue("servlet executed: ", text.startsWith("Web Fragment with output from Servlet Filter"));
		assertTrue("filter executed: ", text.contains("This has been appended by an intrusive filter"));
	}

}
