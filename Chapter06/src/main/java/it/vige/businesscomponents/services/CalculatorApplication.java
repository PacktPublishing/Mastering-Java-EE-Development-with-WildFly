package it.vige.businesscomponents.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class CalculatorApplication extends Application {

	@Inject
	private MyResourceSingleton myResourceSingleton;

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<>();
		classes.add(RegisterOperation.class);
		classes.add(AddProperties.class);
		classes.add(Calculator.class);
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> objects = new HashSet<Object>();
		objects.add(myResourceSingleton);
		return objects;
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("property_1", "OK");
		properties.put("property_2", "NOT_OK");
		return properties;
	}
}
