package it.vige.businesscomponents.services.components;

import static io.undertow.util.Headers.CONTENT_MD5_STRING;
import static java.security.MessageDigest.getInstance;
import static java.util.Base64.getEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
public class ContentMD5Writer implements WriterInterceptor {

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

		MessageDigest digest = null;
		try {
			digest = getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DigestOutputStream digestStream = new DigestOutputStream(buffer, digest);

		OutputStream old = context.getOutputStream();
		context.setOutputStream(digestStream);
		try {
			context.proceed();

			byte[] hash = digest.digest();
			String encodedHash = getEncoder().encodeToString(hash);
			context.getHeaders().putSingle(CONTENT_MD5_STRING, encodedHash);
			byte[] content = buffer.toByteArray();
			old.write(content);
		} finally {
			context.setOutputStream(old);
		}
	}

}
