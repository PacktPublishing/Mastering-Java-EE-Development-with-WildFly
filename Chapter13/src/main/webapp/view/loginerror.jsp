<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Form-Based Login Error Page</title>
</head>
<body>
	<h1>Form-Based Login Error Page</h1>

	<h2>Invalid user name or password.</h2>

	<p>Please enter a user name or password that is authorized to
		access this application. For this application, make sure to create a
		user:
	<p>
	<p>

		Invoke "./bin/add-user.sh -a -u u1 -p p1 -g g1"<br> Click here to
		<a href="${pageContext.request.contextPath}/index.jsp">Try Again</a>
	</p>

</body>
</html>
