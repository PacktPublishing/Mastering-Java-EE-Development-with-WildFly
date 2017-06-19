package it.vige.businesscomponents.services;

import static java.util.logging.Logger.getLogger;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Singleton
public class MyResourceSingleton implements DynamicFeature {

	private static final Logger logger = getLogger(MyResourceSingleton.class.getName());

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		Class<?> resourceClass = resourceInfo.getResourceClass();
		Method method = resourceInfo.getResourceMethod();
		logger.info("getResourceClass: " + resourceClass);
		logger.info("getResourceMethod: " + method);
		context.property("new_dynamic_feature", resourceClass + "|" + method);
	}

}
