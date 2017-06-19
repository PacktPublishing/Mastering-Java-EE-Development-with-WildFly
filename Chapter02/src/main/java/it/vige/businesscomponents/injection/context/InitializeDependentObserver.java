package it.vige.businesscomponents.injection.context;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

public class InitializeDependentObserver {

	@Inject
	private DependentBean dependentBean;

	private static final Logger logger = getLogger(InitializeDependentObserver.class.getName());

	public void processDependentScopedInit(@Observes @Initialized(ApplicationScoped.class) ServletContext payload) {
		logger.info("initialized the DependentBean");
		dependentBean.incrementCounter();
	}

	public void processDependentScopedDestroyed(@Observes @Destroyed(ApplicationScoped.class) ServletContext payload) {
		logger.info("destroyed the DependentBean");
	}
}
