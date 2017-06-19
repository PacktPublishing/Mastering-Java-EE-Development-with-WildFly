package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SampleServletRequestAttributeListener implements ServletRequestAttributeListener {

	private static final Logger logger = getLogger(SampleServletRequestAttributeListener.class.getName());

	public static Set<Integer> sampleServletRequestAttributeSet = new HashSet<Integer>();
	
	@Override
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		logger.info("MyServletRequestAttributeListener.attributeAdded: " + srae.getName());
		sampleServletRequestAttributeSet.add(1);
	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		logger.info("MyServletRequestAttributeListener.attributeRemoved: " + srae.getName());
		sampleServletRequestAttributeSet.add(2);
	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		logger.info("MyServletRequestAttributeListener.attributeReplaced: " + srae.getName());
		sampleServletRequestAttributeSet.add(3);
	}

}
