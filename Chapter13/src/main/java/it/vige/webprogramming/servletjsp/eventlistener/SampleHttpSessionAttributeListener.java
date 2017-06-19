package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SampleHttpSessionAttributeListener implements HttpSessionAttributeListener {

	private static final Logger logger = getLogger(SampleHttpSessionAttributeListener.class.getName());

	public static Set<Integer> sampleHttpSessionAttributeSet = new HashSet<Integer>();
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		logger.info("MyHttpSessionAttributeListener.attributeAdded: " + event.getName());
		sampleHttpSessionAttributeSet.add(1);
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		logger.info("MyHttpSessionAttributeListener.attributeRemoved: " + event.getName());
		sampleHttpSessionAttributeSet.add(2);
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		logger.info("MyHttpSessionAttributeListener.attributeReplaced: " + event.getName());
		sampleHttpSessionAttributeSet.add(3);
	}
}
