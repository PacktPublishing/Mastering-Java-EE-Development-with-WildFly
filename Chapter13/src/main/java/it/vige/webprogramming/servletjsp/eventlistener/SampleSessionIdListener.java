package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

@WebListener
public class SampleSessionIdListener implements HttpSessionIdListener {

	private static final Logger logger = getLogger(SampleSessionIdListener.class.getName());

	public static Set<Integer> sampleSessionIdSet = new HashSet<Integer>();
	
	@Override
	public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
		logger.info(
				"MySessionIdListener.sessionIdChanged: new=" + event.getSession().getId() + ", old=" + oldSessionId);
		sampleSessionIdSet.add(1);
	}

}
