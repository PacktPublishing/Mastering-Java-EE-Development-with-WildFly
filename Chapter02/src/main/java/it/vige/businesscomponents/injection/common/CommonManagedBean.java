package it.vige.businesscomponents.injection.common;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.enterprise.inject.spi.BeanManager;

/**
 * User: jpai
 */

@Resources({ @Resource(name = "beanManager", lookup = "java:comp/BeanManager") })
@ManagedBean
public class CommonManagedBean {
	
	@Resource
	private BeanManager beanManager;

	public static final String HELLO_GREETING_PREFIX = "Hello ";

	public String sayHello(String user) {
		return HELLO_GREETING_PREFIX + user;
	}
}
