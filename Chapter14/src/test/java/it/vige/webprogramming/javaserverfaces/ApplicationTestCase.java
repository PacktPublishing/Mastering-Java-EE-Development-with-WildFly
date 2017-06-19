package it.vige.webprogramming.javaserverfaces;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.ResourceBundle.getBundle;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentActionResult;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import it.vige.webprogramming.javaserverfaces.ApplicationTestCase.ConfiguredTestCaseSetup;

@RunWith(Arquillian.class)
@ServerSetup(ConfiguredTestCaseSetup.class)
public class ApplicationTestCase {

	private static final Logger logger = getLogger(ApplicationTestCase.class.getName());

	private static final String FORUMS_DS_XML = "forums-ds.xml";
	private static final String WEBAPP_SRC = "src/main/webapp";

	@ArquillianResource
	private URL base;

	private WebClient webClient;
	private ResourceBundle resourceBundle;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = create(WebArchive.class);
		File[] files = resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
				.resolve("it.vige:rubia-forums-ejb:2.2.1").withTransitivity().asFile();
		war.addAsLibraries(files);
		war.addPackages(true, ApplicationTestCase.class.getPackage())
				.addAsWebInfResource((new File("src/main/resources", "default_graphics/theme.properties")),
						"classes/default_graphics/theme.properties")
				.addAsWebInfResource((new File("src/main/resources", "ResourceJSF.properties")),
						"classes/ResourceJSF.properties")
				.addAsWebInfResource((new File("src/main/resources", "ResourceJSF_it.properties")),
						"classes/ResourceJSF_it.properties")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/category", "viewcategory_body.xhtml"),
						"views/category/viewcategory_body.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/admin", "index.xhtml"), "views/admin/index.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/admin", "deletecategory.xhtml"),
						"views/admin/deletecategory.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/category", "viewcategory_body.xhtml"),
						"views/category/viewcategory_body.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/common", "common.xhtml"), "views/common/common.xhtml")
				.addAsWebResource(new File(WEBAPP_SRC + "/views/common", "common_decoration.xhtml"),
						"views/common/common_decoration.xhtml")
				.addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")), "web.xml")
				.addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "faces-config.xml")), "faces-config.xml")
				.addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "forums.taglib.xml")), "forums.taglib.xml")
				.addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "beans.xml")), "beans.xml")
				.addAsManifestResource((new File("src/test/resources", "MANIFEST.MF")), "MANIFEST.MF");
		return war;
	}

	static class ConfiguredTestCaseSetup implements ServerSetupTask {

		@Override
		public void setup(final ManagementClient managementClient, final String containerId) throws Exception {
			copy(new File("src/test/resources/application-users.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-users.properties")
							.toPath(),
					REPLACE_EXISTING);
			copy(new File("src/test/resources/application-roles.properties").toPath(),
					new File("target/wildfly-10.1.0.Final/standalone/configuration/application-roles.properties")
							.toPath(),
					REPLACE_EXISTING);
			final ServerDeploymentManager manager = ServerDeploymentManager.Factory
					.create(managementClient.getControllerClient());
			final DeploymentPlan plan = manager.newDeploymentPlan().add(new File("src/test/resources/" + FORUMS_DS_XML))
					.andDeploy().build();
			final Future<ServerDeploymentPlanResult> future = manager.execute(plan);
			final ServerDeploymentPlanResult result = future.get(20, SECONDS);
			final ServerDeploymentActionResult actionResult = result.getDeploymentActionResult(plan.getId());
			if (actionResult != null) {
				final Throwable deploymentException = actionResult.getDeploymentException();
				if (deploymentException != null) {
					throw new RuntimeException(deploymentException);
				}
			}
		}

		@Override
		public void tearDown(final ManagementClient managementClient, final String containerId) throws Exception {
			final ServerDeploymentManager manager = ServerDeploymentManager.Factory
					.create(managementClient.getControllerClient());
			final DeploymentPlan undeployPlan = manager.newDeploymentPlan().undeploy(FORUMS_DS_XML)
					.andRemoveUndeployed().build();
			manager.execute(undeployPlan).get();
		}
	}

	@Before
	public void setup() throws IOException {
		webClient = new WebClient();

		DefaultCredentialsProvider creds = new DefaultCredentialsProvider();
		creds.addCredentials("root", "p1");
		webClient.setCredentialsProvider(creds);
		resourceBundle = getBundle("ResourceJSF");
	}

	@Test
	public void testCategoryOperations() throws Exception {
		logger.info("start insert category");
		HtmlPage adminPage = webClient.getPage(base + "views/admin/index.xhtml");
		HtmlAnchor button = adminPage.getAnchors().get(2);
		HtmlPage page = button.click();
		HtmlForm addCategory = page.getFormByName("addCategoryForm");
		String categoryToInsert = "ccccc";
		addCategory.getInputByName("addCategoryForm:Category").setValueAttribute(categoryToInsert);
		HtmlSubmitInput submitButton = addCategory.getInputByName("addCategoryForm:editinline");
		page = submitButton.click();
		assertTrue("The category is created", page.asText().contains(categoryToInsert));
		HtmlAnchor adminPanel = adminPage.getAnchors().get(1);
		page = adminPanel.click();
		button = page.getAnchors().get(6);
		page = button.click();
		HtmlForm editCategory = page.getForms().get(4);
		String categoryToUpdate = "aaaaaa";
		String firstCategory = "Dummy demo category";
		editCategory.getInputByValue(categoryToInsert).setValueAttribute(categoryToUpdate);
		submitButton = editCategory.getInputByValue(resourceBundle.getString("Update"));
		page = submitButton.click();
		assertTrue("The category is updated", page.asText().contains(categoryToUpdate));
		button = page.getAnchors().get(7);
		page = button.click();
		assertTrue("The category is arrowed up",
				page.asText().indexOf(categoryToUpdate) < page.asText().indexOf(firstCategory));
		button = page.getAnchors().get(4);
		page = button.click();
		assertTrue("The category is arrowed down",
				page.asText().indexOf(categoryToUpdate) > page.asText().indexOf(firstCategory));
		button = page.getAnchors().get(8);
		page = button.click();
		HtmlForm deleteCategory = page.getForms().get(1);
		submitButton = deleteCategory.getInputByValue(resourceBundle.getString("Cancel"));
		page = submitButton.click();
		assertTrue("The delete is canceled",
				page.asText().contains(firstCategory) && page.asText().contains(categoryToUpdate));
		button = page.getAnchors().get(0);
		page = button.click();
		assertTrue("In the home page there are two categories",
				page.asText().contains(firstCategory) && page.asText().contains(categoryToUpdate));
		button = page.getAnchors().get(1);
		page = button.click();
		button = page.getAnchors().get(8);
		page = button.click();
		deleteCategory = page.getForms().get(1);
		submitButton = deleteCategory.getInputByValue(resourceBundle.getString("Confirm"));
		page = submitButton.click();
		assertTrue("The delete is confirmed", page.asText()
				.contains("\"" + categoryToUpdate + "\" " + resourceBundle.getString("Category_deleted_1")));
	}
}
