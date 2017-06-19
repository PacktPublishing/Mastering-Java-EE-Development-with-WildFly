package it.vige.webprogramming.servletjsp.nonblocking;

import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/ReadClient" })
public class ReadClient extends HttpServlet {

	private static final Logger logger = getLogger(ReadClient.class.getName());

	private static final long serialVersionUID = -790666727441790093L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Invoke the servlet clients</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Invoke the servlet clients</h1>");

			String path = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
					+ "/WritingServlet";
			out.println("Invoking the endpoint: " + path + "<br>");
			out.flush();
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try (BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				out.println("Sending data ..." + "<br>");
				out.flush();
				out.println(input.readLine());
				out.println("Sleeping ..." + "<br>");
				out.flush();
				sleep(5000);
				out.println("Sending more data ..." + "<br>");
				out.flush();
				out.println(input.readLine());
				input.close();
			}
			out.println("<br><br>Check server.log for output");
			out.println("</body>");
			out.println("</html>");
		} catch (InterruptedException | IOException ex) {
			logger.log(SEVERE, null, ex);
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
		return "Short description";
	}
}
