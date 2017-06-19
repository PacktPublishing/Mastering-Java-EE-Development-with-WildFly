package it.vige.webprogramming.servletjsp;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.xpath;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

import it.vige.webprogramming.servletjsp.fileupload.FileUploadServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class FileUploadTestCase {

	private static final Logger logger = getLogger(FileUploadTestCase.class.getName());

	@ArquillianResource
	private URL base;

	@Drone
	private WebDriver driver;

	@Deployment(testable = false)
	public static WebArchive deploy() throws URISyntaxException {
		final WebArchive war = create(WebArchive.class, "fileupload-test.war");
		war.addPackage(FileUploadServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebResource(new FileAsset(new File("src/main/webapp/view/fileupload.jsp")), "view/fileupload.jsp");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void uploadFile() throws IOException, URISyntaxException {
		logger.info("start file upload test");
		driver.get(base + "view/fileupload.jsp");
		driver.findElement(xpath("html/body/form/input")).sendKeys("/tmp/fake-image.jpg");
		driver.findElement(xpath("html/body/form/input[2]")).click();
		String textPage = driver.findElement(xpath("html/body")).getText();
		assertTrue("File upload executed: ", textPage.contains("Received 1 parts"));
	}

}
