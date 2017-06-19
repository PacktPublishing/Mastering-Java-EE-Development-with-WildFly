package it.vige.webprogramming.servletjsp.eventlistener;

import static java.util.logging.Logger.getLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class MyAttribute implements HttpSessionBindingListener {

	private static final Logger logger = getLogger(MyAttribute.class.getName());

	public static Set<Integer> sampleHttpSessionBindingSet = new HashSet<Integer>();

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		logger.info("MyAttribute.valueBound: " + event.getName());
		sampleHttpSessionBindingSet.add(1);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		logger.info("MyAttribute.valueUnbound: " + event.getName());
		sampleHttpSessionBindingSet.add(2);
	}

}
