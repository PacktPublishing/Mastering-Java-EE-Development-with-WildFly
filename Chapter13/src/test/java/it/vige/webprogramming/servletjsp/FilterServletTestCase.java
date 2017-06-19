package it.vige.webprogramming.servletjsp;

import static java.net.URI.create;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.webprogramming.servletjsp.filters.CharacterServlet;
import it.vige.webprogramming.servletjsp.filters.MyBarFilter;
import it.vige.webprogramming.servletjsp.filters.ResponseCharacterWrapper;

@RunWith(Arquillian.class)
public class FilterServletTestCase {

	@Deployment
	public static WebArchive createDeployment() {
		return create(WebArchive.class).addClass(ResponseCharacterWrapper.class).addClasses(CharacterServlet.class,
				MyBarFilter.class);
	}

	@ArquillianResource
	private URL base;

	@Test
	@RunAsClient
	public void filtered_servlet_should_return_enhanced_foobar_text() throws MalformedURLException {
		Client client = newClient();
		WebTarget target = client.target(create(new URL(base, "filtered/CharacterServlet").toExternalForm()));

		Response response = target.request().get();
		assertThat(response.readEntity(String.class), is(equalTo("my--bar--bar")));
	}

	@Test
	@RunAsClient
	public void standard_servlet_should_return_simple_text() throws MalformedURLException {
		Client client = newClient();
		WebTarget target = client.target(create(new URL(base, "CharacterServlet").toExternalForm()));

		Response response = target.request().get();
		assertThat(response.readEntity(String.class), is(equalTo("bar")));
	}
}
