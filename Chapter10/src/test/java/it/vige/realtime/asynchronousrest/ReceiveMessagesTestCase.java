package it.vige.realtime.asynchronousrest;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ReceiveMessagesTestCase {

	private static final Logger logger = getLogger(ReceiveMessagesTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "asynch-rest-test.war");
		war.addPackage(AsyncResource.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testMagicNumber() throws Exception {
		logger.info("start rest receive messages test");
		MyResult myResponse = invokeFuture(url + "async/resource/simple");
		assertEquals("magic number is: ", "MagicNumber [value=3]", myResponse.getResponse());
		assertEquals("response is: ", true, myResponse.isOk());
		logger.info("end rest receive messages test");
	}

	@Test
	public void testMagicNumberWithTimeout() throws Exception {
		logger.info("start rest receive timeout messages test");
		MyResult myResponse = invokeCallbackResponse(url + "async/resource/withTimeout");
		assertEquals("magic number is: ", "Operation time out.", myResponse.getResponse());
		assertEquals("response is: ", false, myResponse.isOk());
		logger.info("end rest receive timeout messages test");
	}

	@Test
	public void testMagicNumberWithCallback() throws Exception {
		logger.info("start rest receive callback messages test");
		MyResult myResponse = invokeCallbackString(url + "async/resource/withCallback");
		assertEquals("magic number is: ", "MagicNumber [value=22]", myResponse.getResponse());
		assertEquals("response is: ", true, myResponse.isOk());
		logger.info("end rest receive callback messages test");
	}

	private MyResult invokeFuture(String url) {
		Client client = newClient();
		WebTarget target = client.target(url);
		final AsyncInvoker asyncInvoker = target.request().async();
		Future<Response> future = asyncInvoker.get();
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			logger.log(SEVERE, "error", e);
		}
		final MyResult myResponse = new MyResult();
		try {
			Response response = future.get();
			myResponse.setOk(response.hasEntity());
			myResponse.setResponse(response.readEntity(String.class));
		} catch (InterruptedException | ExecutionException e) {
			logger.log(SEVERE, "error", e);
		}
		return myResponse;
	}

	private MyResult invokeCallbackString(String url) {
		Client client = newClient();
		WebTarget target = client.target(url);
		final AsyncInvoker asyncInvoker = target.request().async();
		final MyResult myResponse = new MyResult();

		asyncInvoker.get(new InvocationCallback<String>() {
			@Override
			public void completed(String response) {
				myResponse.setResponse(response);
				myResponse.setOk(true);
			}

			@Override
			public void failed(Throwable arg0) {
				myResponse.setResponse(arg0.getMessage());
				myResponse.setOk(false);
			}
		});
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			logger.log(SEVERE, "error", e);
		}
		return myResponse;
	}

	private MyResult invokeCallbackResponse(String url) {
		Client client = newClient();
		WebTarget target = client.target(url);
		final AsyncInvoker asyncInvoker = target.request().async();
		final MyResult myResponse = new MyResult();

		asyncInvoker.get(new InvocationCallback<Response>() {
			@Override
			public void completed(Response response) {
				myResponse.setResponse(response.readEntity(String.class));
				myResponse.setOk(response.hasEntity());
			}

			@Override
			public void failed(Throwable arg0) {
				myResponse.setResponse(arg0.getMessage());
				myResponse.setOk(false);
			}
		});
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			logger.log(SEVERE, "error", e);
		}
		return myResponse;
	}

}
