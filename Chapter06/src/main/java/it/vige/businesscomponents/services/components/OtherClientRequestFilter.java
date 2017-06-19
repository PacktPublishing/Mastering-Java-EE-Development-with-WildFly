package it.vige.businesscomponents.services.components;

import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.Priorities.ENTITY_CODER;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(ENTITY_CODER)
public class OtherClientRequestFilter implements ClientRequestFilter {

	private static final Logger logger = getLogger(OtherClientRequestFilter.class.getName());

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		logger.info("getAcceptableLanguages : " + requestContext.getAcceptableLanguages());
		logger.info("getClient : " + requestContext.getClient());
		logger.info("getConfiguration : " + requestContext.getConfiguration());
		logger.info("getCookies : " + requestContext.getCookies());
		logger.info("getDate : " + requestContext.getDate());
		logger.info("getEntity : " + requestContext.getEntity());
		logger.info("getEntityAnnotations : " + requestContext.getEntityAnnotations());
		logger.info("getEntityClass : " + requestContext.getEntityClass());
		logger.info("getEntityStream : " + requestContext.getEntityStream());
		logger.info("getEntityType : " + requestContext.getEntityType());
		logger.info("getHeaders : " + requestContext.getHeaders());
		logger.info("getLanguage : " + requestContext.getLanguage());
		logger.info("getMediaType : " + requestContext.getMediaType());
		logger.info("getMethod : " + requestContext.getMethod());
		logger.info("getPropertyNames : " + requestContext.getPropertyNames());
		logger.info("getStringHeaders : " + requestContext.getStringHeaders());
		logger.info("getUri : " + requestContext.getUri());
	}

}
