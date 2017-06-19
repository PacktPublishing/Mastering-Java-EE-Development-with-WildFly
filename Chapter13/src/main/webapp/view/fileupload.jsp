<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Servlet : Cookies</title>

</head>
<body>
	<h1>Servlet : File Upload</h1>

	Select a file to upload:
	<br />
	<form action="${pageContext.request.contextPath}/FileUploadServlet"
		method="post" enctype="multipart/form-data">
		<input type="file" name="file" size="50" /> <br /> <input
			type="submit" value="Upload File" />
	</form>
</body>
</html>
