package it.vige.webprogramming.servletjsp.nonblocking;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class ReadingListener implements ReadListener {

	private static final Logger logger = getLogger(ReadingListener.class.getName());

	private ServletInputStream input = null;
	private AsyncContext context = null;

	public ReadingListener(ServletInputStream in, AsyncContext ac) {
		this.input = in;
		this.context = ac;
	}

	@Override
	public void onDataAvailable() {
		try {
			int len = -1;
			byte b[] = new byte[1024];
			while (input.isReady() && (len = input.read(b)) != -1) {
				String data = new String(b, 0, len);
				logger.info("--> " + data);
			}
		} catch (IOException ex) {
			logger.log(SEVERE, null, ex);
		}
	}

	@Override
	public void onAllDataRead() {
		logger.info("onAllDataRead");
		context.complete();
	}

	@Override
	public void onError(Throwable t) {
		logger.log(SEVERE, "onError executed", t);
		context.complete();
	}
}
