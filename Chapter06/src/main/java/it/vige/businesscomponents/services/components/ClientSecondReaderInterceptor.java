package it.vige.businesscomponents.services.components;

import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.Priorities.ENTITY_CODER;
import static javax.ws.rs.RuntimeType.CLIENT;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

@Provider
@ConstrainedTo(CLIENT)
@Priority(ENTITY_CODER + 20)
public class ClientSecondReaderInterceptor implements ReaderInterceptor {

	private static final Logger logger = getLogger(ClientSecondReaderInterceptor.class.getName());

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext interceptorContext)
			throws IOException, WebApplicationException {
		logger.info("ClientSecondReaderInterceptor invoked.");
		InputStream inputStream = interceptorContext.getInputStream();
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		String requestContent = new String(bytes);
		requestContent = requestContent + " Request changed in ClientSecondReaderInterceptor.";
		interceptorContext.setInputStream(new ByteArrayInputStream(requestContent.getBytes()));
		return interceptorContext.proceed();
	}
}
