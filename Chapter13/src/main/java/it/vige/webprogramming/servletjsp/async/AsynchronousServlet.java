package it.vige.webprogramming.servletjsp.async;

import static it.vige.webprogramming.servletjsp.async.State.onComplete;
import static it.vige.webprogramming.servletjsp.async.State.onError;
import static it.vige.webprogramming.servletjsp.async.State.onStartAsync;
import static it.vige.webprogramming.servletjsp.async.State.onTimeout;
import static it.vige.webprogramming.servletjsp.async.State.running;

import java.io.IOException;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/AsynchronousServlet", asyncSupported = true)
public class AsynchronousServlet extends HttpServlet {

	private static final long serialVersionUID = 1260919965694843835L;
	@Resource
	private ManagedExecutorService executor;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AsyncContext ac = request.startAsync();

		ac.addListener(new AsyncListener() {
			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				event.getSuppliedResponse().getWriter().println(onComplete);
			}

			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				event.getSuppliedResponse().getWriter().println(onTimeout);
				event.getAsyncContext().complete();
			}

			@Override
			public void onError(AsyncEvent event) throws IOException {
				event.getSuppliedResponse().getWriter().println(onError);
			}

			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {
				event.getSuppliedResponse().getWriter().println(onStartAsync);
			}
		});
		executor.submit(new MyAsyncService(ac));
	}

	class MyAsyncService implements Runnable {

		AsyncContext ac;

		public MyAsyncService(AsyncContext ac) {
			this.ac = ac;
		}

		@Override
		public void run() {
			try {
				ac.getResponse().getWriter().println(running);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			ac.complete();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Asynchronous Servlet";
	}
}
