package it.vige.webprogramming.servletjsp.secure;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 979511840316053449L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String user = request.getParameter("user");
		String password = request.getParameter("password");

		if (user != null && password != null) {
			request.login(user, password);
		}

		userDetails(out, request);
	}

	private void userDetails(PrintWriter out, HttpServletRequest request) {
		out.println("isUserInRole?" + request.isUserInRole("g1"));
		out.println("getRemoteUser?" + request.getRemoteUser());
		out.println("getUserPrincipal?" + request.getUserPrincipal());
		out.println("getAuthType?" + request.getAuthType());
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
		return "Login programmatic Servlet";
	}

}
