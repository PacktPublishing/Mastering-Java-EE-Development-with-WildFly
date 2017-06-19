package it.vige.businesscomponents.injection.context;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

public class InitializeApplicationObserver {
	
	@Inject
	private ApplicationBean applicationBean;

	private static final Logger logger = getLogger(InitializeApplicationObserver.class.getName());

	public void processApplicationScopedInit(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
		logger.info("initialized the ApplicationBean");
		applicationBean.incrementCounter();
	}

	public void processApplicationScopedDestroyed(
			@Observes @Destroyed(ApplicationScoped.class) ServletContext payload) {
		logger.info("destroyed the ApplicationBean");
		applicationBean.incrementCounter();
	}
}
