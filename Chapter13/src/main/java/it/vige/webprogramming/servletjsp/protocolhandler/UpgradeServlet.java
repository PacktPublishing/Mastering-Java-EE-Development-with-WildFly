package it.vige.webprogramming.servletjsp.protocolhandler;

import static java.util.logging.Logger.getLogger;
import static javax.servlet.http.HttpServletResponse.SC_SWITCHING_PROTOCOLS;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/UpgradeServlet" })
public class UpgradeServlet extends HttpServlet {

	private static final long serialVersionUID = 5433545980549287085L;

	private static final Logger logger = getLogger(UpgradeServlet.class.getName());

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet UpgradeServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet UpgradeServlet at " + request.getContextPath() + "</h1>");
			if (request.getHeader("Upgrade").equals("echo")) {
				response.setStatus(SC_SWITCHING_PROTOCOLS);
				response.setHeader("Connection", "Upgrade");
				response.setHeader("Upgrade", "echo");
				request.upgrade(SampleProtocolHandler.class);
				logger.info("Request upgraded to SampleProtocolHandler");
			}
			out.println("</body>");
			out.println("</html>");
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
		return "Upgrade Servlet";
	}
}
