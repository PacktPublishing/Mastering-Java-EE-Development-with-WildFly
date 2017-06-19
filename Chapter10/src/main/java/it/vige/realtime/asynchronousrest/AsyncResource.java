package it.vige.realtime.asynchronousrest;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;

@Path("/resource")
public class AsyncResource {

	private int numberOfSuccessResponses;
	private int numberOfFailures;
	private Throwable lastException;

	@GET
	@Path("/simple")
	public void asyncGet(@Suspended final AsyncResponse asyncResponse) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = veryExpensiveOperation();
				asyncResponse.resume(result);
			}

			private String veryExpensiveOperation() {
				return new MagicNumber(3) + "";
			}
		}).start();
	}

	@GET
	@Path("/withTimeout")
	public void asyncGetWithTimeout(@Suspended final AsyncResponse asyncResponse) {
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {

			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(status(SERVICE_UNAVAILABLE).entity("Operation time out.").build());
			}
		});
		asyncResponse.setTimeout(1, SECONDS);

		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = veryExpensiveOperation();
				asyncResponse.resume(result);
			}

			private String veryExpensiveOperation() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
				}
				return new MagicNumber(10) + "";
			}
		}).start();
	}

	@GET
	@Path("/withCallback")
	public void asyncGetWithCallback(@Suspended final AsyncResponse asyncResponse) {
		asyncResponse.register(new CompletionCallback() {
			@Override
			public void onComplete(Throwable throwable) {
				if (throwable == null) {
					numberOfSuccessResponses++;
				} else {
					numberOfFailures++;
					lastException = throwable;
				}
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = veryExpensiveOperation();
				asyncResponse.resume(result);
			}

			private String veryExpensiveOperation() {
				return new MagicNumber(22) + "";
			}
		}).start();
	}

	public int getNumberOfSuccessResponses() {
		return numberOfSuccessResponses;
	}

	public int getNumberOfFailures() {
		return numberOfFailures;
	}

	public Throwable getLastException() {
		return lastException;
	}
}
