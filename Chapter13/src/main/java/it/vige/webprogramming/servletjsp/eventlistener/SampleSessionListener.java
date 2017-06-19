package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SampleSessionListener implements HttpSessionListener {

	private static final Logger logger = getLogger(SampleSessionListener.class.getName());

	public static Set<Integer> sampleSessionSet = new HashSet<Integer>();
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("MySessionListener.sessionCreated: " + se.getSession().getId());
		sampleSessionSet.add(1);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("MySessionListener.sessionDestroyed: " + se.getSession().getId());
		sampleSessionSet.add(2);
	}
}
