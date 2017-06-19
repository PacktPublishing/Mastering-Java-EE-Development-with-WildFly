package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SampleContextAttributeListener implements ServletContextAttributeListener {

	private static final Logger logger = getLogger(SampleContextAttributeListener.class.getName());

	public static Set<Integer> sampleContextAttributeSet = new HashSet<Integer>();

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		logger.info("MyContextAttributeListener.attributeAdded: " + event.getName());
		sampleContextAttributeSet.add(1);
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		logger.info("MyContextAttributeListener.attributeRemoved: " + event.getName());
		sampleContextAttributeSet.add(2);
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		logger.info("MyContextAttributeListener.attributeReplaced: " + event.getName());
		sampleContextAttributeSet.add(3);
	}
}
