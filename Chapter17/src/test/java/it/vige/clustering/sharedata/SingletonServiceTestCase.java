package it.vige.clustering.sharedata;

import static it.vige.clustering.sharedata.singleton.HAServiceActivator.DEFAULT_SERVICE_NAME;
import static it.vige.clustering.sharedata.singleton.HAServiceActivator.QUORUM_SERVICE_NAME;
import static it.vige.clustering.sharedata.singleton.HAServiceServlet.createURI;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;
import static org.jboss.as.test.http.util.TestHttpClientUtils.promiscuousCookieHttpClient;
import static org.jboss.as.test.shared.integration.ejb.security.PermissionUtils.createPermissionsXmlAsset;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.server.security.ServerPermission;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.clustering.sharedata.singleton.HAService;
import it.vige.clustering.sharedata.singleton.HAServiceActivator;
import it.vige.clustering.sharedata.singleton.HAServiceServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class SingletonServiceTestCase extends ClusterAbstractTestCase {

	@Deployment(name = DEPLOYMENT_1, managed = false, testable = false)
	@TargetsContainer(CONTAINER_1)
	public static Archive<?> deployment0() {
		return createDeployment();
	}

	@Deployment(name = DEPLOYMENT_2, managed = false, testable = false)
	@TargetsContainer(CONTAINER_2)
	public static Archive<?> deployment1() {
		return createDeployment();
	}

	private static Archive<?> createDeployment() {
		WebArchive war = create(WebArchive.class, "singleton.war");
		war.addPackage(HAService.class.getPackage());
		war.setManifest(new StringAsset("Manifest-Version: 1.0\nDependencies: org.jboss.as.server\n"));
		war.addAsManifestResource(createPermissionsXmlAsset(new RuntimePermission("getClassLoader"), // See
																										// org.jboss.as.server.deployment.service.ServiceActivatorProcessor#deploy()
				new ServerPermission("useServiceRegistry"), // See
															// org.jboss.as.server.deployment.service.SecuredServiceRegistry
				new ServerPermission("getCurrentServiceContainer")), "permissions.xml");
		war.addAsServiceProvider(ServiceActivator.class, HAServiceActivator.class);
		return war;
	}

	@Test
	public void testSingletonService(
			@ArquillianResource(HAServiceServlet.class) @OperateOnDeployment(DEPLOYMENT_1) URL baseURL1,
			@ArquillianResource(HAServiceServlet.class) @OperateOnDeployment(DEPLOYMENT_2) URL baseURL2)
			throws IOException, URISyntaxException {

		// Needed to be able to inject ArquillianResource
		stop(CONTAINER_2);

		try (CloseableHttpClient client = promiscuousCookieHttpClient()) {
			HttpResponse response = client.execute(new HttpGet(createURI(baseURL1, DEFAULT_SERVICE_NAME, NODE_1)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_1, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL1, QUORUM_SERVICE_NAME)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertNull(response.getFirstHeader("node"));
			} finally {
				closeQuietly(response);
			}

			start(CONTAINER_2);

			response = client.execute(new HttpGet(createURI(baseURL1, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL1, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			stop(CONTAINER_2);

			response = client.execute(new HttpGet(createURI(baseURL1, DEFAULT_SERVICE_NAME, NODE_1)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_1, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL1, QUORUM_SERVICE_NAME)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertNull(response.getFirstHeader("node"));
			} finally {
				closeQuietly(response);
			}

			start(CONTAINER_2);

			response = client.execute(new HttpGet(createURI(baseURL1, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL1, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			stop(CONTAINER_1);

			response = client.execute(new HttpGet(createURI(baseURL2, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, QUORUM_SERVICE_NAME)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertNull(response.getFirstHeader("node"));
			} finally {
				closeQuietly(response);
			}

			start(CONTAINER_1);

			response = client.execute(new HttpGet(createURI(baseURL1, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL1, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, DEFAULT_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}

			response = client.execute(new HttpGet(createURI(baseURL2, QUORUM_SERVICE_NAME, NODE_2)));
			try {
				assertEquals(SC_OK, response.getStatusLine().getStatusCode());
				assertEquals(NODE_2, response.getFirstHeader("node").getValue());
			} finally {
				closeQuietly(response);
			}
		}
	}
}
