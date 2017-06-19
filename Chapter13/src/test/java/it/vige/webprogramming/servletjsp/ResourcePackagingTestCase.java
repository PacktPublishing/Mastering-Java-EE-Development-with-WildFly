package it.vige.webprogramming.servletjsp;

import static java.net.URI.create;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ResourcePackagingTestCase {

	@Deployment(testable = false)
	public static WebArchive deploy() throws URISyntaxException {
		return create(WebArchive.class).addAsLibrary(new File("target/servlets-jsp/WEB-INF/lib/packResources.jar"),
				"packResources.jar");
	}

	@ArquillianResource
	private URL base;

	@Test
	public void getMyResourceJarStyles() throws MalformedURLException {
		Client client = newClient();
		WebTarget target = client.target(create(new URL(base, "styles.css").toExternalForm()));
		Response response = target.request().get();

		assertThat(response.getStatus(), is(equalTo(200)));

		String style = response.readEntity(String.class);
		assertThat(style, startsWith("body {"));
	}

}
