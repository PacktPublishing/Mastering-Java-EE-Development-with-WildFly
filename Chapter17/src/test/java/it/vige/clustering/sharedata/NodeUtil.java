package it.vige.clustering.sharedata;

import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.logging.Logger;

public final class NodeUtil {

	private static final Logger log = Logger.getLogger(NodeUtil.class);

	public static void deploy(Deployer deployer, String... deployments) {
		for (String deployment : deployments) {
			log.info("Deploying deployment=" + deployment);
			deployer.deploy(deployment);
		}
	}

	public static void undeploy(Deployer deployer, String... deployments) {
		for (String deployment : deployments) {
			log.info("Undeploying deployment=" + deployment);
			deployer.undeploy(deployment);
		}
	}

	public static void start(ContainerController controller, Deployer deployer, String container, String deployment) {
		try {
			log.info("Starting deployment=" + deployment + ", container=" + container);
			controller.start(container);
			deployer.deploy(deployment);
			log.info("Started deployment=" + deployment + ", container=" + container);
		} catch (Throwable e) {
			log.error("Failed to start container(s)", e);
		}
	}

	public static void start(ContainerController controller, String... containers) {
		// TODO do this in parallel.
		for (String container : containers) {
			try {
				log.info("Starting deployment=NONE, container=" + container);
				controller.start(container);
			} catch (Throwable e) {
				log.error("Failed to start containers", e);
			}
		}
	}

	public static void stop(ContainerController controller, String... containers) {
		for (String container : containers) {
			try {
				log.info("Stopping container=" + container);
				controller.stop(container);
				log.info("Stopped container=" + container);
			} catch (Throwable e) {
				log.error("Failed to stop containers", e);
			}
		}
	}

	public static void stop(ContainerController controller, Deployer deployer, String container, String deployment) {
		try {
			log.info("Stopping deployment=" + deployment + ", container=" + container);
			deployer.undeploy(deployment);
			controller.stop(container);
			log.info("Stopped deployment=" + deployment + ", container=" + container);
		} catch (Throwable e) {
			log.error("Failed to stop containers", e);
		}
	}

	private NodeUtil() {
	}
}
