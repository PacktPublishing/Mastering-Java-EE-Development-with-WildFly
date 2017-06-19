package it.vige.realtime.websocket;

import static io.undertow.util.Headers.SEC_WEB_SOCKET_PROTOCOL_STRING;
import static io.undertow.websockets.core.WebSocketVersion.V13;
import static io.undertow.websockets.jsr.DefaultWebSocketClientSslProvider.SSL_CONTEXT;
import static it.vige.realtime.websocket.session.SecureSocketClient.awake;
import static it.vige.realtime.websocket.session.SessionSocketServer.sessionServer;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.logging.Logger.getLogger;
import static javax.net.ssl.SSLContext.getInstance;
import static javax.websocket.ClientEndpointConfig.Builder.create;
import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.Extension;
import javax.websocket.Extension.Parameter;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.websocket.session.SecureSocketClient;
import it.vige.realtime.websocket.session.SessionSocketServer;

@RunWith(Arquillian.class)
public class SessionSecureTestCase {

	private static final Logger logger = getLogger(SessionSecureTestCase.class.getName());

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "secure-test.war");
		war.addPackage(SessionSocketServer.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/secure-web.xml")), "web.xml");
		war.addAsWebResource(new FileAsset(new File("src/test/resources/index.html")), "index.html");
		return war;
	}

	@Test
	public void testSessionSecure() throws Exception {
		logger.info("start websocket server session test");
		session();
	}

	private void session() throws Exception {
		connect();
		assertFalse("id: ", sessionServer.getId().isEmpty());
		assertTrue("negotiatedSubprotocol: ", sessionServer.getNegotiatedSubprotocol().isEmpty());
		Set<Session> sessions = sessionServer.getOpenSessions();
		assertEquals("openSessions: ", 1, sessions.size());
		assertEquals("client sessions are: ", sessionServer, sessions.iterator().next());
		Map<String, String> pathParameters = sessionServer.getPathParameters();
		assertTrue("pathParameters: ", pathParameters.isEmpty());
		for (String key : pathParameters.keySet()) {
			logger.info("pathParameters: " + key + " - " + pathParameters.get(key));
		}
		assertEquals("protocolVersion: ", V13.toHttpHeaderValue(), sessionServer.getProtocolVersion());
		assertNull("queryString: ", sessionServer.getQueryString());
		Map<String, List<String>> requestParameters = sessionServer.getRequestParameterMap();
		assertTrue("requestParameters: ", requestParameters.isEmpty());
		for (String key : requestParameters.keySet()) {
			logger.info("requestParameters: " + key + " - " + requestParameters.get(key));
		}
		assertEquals("requestURI: ", "/secure-test/session", sessionServer.getRequestURI() + "");
		assertNull("userPrincipal: ", sessionServer.getUserPrincipal());
		Map<String, Object> userProperties = sessionServer.getUserProperties();
		assertTrue("userProperties: ", userProperties.isEmpty());
		for (String key : userProperties.keySet()) {
			logger.info("userProperties: " + key + " - " + userProperties.get(key));
		}
		assertTrue("secure: ", sessionServer.isSecure());
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
		SSLContext context = createSSLContext();
		SecureSocketClient endpoint = new SecureSocketClient();

		Configurator configurator = new Configurator() {

			@Override
			public void beforeRequest(Map<String, List<String>> headers) {
				headers.put(SEC_WEB_SOCKET_PROTOCOL_STRING, singletonList("configured-proto"));
			}

		};
		ClientEndpointConfig clientEndpointConfig = create().configurator(configurator)
				.preferredSubprotocols(asList(new String[] { "foo", "bar", "configured-proto" })).build();
		clientEndpointConfig.getUserProperties().put(SSL_CONTEXT, context);

		final WebSocketContainer serverContainer = getWebSocketContainer();
		URI uri = new URI("wss://127.0.0.1:8443/secure-test/session");
		serverContainer.connectToServer(endpoint, clientEndpointConfig, uri);
		awake();
	}

	private SSLContext createSSLContext() throws Exception {
		SSLContext sslContext = getInstance("TLS");

		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);

		return sslContext;
	}
}
