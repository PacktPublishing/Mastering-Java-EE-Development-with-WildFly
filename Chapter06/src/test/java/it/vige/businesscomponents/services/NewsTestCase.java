package it.vige.businesscomponents.services;

import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.id;
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

import it.vige.businesscomponents.news.Param;

@RunWith(Arquillian.class)
@RunAsClient
public class NewsTestCase {

	private static final Logger logger = getLogger(NewsTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Drone
	private WebDriver driver;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "news-test.war");
		war.addPackage(Param.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebResource(new FileAsset(new File("src/test/resources/index.html")), "index.html");
		war.addAsWebResource(new FileAsset(new File("src/test/resources/form.html")), "form.html");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testJaxRSPostWithBean() throws Exception {
		logger.info("start JaxRS post with bean test");
		driver.get(url + "");
		driver.findElement(id("data")).sendKeys("my_data");
		driver.findElement(xpath("html/body/form/input[2]")).click();
		assertEquals("the page result is: ", "6|my_data|" + APPLICATION_FORM_URLENCODED,
				driver.findElement(xpath("html/body")).getText());
	}

	@Test
	public void testJaxRSPostWithForm() throws Exception {
		logger.info("start JaxRS post with form test");
		driver.get(url + "form.html");
		driver.findElement(id("data")).sendKeys("my_data");
		driver.findElement(xpath("html/body/form/input[2]")).click();
		assertEquals("the page result is: ", "my_data", driver.findElement(xpath("html/body")).getText());
	}

}
