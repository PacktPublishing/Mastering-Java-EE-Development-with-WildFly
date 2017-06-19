package it.vige.businesscomponents.services.components;

import static javax.ws.rs.Priorities.ENTITY_CODER;
import static javax.ws.rs.core.Response.serverError;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(ENTITY_CODER - 1)
public class BlockChainFilter implements ClientRequestFilter {

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		ResponseBuilder responseBuilder = serverError();
		Response response = responseBuilder.status(BAD_REQUEST).build();
		requestContext.abortWith(response);
	}

}
