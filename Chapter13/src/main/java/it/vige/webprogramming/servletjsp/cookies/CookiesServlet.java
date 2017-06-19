package it.vige.webprogramming.servletjsp.cookies;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/CookiesServlet" })
@MultipartConfig(location = "/tmp")
public class CookiesServlet extends HttpServlet {

	private static final long serialVersionUID = -6655331420079266407L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet Cookies</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet TestServlet at " + request.getContextPath() + "</h1>");
			SessionCookieConfig cookies = request.getServletContext().getSessionCookieConfig();
			out.println("Found cookie: " + cookies.getName());

			Cookie cookie = new Cookie("myCookieKey", "myCookieValue");
			cookie.setMaxAge(60);
			response.addCookie(cookie);
			out.println("<br><br>Set a new cookie");

			cookie = new Cookie("myHttpOnlyCookieKey", "myHttpOnlyCookieValue");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(60);
			response.addCookie(cookie);
			out.println("<br>Set a new HTTPOnly Cookie<br><br>");
			out.println("Check what cookies are visible by");
			out.println("<a href=\"http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/view/index-cookies.jsp\">clicking here</a>");

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
		return "Cookies Servlet";
	}

}
