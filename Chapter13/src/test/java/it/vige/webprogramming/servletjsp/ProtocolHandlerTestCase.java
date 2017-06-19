package it.vige.webprogramming.servletjsp;

import static it.vige.webprogramming.servletjsp.protocolhandler.SampleProtocolHandler.CRLF;
import static java.lang.Integer.valueOf;
import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.webprogramming.servletjsp.protocolhandler.UpgradeServlet;

@RunWith(Arquillian.class)
@RunAsClient
public class ProtocolHandlerTestCase {

	private static final Logger logger = getLogger(ProtocolHandlerTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "protocolhandler-test.war");
		war.addPackage(UpgradeServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testProtocolHandler() throws Exception {
		logger.info("start protocol handler test");
		String host;
		String contextRoot;
		int port;
		Socket socket = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		String response;

		Matcher matcher = compile("http://(.*):(\\d{1,5})/(.*)").matcher(url.toString());
		if (matcher.find()) {
			host = matcher.group(1);
			port = valueOf(matcher.group(2));
			contextRoot = matcher.group(3);
		} else {
			throw new AssertionError("Cannot parse the test archive URL");
		}

		try {
			socket = new Socket(host, port);
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			// Initial HTTP upgrade request
			out.write("GET /" + contextRoot + "UpgradeServlet HTTP/1.1" + CRLF);
			out.write("Host: " + host + ":" + port + CRLF);
			out.write("Upgrade: echo" + CRLF);
			out.write("Connection: Upgrade" + CRLF);
			out.write(CRLF);
			out.flush();

			// Receive the protocol upgrade response
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				if ("".equals(line)) {
					break;
				}
			}

			// Send dummy request
			out.write("dummy request#");
			out.flush();

			// Receive the dummy response
			StringBuilder buffer = new StringBuilder();
			while (!(line = in.readLine()).equals("END")) {
				buffer.append(line);
			}
			response = buffer.toString();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		assertTrue(response.equals("upgradereceived"));
	}

}
