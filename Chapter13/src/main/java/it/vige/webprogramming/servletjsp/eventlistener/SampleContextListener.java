package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SampleContextListener implements ServletContextListener {

	private static final Logger logger = getLogger(SampleContextListener.class.getName());

	public static Set<Integer> sampleContextSet = new HashSet<Integer>();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("MyContextListener.contextInitialized: " + sce.getServletContext().getContextPath());
		sampleContextSet.add(1);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("MyContextListener.contextDestroyed: " + sce.getServletContext().getContextPath());
		sampleContextSet.add(2);
	}
}
