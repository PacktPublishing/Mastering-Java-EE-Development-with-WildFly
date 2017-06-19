package it.vige.businesscomponents.services.otherhttp;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

@Logged
public class RegisterCall implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String calledMethod = requestContext.getMethod();
		requestContext.setProperty("calledMethod", calledMethod);
	}

}
