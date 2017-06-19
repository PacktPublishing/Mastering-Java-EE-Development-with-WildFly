package it.vige.clustering.sharedata.singleton;

import static it.vige.clustering.sharedata.singleton.HAServiceServlet.SERVLET_PATH;
import static java.lang.String.format;
import static org.jboss.as.server.CurrentServiceContainer.getServiceContainer;
import static org.jboss.msc.service.ServiceName.parse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

@WebServlet(urlPatterns = { SERVLET_PATH })
public class HAServiceServlet extends HttpServlet {
	private static final long serialVersionUID = -592774116315946908L;
	private static final String SERVLET_NAME = "service";
	static final String SERVLET_PATH = "/" + SERVLET_NAME;
	private static final String SERVICE = "service";
	private static final String EXPECTED = "expected";
	private static final int RETRIES = 10;

	public static URI createURI(URL baseURL, ServiceName serviceName) throws URISyntaxException {
		return baseURL.toURI().resolve(buildQuery(serviceName).toString());
	}

	public static URI createURI(URL baseURL, ServiceName serviceName, String expected) throws URISyntaxException {
		return baseURL.toURI()
				.resolve(buildQuery(serviceName).append('&').append(EXPECTED).append('=').append(expected).toString());
	}

	private static StringBuilder buildQuery(ServiceName serviceName) {
		return new StringBuilder(SERVLET_NAME).append('?').append(SERVICE).append('=')
				.append(serviceName.getCanonicalName());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String serviceName = getRequiredParameter(req, SERVICE);
		String expected = req.getParameter(EXPECTED);
		this.log(format("Received request for %s, expecting %s", serviceName, expected));
		@SuppressWarnings("unchecked")
		ServiceController<Environment> service = (ServiceController<Environment>) getServiceContainer()
				.getService(parse(serviceName));
		try {
			Environment env = service.getValue();
			if (expected != null) {
				for (int i = 0; i < RETRIES; ++i) {
					if ((env != null) && expected.equals(env.getNodeName()))
						break;
					Thread.yield();
					env = service.getValue();
				}
			}
			if (env != null) {
				resp.setHeader("node", env.getNodeName());
			}
		} catch (IllegalStateException e) {
			// Service was not started
		}
		resp.getWriter().write("Success");
	}

	private static String getRequiredParameter(HttpServletRequest req, String name) throws ServletException {
		String value = req.getParameter(name);
		if (value == null) {
			throw new ServletException(String.format("No %s specified", name));
		}
		return value;
	}
}
