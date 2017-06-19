package it.vige.businesscomponents.services.components;

import static io.undertow.util.Headers.CONTENT_TYPE_STRING;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.Priorities.ENTITY_CODER;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(ENTITY_CODER + 2)
public class MyClientResponseFilter implements ClientResponseFilter {

	private static final Logger logger = getLogger(MyClientResponseFilter.class.getName());

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		logger.info("getAllowedMethods : " + responseContext.getAllowedMethods());
		logger.info("getCookies : " + responseContext.getCookies());
		logger.info("getDate : " + responseContext.getDate());
		logger.info("getEntityStream : " + responseContext.getEntityStream());
		logger.info("getEntityTag : " + responseContext.getEntityTag());
		logger.info("getHeaders : " + responseContext.getHeaders());
		logger.info("getLanguage : " + responseContext.getLanguage());
		logger.info("getLastModified : " + responseContext.getLastModified());
		logger.info("getLength : " + responseContext.getLength());
		logger.info("getLinks : " + responseContext.getLinks());
		logger.info("getLocation : " + responseContext.getLocation());
		logger.info("getMediaType : " + responseContext.getMediaType());
		logger.info("getStatus : " + responseContext.getStatus());
		logger.info("getStatusInfo : " + responseContext.getStatusInfo());
		responseContext.getHeaders().putSingle(CONTENT_TYPE_STRING,
				requestContext.getHeaderString(CONTENT_TYPE_STRING));
	}

}
