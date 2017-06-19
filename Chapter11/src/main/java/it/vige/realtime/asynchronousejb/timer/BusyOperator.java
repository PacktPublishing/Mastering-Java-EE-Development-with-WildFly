package it.vige.realtime.asynchronousejb.timer;

import static java.lang.Thread.interrupted;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ejb.LockType.WRITE;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;

@Singleton
@Lock(WRITE)
public class BusyOperator {

	public static CountDownLatch count;

	@Asynchronous
	public Future<Object> stayBusy(CountDownLatch ready) {
		ready.countDown();

		try {
			count = new CountDownLatch(1);
			count.await();
		} catch (InterruptedException e) {
			interrupted();
		}

		return null;
	}

	@AccessTimeout(0)
	public void doItNow() {
		// do something
	}

	@AccessTimeout(value = 5, unit = SECONDS)
	public void doItSoon() {
		// do something
	}

}
