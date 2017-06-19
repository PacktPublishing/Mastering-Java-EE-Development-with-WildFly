package it.vige.realtime.websocket;

import static it.vige.realtime.websocket.intro.WebSocketClient.getMessage;
import static it.vige.realtime.websocket.intro.WebSocketClient.postConstructCalled;
import static it.vige.realtime.websocket.intro.WebSocketClient.reset;
import static java.lang.Thread.sleep;
import static java.util.logging.Logger.getLogger;
import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.websocket.intro.WebSocketClient;
import it.vige.realtime.websocket.intro.WebSocketServer;

@RunWith(Arquillian.class)
public class IntroTestCase {

	private static final Logger logger = getLogger(IntroTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "intro-test.war");
		war.addPackage(WebSocketServer.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testServerFirstCall() throws Exception {
		logger.info("start websocket server first call test");
		reset();

		final WebSocketContainer serverContainer = getWebSocketContainer();
		serverContainer.connectToServer(WebSocketClient.class, url.toURI().resolve("chat"));

		assertEquals("the message is sent at the start of client", "Server received [Hello]", getMessage());
		sleep(30000);
		assertEquals("It comes from the server. First", "Message from server", getMessage());
		assertEquals("It comes from the server. Second", "Message from server", getMessage());
		assertEquals("It comes from the server. Third", "Message from server", getMessage());
		assertNull("no other messages", getMessage());

		assertTrue("PostConstruct method on client endpoint instance not called.", postConstructCalled);
	}
}
