package it.vige.realtime.websocket;

import static io.undertow.websockets.core.WebSocketVersion.V13;
import static it.vige.realtime.websocket.session.SessionMessageHandler.sentPong;
import static it.vige.realtime.websocket.session.SessionSocketClient.awake;
import static it.vige.realtime.websocket.session.SessionSocketClient.sessionClient;
import static java.nio.ByteBuffer.allocate;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.websocket.ContainerProvider.getWebSocketContainer;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Extension;
import javax.websocket.Extension.Parameter;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.realtime.websocket.session.SessionMessageHandler;
import it.vige.realtime.websocket.session.SessionSocketClient;
import it.vige.realtime.websocket.session.SessionSocketServer;

@RunWith(Arquillian.class)
public class SessionClientTestCase {

	private static final Logger logger = getLogger(SessionClientTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "session-test.war");
		war.addPackage(SessionSocketServer.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testSessionClient() throws Exception {
		logger.info("start websocket client session test");
		session();
	}

	private void session() throws Exception {
		connect();
		assertFalse("id: ", sessionClient.getId().isEmpty());
		assertTrue("negotiatedSubprotocol: ", sessionClient.getNegotiatedSubprotocol().isEmpty());
		Set<Session> sessions = sessionClient.getOpenSessions();
		assertTrue("openSessions: ", sessions.isEmpty());
		Map<String, String> pathParameters = sessionClient.getPathParameters();
		assertTrue("pathParameters: ", pathParameters.isEmpty());
		for (String key : pathParameters.keySet()) {
			logger.info("pathParameters: " + key + " - " + pathParameters.get(key));
		}
		assertEquals("protocolVersion: ", V13.toHttpHeaderValue(), sessionClient.getProtocolVersion());
		assertNull("queryString: ", sessionClient.getQueryString());
		Map<String, List<String>> requestParameters = sessionClient.getRequestParameterMap();
		assertTrue("requestParameters: ", requestParameters.isEmpty());
		for (String key : requestParameters.keySet()) {
			logger.info("requestParameters: " + key + " - " + requestParameters.get(key));
		}
		assertEquals("requestURI: ", "http://127.0.0.1:8080/session-test/session", sessionClient.getRequestURI() + "");
		assertNull("userPrincipal: ", sessionClient.getUserPrincipal());
		Map<String, Object> userProperties = sessionClient.getUserProperties();
		assertTrue("userProperties: ", userProperties.isEmpty());
		for (String key : userProperties.keySet()) {
			logger.info("userProperties: " + key + " - " + userProperties.get(key));
		}
		assertFalse("secure: ", sessionClient.isSecure());
		assertTrue("open: ", sessionClient.isOpen());
		List<Extension> negotiatedExtensions = sessionClient.getNegotiatedExtensions();
		assertTrue("negotiatedExtensions: ", negotiatedExtensions.isEmpty());
		extensions(negotiatedExtensions.iterator());
		async();
		basic();
		connect();
		container();
		messageHandlers();
		max();
	}

	private void container() {
		WebSocketContainer container = sessionClient.getContainer();
		assertEquals("defaultAsyncSendTimeout: ", 0, container.getDefaultAsyncSendTimeout());
		assertEquals("defaultMaxBinaryMessageBufferSize: ", 0, container.getDefaultMaxBinaryMessageBufferSize());
		assertEquals("defaultMaxSessionIdleTimeout: ", 0, container.getDefaultMaxSessionIdleTimeout());
		assertEquals("defaultMaxTextMessageBufferSize: ", 0, container.getDefaultMaxTextMessageBufferSize());
		container.setAsyncSendTimeout(4);
		container.setDefaultMaxBinaryMessageBufferSize(5);
		container.setDefaultMaxSessionIdleTimeout(6);
		container.setDefaultMaxTextMessageBufferSize(7);
		assertEquals("defaultAsyncSendTimeout: ", 4, container.getDefaultAsyncSendTimeout());
		assertEquals("defaultMaxBinaryMessageBufferSize: ", 5, container.getDefaultMaxBinaryMessageBufferSize());
		assertEquals("defaultMaxSessionIdleTimeout: ", 6, container.getDefaultMaxSessionIdleTimeout());
		assertEquals("defaultMaxTextMessageBufferSize: ", 7, container.getDefaultMaxTextMessageBufferSize());
		Set<Extension> installedExtensions = container.getInstalledExtensions();
		assertTrue("installedExtensions: ", installedExtensions.isEmpty());
		extensions(installedExtensions.iterator());
	}

	private void messageHandlers() {
		Set<MessageHandler> messageHandlers = sessionClient.getMessageHandlers();
		assertEquals("messageHandler: ", 1, messageHandlers.size());
		for (MessageHandler messageHandler : messageHandlers) {
			logger.info("messageHandler: " + messageHandler);
		}
		MessageHandler messageHandler = new SessionMessageHandler();
		sessionClient.addMessageHandler(messageHandler);
		messageHandlers = sessionClient.getMessageHandlers();
		assertEquals("messageHandler: ", 2, messageHandlers.size());
		try {
			sessionClient.getBasicRemote().sendPong(allocate(33));
			assertFalse(
					"the pong is not received by the session message handler because exists a default message handler for the pong messages",
					sentPong);
		} catch (IllegalArgumentException | IOException e) {
			fail();
		}
		sessionClient.removeMessageHandler(messageHandler);
	}

	private void max() {
		assertEquals("maxBinaryMessageBufferSize: ", 0, sessionClient.getMaxBinaryMessageBufferSize());
		assertEquals("maxIdleTimeout: ", 0, sessionClient.getMaxIdleTimeout());
		assertEquals("maxTextMessageBufferSize: ", 0, sessionClient.getMaxTextMessageBufferSize());
		sessionClient.setMaxBinaryMessageBufferSize(4);
		sessionClient.setMaxIdleTimeout(7);
		sessionClient.setMaxTextMessageBufferSize(8);
		assertEquals("maxBinaryMessageBufferSize: ", 4, sessionClient.getMaxBinaryMessageBufferSize());
		assertEquals("maxIdleTimeout: ", 7, sessionClient.getMaxIdleTimeout());
		assertEquals("maxTextMessageBufferSize: ", 8, sessionClient.getMaxTextMessageBufferSize());
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

	private void async() throws Exception {
		Async async = sessionClient.getAsyncRemote();
		remoteEndpoint(async);
		async.setBatchingAllowed(true);
		assertTrue("remoteEndpoint batchingAllowed: ", async.getBatchingAllowed());
		assertEquals("async getSendTimeout: ", 0, async.getSendTimeout());
		async.setSendTimeout(45);
		assertEquals("async getSendTimeout: ", 45, async.getSendTimeout());
		ByteBuffer byteBuffer = allocate(11);
		Future<Void> future = async.sendBinary(byteBuffer);
		try {
			assertNull("no result because is a void", future.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.log(SEVERE, "error", e);
			fail();
		}
		connect();
		async = sessionClient.getAsyncRemote();
		async.setSendTimeout(45);
		SendHandler sendHandler = new SendHandler() {
			@Override
			public void onResult(SendResult result) {
				assertOK(result);
			}
		};
		async.sendBinary(byteBuffer, sendHandler);
		future = async.sendObject(666);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.log(SEVERE, "error", e);
			fail();
		}
		connect();
		async = sessionClient.getAsyncRemote();
		async.setSendTimeout(45);
		async.sendObject("my test 2", sendHandler);
		future = async.sendText("my text");
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.log(SEVERE, "error", e);
			fail();
		}
		async.sendText("my text 2", sendHandler);
	}

	private void basic() throws Exception {
		connect();
		Basic basic = sessionClient.getBasicRemote();
		remoteEndpoint(basic);
		basic.setBatchingAllowed(true);
		assertFalse("remoteEndpoint batchingAllowed: ", basic.getBatchingAllowed());
		ByteBuffer byteBuffer = allocate(23);
		try {
			basic.sendBinary(byteBuffer);
			basic.sendBinary(byteBuffer, true);
			basic.sendObject(555);
			basic.sendText("my text");
			basic.sendText("my text 2", false);
			connect();
			basic = sessionClient.getBasicRemote();
			assertNotNull("basic sendStream: " + basic.getSendStream());
			assertNotNull("basic sendWriter: " + basic.getSendWriter());
		} catch (IOException | EncodeException e) {
			logger.log(SEVERE, "error", e);
			fail();
		}
	}

	private void remoteEndpoint(RemoteEndpoint remoteEndpoint) {
		try {
			remoteEndpoint.flushBatch();
			ByteBuffer byteBuffer = allocate(23);
			remoteEndpoint.sendPing(byteBuffer);
			remoteEndpoint.sendPong(byteBuffer);
			assertFalse("remoteEndpoint batchingAllowed: ", remoteEndpoint.getBatchingAllowed());
		} catch (IOException e) {
			logger.log(SEVERE, "error", e);
			fail();
		}
	}

	private void assertOK(SendResult result) {
		assertTrue("session sendHandler result ok: ", result.isOK());
		assertNull("session sendHandler result exception: ", result.getException());
	}

	private void connect() throws Exception {
		while (sessionClient != null && !sessionClient.isOpen()) {
			break;
		}
		final WebSocketContainer serverContainer = getWebSocketContainer();
		serverContainer.connectToServer(SessionSocketClient.class, url.toURI().resolve("session"));
		awake();
	}
}
