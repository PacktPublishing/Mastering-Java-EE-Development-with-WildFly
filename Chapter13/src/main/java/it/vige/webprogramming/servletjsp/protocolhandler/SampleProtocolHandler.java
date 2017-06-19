package it.vige.webprogramming.servletjsp.protocolhandler;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

public class SampleProtocolHandler implements HttpUpgradeHandler {

	public static final String CRLF = "\r\n";

	@Override
	public void init(WebConnection wc) {
		try (ServletInputStream input = wc.getInputStream(); ServletOutputStream output = wc.getOutputStream();) {
			output.write(("upgrade" + CRLF).getBytes());
			output.write(("received" + CRLF).getBytes());
			output.write("END".getBytes());
		} catch (IOException ex) {
		}
	}

	@Override
	public void destroy() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
