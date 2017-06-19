package it.vige.businesscomponents.injection.interceptor;

import static java.util.logging.Logger.getLogger;
import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.inject.Intercepted;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.interceptor.AroundConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import it.vige.businesscomponents.injection.interceptor.service.Item;

/**
 * LoggingInterceptor binds to {@link @Logging} annotation, so methods or beans
 * which @Logging annotation is applied to, will be intercepted.
 *
 * @author Luca Stancapiano
 */
@Interceptor
@Logging
@Priority(LIBRARY_BEFORE)
public class LoggingInterceptor {

	@Inject
	@Intercepted
	private Bean<?> bean;

	private static final Logger logger = getLogger(LoggingInterceptor.class.getName());

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		String methodName = ic.getMethod().getName();
		logger.info("Executing " + ic.getTarget().getClass().getSimpleName() + "." + methodName + " method");
		Object[] parameters = (Object[]) ic.getParameters();
		logger.info("parameters are: " + parameters.length);
		if (parameters.length == 1) {
			Item item = (Item) parameters[0];
			logger.info("item: " + item.getName());
		}
		return ic.proceed();
	}

	@AroundConstruct
	public Object aroundConstruct(InvocationContext ic) throws Exception {
		logger.info("Executing " + ic.getConstructor());
		Map<String, Object> data = ic.getContextData();
		data.forEach((k, v) -> logger.info("data key: " + k + " - value: " + v));
		return ic.proceed();
	}

	public Bean<?> getBean() {
		return bean;
	}
}
