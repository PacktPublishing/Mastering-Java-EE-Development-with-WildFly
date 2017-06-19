package it.vige.businesscomponents.injection.interceptor;

import static it.vige.businesscomponents.injection.interceptor.service.History.getItemHistory;
import static java.util.logging.Logger.getLogger;

import java.util.Map;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import it.vige.businesscomponents.injection.interceptor.service.Item;

@Interceptor
public class IncludedInterceptor {

	private static final Logger logger = getLogger(IncludedInterceptor.class.getName());

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
		Map<String, Object> contextData = ic.getContextData();
		if (!contextData.isEmpty())
			getItemHistory().add(contextData.get("test_trace") + "");
		return ic.proceed();
	}

}
