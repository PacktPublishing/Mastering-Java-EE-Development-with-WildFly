package it.vige.realtime.asynchronousejb.bean;

import static java.util.logging.Logger.getLogger;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;

@Singleton
@Asynchronous
public class AsyncBean {

	private static final Logger logger = getLogger(AsyncBean.class.getName());
	
	public void ignoreResult(int a, int b) {
		logger.info("it's asynchronous");
	}

	public Future<Integer> longProcessing(int a, int b) {
		logger.info("the calling thread will waith when the Future.get is excuted");
		return new AsyncResult<Integer>(a * b);
	}
}
