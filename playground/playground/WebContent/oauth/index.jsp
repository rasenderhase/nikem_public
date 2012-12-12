<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=uft-8">
<title>Willkommen bei nikem</title>
</head>
<body>
<h1>Willkommen bei nikem</h1>
<p>Uns sind folgende Daten über Sie bekannt:</p>
<pre>
<%= session.getAttribute("user") %>
</pre>


</body>
</html>