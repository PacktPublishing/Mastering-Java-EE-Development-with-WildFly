package it.vige.businesscomponents.injection.interceptor;

import static java.util.logging.Logger.getLogger;
import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.inject.Intercepted;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Logging
@Priority(LIBRARY_BEFORE + 1)
public class TraceInterceptor {

	@Inject
	@Intercepted
	private Bean<?> bean;

	private static final Logger logger = getLogger(TraceInterceptor.class.getName());

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		String methodName = ic.getMethod().getName();
		logger.info("Tracing " + ic.getTarget().getClass().getSimpleName() + "." + methodName + " method");
		return ic.proceed();
	}

	public Bean<?> getBean() {
		return bean;
	}

}
