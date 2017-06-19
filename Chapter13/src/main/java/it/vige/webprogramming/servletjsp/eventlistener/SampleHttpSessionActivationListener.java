package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

public class SampleHttpSessionActivationListener implements HttpSessionActivationListener {

	private static final Logger logger = getLogger(SampleHttpSessionActivationListener.class.getName());

	public static Set<Integer> sampleHttpSessionActivationSet = new HashSet<Integer>();
	
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		logger.info("MyHttpSessionActivationListener.sessionWillPassivate: " + se.getSession().getId());
		sampleHttpSessionActivationSet.add(1);
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		logger.info("MyHttpSessionActivationListener.sessionDidActivate: " + se.getSession().getId());
		sampleHttpSessionActivationSet.add(2);
	}

}
