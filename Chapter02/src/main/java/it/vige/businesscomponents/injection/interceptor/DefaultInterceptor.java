package it.vige.businesscomponents.injection.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import it.vige.businesscomponents.injection.interceptor.service.SimpleStatelessBean;

public class DefaultInterceptor {

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		SimpleStatelessBean.executed = true;
		return ic.proceed();
	}
}
