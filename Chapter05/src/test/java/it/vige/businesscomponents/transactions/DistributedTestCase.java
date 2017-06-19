package it.vige.businesscomponents.transactions;

import static it.vige.businesscomponents.transactions.distributed.Format.BOLD;
import static it.vige.businesscomponents.transactions.distributed.Format.ITALIC;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentActionResult;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.transactions.DistributedTestCase.DistributedTestCaseSetup;
import it.vige.businesscomponents.transactions.distributed.Image;
import it.vige.businesscomponents.transactions.distributed.Magazine;
import it.vige.businesscomponents.transactions.distributed.Text;

@RunWith(Arquillian.class)
@ServerSetup(DistributedTestCaseSetup.class)
public class DistributedTestCase {

	private static final Logger logger = getLogger(DistributedTestCase.class.getName());

	private static final String TEXTS_DS_XML = "texts-ds.xml";

	@Inject
	private UserTransaction userTransaction;

	@EJB(mappedName = "java:module/Images")
	private Magazine<Image> images;

	@EJB(mappedName = "java:module/Texts")
	private Magazine<Text> texts;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "distributed-test.jar");
		jar.addPackage(Magazine.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/distributed/persistence-magazine.xml")),
				"persistence.xml");
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/MANIFEST.MF")), "MANIFEST.MF");
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	static class DistributedTestCaseSetup implements ServerSetupTask {

		@Override
		public void setup(final ManagementClient managementClient, final String containerId) throws Exception {
			final ServerDeploymentManager manager = ServerDeploymentManager.Factory
					.create(managementClient.getControllerClient());
			final DeploymentPlan plan = manager.newDeploymentPlan()
					.add(new File("src/test/resources/distributed/" + TEXTS_DS_XML)).andDeploy().build();
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
			final DeploymentPlan undeployPlan = manager.newDeploymentPlan().undeploy(TEXTS_DS_XML).andRemoveUndeployed()
					.build();
			manager.execute(undeployPlan).get();
		}
	}

	private void insertBlock() {
		images.write(new Image("image_title_1.jpg", "First title".getBytes()));
		texts.write(new Text(BOLD, "starting first chapter"));
		texts.write(new Text(ITALIC, "starting first paragraph"));
		images.write(new Image("image_title_2.jpg", "Second title".getBytes()));
		texts.write(new Text(BOLD, "starting second chapter"));
		texts.write(new Text(ITALIC, "starting first paragraph"));
		images.write(new Image("image_title_1.jpg", "Third title".getBytes()));
		texts.write(new Text(BOLD, "starting third chapter"));
	}

	@Test
	public void testMultipleDatabases() throws Exception {
		logger.info("start multiple databases ok test");
		userTransaction.begin();
		insertBlock();
		userTransaction.rollback();
		assertEquals("no images are inserted", 0, images.read().size());
		assertEquals("no texts are inserted", 0, texts.read().size());
		userTransaction.begin();
		insertBlock();
		userTransaction.commit();
		assertEquals("all images are inserted", 3, images.read().size());
		assertEquals("all texts are inserted", 5, texts.read().size());
	}
}
