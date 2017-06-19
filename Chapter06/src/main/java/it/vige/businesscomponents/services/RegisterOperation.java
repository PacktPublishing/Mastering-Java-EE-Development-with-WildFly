package it.vige.businesscomponents.services;

import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class RegisterOperation implements ContainerRequestFilter {

	private static final Logger logger = getLogger(RegisterOperation.class.getName());
	
	public static String calledMethod;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.info("getHeaders : " + requestContext.getHeaders());
		logger.info("getDate : " + requestContext.getDate());
		logger.info("getLanguage : " + requestContext.getLanguage());
		logger.info("getEntityStream : " + requestContext.getEntityStream());
		logger.info("getLength : " + requestContext.getLength());
		logger.info("getMediaType : " + requestContext.getMediaType());
		logger.info("getMethod : " + requestContext.getMethod());
		logger.info("getPropertyNames : " + requestContext.getPropertyNames());
		logger.info("getRequest : " + requestContext.getRequest());
		logger.info("getSecurityContext : " + requestContext.getSecurityContext());
		logger.info("getUriInfo : " + requestContext.getUriInfo());
		calledMethod = requestContext.getMethod();
	}

}
