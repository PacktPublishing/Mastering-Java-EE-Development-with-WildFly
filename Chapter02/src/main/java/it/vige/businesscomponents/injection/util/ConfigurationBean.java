package it.vige.businesscomponents.injection.util;

import static it.vige.businesscomponents.injection.util.ConfigurationKey.PRODUCER;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

public class ConfigurationBean {

	@Produces
	@ConfigurationValue(key = PRODUCER)
	public String produceConfigurationValue(InjectionPoint injectionPoint) {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfigurationValue annotation = annotated.getAnnotation(ConfigurationValue.class);
		if (annotation != null) {
			ConfigurationKey key = annotation.key();
			if (key != null) {
				switch (key) {
				case DEFAULT_DIRECTORY:
					return "/user/test";
				case VERSION:
					return "2.3.4";
				case BUILD_TIMESTAMP:
					return "10-10-2016:10:10:10";
				default:
					return null;
				}
			}
		}
		throw new IllegalStateException("No key for injection point: " + injectionPoint);
	}
}
