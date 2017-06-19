package it.vige.businesscomponents.services.otherhttp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class HttpApplication extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<>();
		classes.add(RegisterCall.class);
		classes.add(RegisterResponse.class);
		classes.add(HttpReceiver.class);
		return classes;
	}
}
