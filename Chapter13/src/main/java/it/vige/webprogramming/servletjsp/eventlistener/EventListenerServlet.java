package it.vige.webprogramming.servletjsp.eventlistener;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/EventListenerServlet")
public class EventListenerServlet extends HttpServlet {

	private static final long serialVersionUID = 5505334314682729035L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet Event Listeners</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Servlet Event Listeners</h1>");
		out.println("<h2>Setting, updating, and removing ServletContext Attributes</h2>");
		request.getServletContext().setAttribute("attribute1", "attribute-value1");
		request.getServletContext().setAttribute("attribute1", "attribute-updated-value1");
		request.getServletContext().removeAttribute("attribute1");
		out.println("done");
		out.println("<h2>Setting, updating, and removing HttpSession Attributes</h2>");
		request.getSession(true).setAttribute("attribute1", "attribute-value1");
		request.getSession().setAttribute("attribute1", "attribute-updated-value1");
		request.getSession().removeAttribute("attribute1");
		out.println("done");
		out.println("<h2>Setting, updating, and removing ServletRequest Attributes</h2>");
		request.setAttribute("attribute1", "attribute-value1");
		request.setAttribute("attribute1", "attribute-updated-value1");
		request.removeAttribute("attribute1");
		out.println("done");
		out.println("<h2>Invalidating session</h2>");
		request.getSession().invalidate();
		out.println("done");
		out.println("<br><br>Check output in server log");
		out.println("</body>");
		out.println("</html>");
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
		return "Event Listener Servlet";
	}
}
