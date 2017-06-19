package it.vige.webprogramming.servletjsp.registration;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

@WebListener
public class RegistrationContextListener implements ServletContextListener {

	private static final Logger logger = getLogger(RegistrationContextListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Servlet context initialized: " + sce.getServletContext().getContextPath());
		Dynamic registration = sce.getServletContext().addServlet("dynamic", RegistrationDynamicServlet.class);
		registration.addMapping("/dynamic");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Servlet context destroyed: " + sce.getServletContext().getContextPath());
	}
}
