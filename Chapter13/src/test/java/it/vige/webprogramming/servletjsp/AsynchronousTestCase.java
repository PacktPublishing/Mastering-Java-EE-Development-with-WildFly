package it.vige.webprogramming.servletjsp;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.vige.webprogramming.servletjsp.async.AsynchronousServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class AsynchronousTestCase {

	private static final Logger logger = getLogger(AsynchronousTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Drone
	private WebDriver driver;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "asynchronous-test.war");
		war.addPackage(AsynchronousServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testAsynchronous() throws Exception {
		logger.info("start asynchronous test");
		URL urlServlet = new URL(url + "AsynchronousServlet");
		HttpURLConnection conn = (HttpURLConnection) urlServlet.openConnection();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			assertEquals("first received row", "running", input.readLine());
			assertEquals("second received row", "onComplete", input.readLine());
			input.close();
		}
	}

}
