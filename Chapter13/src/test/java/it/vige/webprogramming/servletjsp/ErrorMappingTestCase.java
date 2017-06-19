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

import it.vige.webprogramming.servletjsp.errormapping.ErrorMappingServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class ErrorMappingTestCase {

	private static final Logger logger = getLogger(ErrorMappingTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Drone
	private WebDriver driver;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "errormappings-test.war");
		war.addPackage(ErrorMappingServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/errormapping.jsp")), "view/errormapping.jsp");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/error-404.jsp")), "view/error-404.jsp");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/error-exception.jsp")),
				"view/error-exception.jsp");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web-errormapping.xml")), "web.xml");
		return war;
	}

	@Test
	public void testErrorMapping() throws Exception {
		logger.info("start error mappings test");
		driver.get(url + "view/errormapping.jsp");
		driver.findElement(xpath("html/body/a")).click();
		String textPage1 = driver.findElement(xpath("html/body")).getText();
		assertTrue("contains 404 error: ", textPage1.contains("Error Mapping Sample - 404 page not found"));
		driver.findElement(xpath("html/body/a")).click();
		driver.findElement(xpath("html/body/a[2]")).click();
		String textPage2 = driver.findElement(xpath("html/body")).getText();
		assertTrue("contains exception: ", textPage2.contains("Error Mapping Sample - Exception Mapping"));
	}

}
