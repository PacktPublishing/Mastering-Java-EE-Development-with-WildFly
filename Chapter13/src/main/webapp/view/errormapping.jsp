<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Servlet : Error Mapping</title>
</head>
<body>
	<h1>Servlet : Error Mapping</h1>

	Go to a page that
	<a href="404.jsp">does not exist</a>.
	<br>
	<br> Call a Servlet that
	<a href="${pageContext.request.contextPath}/ErrorMappingServlet">throws
		exception</a>
</body>
</html>
