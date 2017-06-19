package it.vige.webprogramming.servletjsp.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationDynamicServlet extends HttpServlet {

	private static final long serialVersionUID = -3915226545951013616L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print("dynamic GET");
	}

	@Override
	public String getServletInfo() {
		return "My registration servlet!";
	}
}