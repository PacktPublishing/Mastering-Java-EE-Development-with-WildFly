package it.vige.businesscomponents.services;

import static io.undertow.util.Headers.CONTENT_MD5_STRING;
import static io.undertow.util.Headers.CONTENT_TYPE_STRING;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.RuntimeType.CLIENT;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.services.components.BlockChainFilter;
import it.vige.businesscomponents.services.components.ClientFirstReaderInterceptor;
import it.vige.businesscomponents.services.components.ClientSecondReaderInterceptor;
import it.vige.businesscomponents.services.components.ContentMD5Writer;
import it.vige.businesscomponents.services.components.MyClientRequestFilter;
import it.vige.businesscomponents.services.components.MyClientResponseFilter;
import it.vige.businesscomponents.services.components.MyComponent;
import it.vige.businesscomponents.services.components.OtherClientRequestFilter;
import it.vige.businesscomponents.services.components.OtherClientResponseFilter;

@RunWith(Arquillian.class)
public class ComponentTestCase {

	private static final Logger logger = getLogger(ComponentTestCase.class.getName());

	@ArquillianResource
	private URL url;

	@Deployment
	public static WebArchive createWebDeployment() {
		final WebArchive war = create(WebArchive.class, "component-test.war");
		war.addPackage(MyComponent.class.getPackage());
		war.addAsWebInfResource(INSTANCE, "beans.xml");
		war.addAsWebInfResource(new FileAsset(new File("src/test/resources/web.xml")), "web.xml");
		return war;
	}

	@Test
	public void testConfiguration() throws Exception {
		logger.info("start REST Configuration test");
		Client client = newClient();
		Configuration configuration = client.getConfiguration();
		Set<Class<?>> classes = configuration.getClasses();
		for (Class<?> clazz : classes) {
			assertTrue("verify if the class is a rest component or provider",
					MessageBodyReader.class.isAssignableFrom(clazz) || MessageBodyWriter.class.isAssignableFrom(clazz)
							|| clazz.isAnnotationPresent(Provider.class)
							|| DynamicFeature.class.isAssignableFrom(clazz));
			Map<Class<?>, Integer> contracts = configuration.getContracts(clazz);
			assertFalse("each class has different contracts", contracts.isEmpty());
			for (Class<?> contract : contracts.keySet()) {
				int value = contracts.get(contract);
				assertTrue("verify if the contract is a rest component or provider",
						value == 5000 || value == 4000 || value == 3000 || value == 0);
			}
		}
		Set<Object> instances = configuration.getInstances();
		assertTrue("by default there are not instances", instances.isEmpty());
		Map<String, Object> properties = configuration.getProperties();
		assertTrue("by default there are not properties", properties.isEmpty());
		MyComponent myComponent = new MyComponent();
		client.register(myComponent);
		instances = configuration.getInstances();
		assertFalse("Added instance", instances.isEmpty());
		for (Object instance : instances) {
			if (instance instanceof MyComponent)
				assertTrue("MyComponent is registered and active", configuration.isEnabled((Feature) instance));
		}
		assertEquals("Added property through MyComponent", 1, properties.size());
		boolean property = (Boolean) properties.get("configured_myComponent");
		assertEquals("configured_myComponent ok!", true, property);
		assertEquals("type CLIENT by default", CLIENT, configuration.getRuntimeType());
	}

