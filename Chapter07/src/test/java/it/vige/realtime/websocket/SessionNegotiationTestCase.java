package it.vige.realtime.websocket;

import static io.undertow.websockets.core.WebSocketVersion.V13;
import static it.vige.realtime.websocket.session.SecureSocketClient.awake;
import static it.vige.realtime.websocket.negotiation.MessageEndpoint.sessionServer;
import static java.util.logging.Logger.getLogger;
import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.Extension;
import javax.websocket.Extension.Parameter;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.websocket.negotiation.AnnotatedClientEndpointWithConfigurator;
import it.vige.realtime.websocket.session.SessionSocketServer;

@RunWith(Arquillian.class)
public class SessionNegotiationTestCase {

	private static final Logger logger = getLogger(SessionNegotiationTestCase.class.getName());

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "negotiation-test.war");
		war.addPackage(AnnotatedClientEndpointWithConfigurator.class.getPackage());
		war.addPackage(SessionSocketServer.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		return war;
	}

	@Test
	public void testSessionNegotiation() throws Exception {
		logger.info("start websocket server negotiation test");
		session();
	}

	private void session() throws Exception {
		connect();
		assertFalse("id: ", sessionServer.getId().isEmpty());
		assertEquals("negotiatedSubprotocol: ", "configured-proto", sessionServer.getNegotiatedSubprotocol());
		Set<Session> sessions = sessionServer.getOpenSessions();
		assertEquals("openSessions: ", 1, sessions.size());
		assertEquals("client sessions are: ", sessionServer, sessions.iterator().next());
		Map<String, String> pathParameters = sessionServer.getPathParameters();
		assertEquals("pathParameters: ", 1, pathParameters.size());
		for (String key : pathParameters.keySet()) {
			assertEquals("the path parameter key: ", "user", key);
			assertEquals("the path parameter value: ", "Bob", pathParameters.get(key));
		}
		assertEquals("protocolVersion: ", V13.toHttpHeaderValue(), sessionServer.getProtocolVersion());
		assertEquals("queryString: ", "my_request_id=35", sessionServer.getQueryString());
		Map<String, List<String>> requestParameters = sessionServer.getRequestParameterMap();
		assertEquals("requestParameters: ", 1, requestParameters.size());
		for (String key : requestParameters.keySet()) {
			assertEquals("requestParameters key: ", "my_request_id", key);
			List<String> values = requestParameters.get(key);
			assertEquals("requestParameters value: ", 1, values.size());
			assertEquals("requestParameters value: ", "35", values.get(0));
		}
		assertEquals("requestURI: ", "/negotiation-test/chat/Bob?my_request_id=35", sessionServer.getRequestURI() + "");
		assertNull("userPrincipal: ", sessionServer.getUserPrincipal());
		Map<String, Object> userProperties = sessionServer.getUserProperties();
		assertTrue("userProperties: ", userProperties.isEmpty());
		for (String key : userProperties.keySet()) {
			logger.info("userProperties: " + key + " - " + userProperties.get(key));
		}
		assertFalse("secure: ", sessionServer.isSecure());
		assertTrue("open: ", sessionServer.isOpen());
		List<Extension> negotiatedExtensions = sessionServer.getNegotiatedExtensions();
		assertTrue("negotiatedExtensions: ", negotiatedExtensions.isEmpty());
		extensions(negotiatedExtensions.iterator());
	}

	private void extensions(Iterator<Extension> extensions) {
		while (extensions.hasNext()) {
			Extension extension = extensions.next();
			logger.info("extension name: " + extension.getName());
			List<Parameter> parameters = extension.getParameters();
			for (Parameter parameter : parameters) {
				logger.info("extension parameter name: " + parameter.getName());
				logger.info("extension parameter value: " + parameter.getValue());
			}
		}
	}

	private void connect() throws Exception {
		while (sessionServer != null && !sessionServer.isOpen()) {
			break;
		}

		final WebSocketContainer serverContainer = getWebSocketContainer();
		URI uri = new URI("ws://localhost:8080/negotiation-test/chat/Bob?my_request_id=35");
		serverContainer.connectToServer(AnnotatedClientEndpointWithConfigurator.class, uri);
		awake();
	}
}
