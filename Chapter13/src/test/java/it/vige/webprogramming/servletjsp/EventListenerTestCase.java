package it.vige.webprogramming.servletjsp;

import static it.vige.webprogramming.servletjsp.eventlistener.SampleContextAttributeListener.sampleContextAttributeSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleContextListener.sampleContextSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleHttpSessionActivationListener.sampleHttpSessionActivationSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleHttpSessionAttributeListener.sampleHttpSessionAttributeSet;
import static it.vige.webprogramming.servletjsp.eventlistener.MyAttribute.sampleHttpSessionBindingSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleServletRequestAttributeListener.sampleServletRequestAttributeSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleServletRequestListener.sampleServletRequestSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleSessionIdListener.sampleSessionIdSet;
import static it.vige.webprogramming.servletjsp.eventlistener.SampleSessionListener.sampleSessionSet;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.webprogramming.servletjsp.eventlistener.EventListenerServlet;

@RunWith(Arquillian.class)
public class EventListenerTestCase {

	private static final Logger logger = getLogger(EventListenerTestCase.class.getName());

	@ArquillianResource
	private URL path;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "eventlistener-test.war");
		war.addPackage(EventListenerServlet.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testEventListener() throws Exception {
		logger.info("start event listener test");
		assertEquals("sample Context Attribute Set: ", 3, sampleContextAttributeSet.size());
		assertEquals("sample Context Set: ", 1, sampleContextSet.size());
		assertTrue("sample HttpSession Activation Set: ",
				sampleHttpSessionActivationSet == null || sampleHttpSessionActivationSet.isEmpty());
		assertEquals("sample HttpSession Attribute Set: ", 0, sampleHttpSessionAttributeSet.size());
		assertTrue("sample HttpSession Binding Set: ",
				sampleHttpSessionBindingSet == null || sampleHttpSessionBindingSet.isEmpty());
		assertEquals("sample ServletRequest Attribute Set: ", 2, sampleServletRequestAttributeSet.size());
		assertEquals("sample ServletRequest Set: ", 2, sampleServletRequestSet.size());
		assertEquals("sample SessionId Set: ", 0, sampleSessionIdSet.size());
		assertEquals("sample Session Set: ", 0, sampleSessionSet.size());

		URL url = new URL(path + "EventListenerServlet");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			input.readLine();
			input.close();
		}
		assertEquals("sample HttpSession Attribute Set: ", 3, sampleHttpSessionAttributeSet.size());
		assertEquals("sample ServletRequest Attribute Set: ", 3, sampleServletRequestAttributeSet.size());
		assertEquals("sample ServletRequest Set: ", 2, sampleServletRequestSet.size());
		assertEquals("sample Session Set: ", 2, sampleSessionSet.size());
	}

}
