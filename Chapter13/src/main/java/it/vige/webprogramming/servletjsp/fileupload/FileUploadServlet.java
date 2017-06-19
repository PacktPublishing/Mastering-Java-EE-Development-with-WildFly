package it.vige.webprogramming.servletjsp.fileupload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns = { "/FileUploadServlet" })
@MultipartConfig(location = "/tmp")
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 501519541911873225L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>File Upload Servlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>File Upload Servlet</h1>");
			out.println("Receiving the uploaded file ...<br>");
			out.println("Received " + request.getParts().size() + " parts ...<br>");
			String fileName = "";
			for (Part part : request.getParts()) {
				fileName = part.getSubmittedFileName();
				out.println("... writing " + fileName + " part<br>");
				part.write(fileName);
				out.println("... written<br>");
			}
			out.println("... uploaded to: /tmp/" + fileName);
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
		return "File Upload Servlet";
	}
}