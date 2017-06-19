package it.vige.businesscomponents.injection.common;

import javax.annotation.Resource;
import javax.enterprise.inject.spi.BeanManager;

/**
 * User: jpai
 */

public class CommonBean {

	public static final String HELLO_GREETING_PREFIX = "Hello ";

	@Resource(lookup = "java:comp/BeanManager")
	private BeanManager beanManager;

	public String sayHello(String user) {
		return HELLO_GREETING_PREFIX + user;
	}

	public BeanManager getBeanManager() {
		return beanManager;
	}
}
