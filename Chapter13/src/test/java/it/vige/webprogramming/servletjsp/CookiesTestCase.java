package it.vige.webprogramming.servletjsp;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.xpath;

import java.io.File;
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

import it.vige.webprogramming.servletjsp.cookies.CookiesServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class CookiesTestCase {

	private static final Logger logger = getLogger(CookiesTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Drone
	private WebDriver driver;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "cookies-test.war");
		war.addPackage(CookiesServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/cookies.jsp")), "view/cookies.jsp");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/index-cookies.jsp")),
				"view/index-cookies.jsp");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testCookies() throws Exception {
		logger.info("start cookies test");
		driver.get(url + "view/cookies.jsp");
		driver.findElement(xpath("html/body/a")).click();
		String textPage1 = driver.findElement(xpath("html/body")).getText();
		assertTrue("the page result is: ", textPage1.contains("Found cookie: JSESSIONID"));
		driver.findElement(xpath("html/body/a")).click();
		String textPage2 = driver.findElement(xpath("html/body")).getText();
		assertTrue("the page result is: ", textPage2.contains("myCookieKey=myCookieValue"));
		assertTrue("the page result is: ", textPage2.contains("JSESSIONID="));
	}

}
