package it.vige.businesscomponents.injection.event;

import static javax.interceptor.Interceptor.Priority.PLATFORM_BEFORE;
import static java.util.logging.Logger.getLogger;
import static javax.enterprise.event.Reception.IF_EXISTS;
import static javax.enterprise.event.TransactionPhase.AFTER_COMPLETION;
import static javax.enterprise.event.TransactionPhase.AFTER_FAILURE;
import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;
import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

@Singleton
@Priority(PLATFORM_BEFORE)
public class IfExistsObserver {

	private static final Logger logger = getLogger(IfExistsObserver.class.getName());

	public void afterFailure(@Observes(notifyObserver = IF_EXISTS, during = AFTER_FAILURE) Bill event) {
		logger.info("observing if exists after failure");
		event.incrementId();
	}

	public void afterSuccess(@Observes(notifyObserver = IF_EXISTS, during = AFTER_SUCCESS) Bill event) {
		logger.info("observing if exists after success");
		event.incrementId();
	}

	public void beforeCompletion(@Observes(notifyObserver = IF_EXISTS, during = BEFORE_COMPLETION) Bill event) {
		logger.info("observing if exists before completion");
		event.incrementId();
	}

	public void afterCompletion(@Observes(notifyObserver = IF_EXISTS, during = AFTER_COMPLETION) Bill event) {
		logger.info("observing if exists after completion");
		event.incrementId();
	}

	public void inProgress(@Observes(notifyObserver = IF_EXISTS) Bill event) {
		logger.info("observing if exists after completion");
		event.incrementId();
	}
}
