package it.vige.realtime.javamail;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.RejectException;

public class MyMessageHandlerFactory implements MessageHandlerFactory {

	private static final Logger logger = getLogger(MyMessageHandlerFactory.class.getName());

	private String body;
	
	public MessageHandler create(MessageContext ctx) {
		return new Handler(ctx);
	}

	class Handler implements MessageHandler {
		MessageContext ctx;

		public Handler(MessageContext ctx) {
			this.ctx = ctx;
		}

		public void from(String from) throws RejectException {
			logger.info("FROM:" + from);
		}

		public void recipient(String recipient) throws RejectException {
			logger.info("RECIPIENT:" + recipient);
		}

		public void data(InputStream data) throws IOException {
			logger.info("MAIL DATA");
			logger.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
			body = this.convertStreamToString(data);
			logger.info(body);
			logger.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		}

		public void done() {
			logger.info("Finished");
		}

		public String convertStreamToString(InputStream is) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				logger.log(SEVERE, "activiti diagram", e);
			}
			return sb.toString();
		}

	}
	
	public String getBody() {
		return body;
	}
}