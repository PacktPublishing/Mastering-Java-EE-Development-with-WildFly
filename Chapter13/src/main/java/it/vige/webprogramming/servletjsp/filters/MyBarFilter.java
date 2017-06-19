package it.vige.webprogramming.servletjsp.filters;

import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "MyBarFilter", urlPatterns = { "/filtered/*" })
public class MyBarFilter implements Filter {

	private static final Logger logger = getLogger(MyBarFilter.class.getName());

	private FilterConfig filterConfig;

	private void doBeforeProcessing(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		try (PrintWriter out = response.getWriter()) {
			out.print("my--");
			out.flush();
		}
	}

	private void doAfterProcessing(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		try (PrintWriter out = response.getWriter()) {
			out.print("--bar");
			out.flush();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		ResponseCharacterWrapper wrappedResponse = new ResponseCharacterWrapper((HttpServletResponse) response);

		doBeforeProcessing(request, wrappedResponse);
		chain.doFilter(request, wrappedResponse);
		doAfterProcessing(request, wrappedResponse);

		out.write(wrappedResponse.toString());
		logger.info("filter name: " + filterConfig.getFilterName());
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

}
