package it.vige.businesscomponents.injection.event;

import static java.util.logging.Logger.getLogger;
import static javax.enterprise.event.TransactionPhase.AFTER_COMPLETION;
import static javax.enterprise.event.TransactionPhase.AFTER_FAILURE;
import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;
import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;
import static javax.interceptor.Interceptor.Priority.PLATFORM_BEFORE;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;

@Priority(PLATFORM_BEFORE + 1)
public class AlwaysObserver {

	private static final Logger logger = getLogger(AlwaysObserver.class.getName());

	public void afterFailure(@Observes(during = AFTER_FAILURE) Bill event) {
		logger.info("observing if exists after failure");
		event.incrementId();
	}

	public void afterSuccess(@Observes(during = AFTER_SUCCESS) Bill event) {
		logger.info("observing if exists after success");
		event.incrementId();
	}

	public void beforeCompletion(@Observes(during = BEFORE_COMPLETION) Bill event) {
		logger.info("observing if exists before completion");
		event.incrementId();
	}

	public void afterCompletion(@Observes(during = AFTER_COMPLETION) Bill event) {
		logger.info("observing if exists after completion");
		event.incrementId();
	}

	public void inProgress(@Observes Bill event) {
		logger.info("observing if exists after completion");
		event.incrementId();
	}
}