	@Test
	public void testRegister() throws Exception {
		logger.info("start Register test");
		Client client = newClient();
		Map<Class<?>, Integer> myContracts = new HashMap<Class<?>, Integer>();
		myContracts.put(Feature.class, 1200);
		client.register(MyComponent.class, myContracts);
		Configuration configuration = client.getConfiguration();
		Set<Class<?>> classes = configuration.getClasses();
		for (Class<?> clazz : classes) {
			if (MyComponent.class.isAssignableFrom(clazz)) {
				Map<Class<?>, Integer> contracts = configuration.getContracts(clazz);
				int priority = contracts.get(Feature.class);
				assertTrue("Only standard: Feature, DynamicFeature, WriterInterceptor, "
						+ "ReaderInterceptor, ContainerResponseFilter, " + "ContainerRequestFilter, "
						+ "ClientResponseFilter, ClientRequestFilter, " + "ExceptionMapper, MessageBodyWriter, "
						+ "MessageBodyReader,ParamConverterProvider or implemented: InjectorFactory, "
						+ "StringParameterUnmarshaller, StringConverter, " + "ContextResolver, PostProcessInterceptor, "
						+ "PreProcessInterceptor, ClientExecutionInterceptor, ClientExceptionMapper"
						+ "can be registered as contracts. Registered priority", priority == 1200);
			}
		}
	}

	@Test
	@RunAsClient
	public void testMD5() throws Exception {
		logger.info("start JaxRS Post test");
		Client client = newClient();
		client.register(ContentMD5Writer.class);
		Response response = client.target(url + "myjaxrs/simple/valuesget").request().get();
		String md5 = response.getHeaderString(CONTENT_MD5_STRING);
		assertEquals("Content-MD5: ", "hcEzFGyuhOARcfBb4bM1sw==", md5);

	}

	@Test
	@RunAsClient
	public void testGet() throws Exception {
		logger.info("Registering Client Level Filters");
		Client client = newClient();
		client.register(new OtherClientResponseFilter());
		WebTarget target = client.target(url + "myjaxrs/simple/");
		target.register(new OtherClientRequestFilter());

		WebTarget resourceTarget = target.path("/valuesget");
		resourceTarget = resourceTarget.queryParam("OrderID", "111").queryParam("UserName", "Luke");
		resourceTarget.register(new MyClientResponseFilter());
		resourceTarget.register(new MyClientRequestFilter());

		logger.info("Invoking REST Service: " + resourceTarget.getUri().toString());
		Invocation invocation = resourceTarget.request().buildGet();
		Response response = invocation.invoke();
		String respContent = "";

		if (response.hasEntity())
			respContent = response.readEntity(String.class);

		assertEquals("Response--> ", "111-Luke", respContent);
		assertEquals("Content Type after changing in ClientResponseFilter: ", TEXT_HTML,
				response.getHeaderString(CONTENT_TYPE_STRING));
	}

	@Test
	@RunAsClient
	public void testReader() throws Exception {
		logger.info("start JaxRS Post test");
		Client client = newClient();
		client.register(ClientFirstReaderInterceptor.class);
		client.register(ClientSecondReaderInterceptor.class);
		Entity<String> value = entity("my test", TEXT_PLAIN);
		Response response = client.target(url + "myjaxrs/simple/values").request().post(value);
		String result = response.readEntity(String.class);
		assertEquals("The ServerReaderInterceptor is not registered because has SERVER runtime type ",
				"Order successfully placed .Request changed in ClientFirstReaderInterceptor. Request changed in ClientSecondReaderInterceptor.",
				result);
	}

	@Test
	@RunAsClient
	public void testAbort() throws Exception {
		logger.info("Registering Client Abort Filters");
		Client client = newClient();
		client.register(new MyClientResponseFilter());
		client.register(new MyClientRequestFilter());
		client.register(new BlockChainFilter());
		WebTarget target = client.target(url + "myjaxrs/simple/");

		WebTarget resourceTarget = target.path("/valuesget");
		resourceTarget = resourceTarget.queryParam("OrderID", "111").queryParam("UserName", "Luke");

		logger.info("Invoking REST Service: " + resourceTarget.getUri().toString());
		Invocation invocation = resourceTarget.request().buildGet();
		Response response = invocation.invoke();

		assertEquals(
				"MyClientRequestFilter is not executed because BlockChainFilter blocks it. So no header is saved: ", "",
				response.getHeaderString(CONTENT_TYPE_STRING));
	}
}
