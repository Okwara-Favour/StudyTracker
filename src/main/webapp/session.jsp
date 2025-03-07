<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudySessions</title>
</head>
<body>
<h2> Study Session Page</h2>
	<%
        // Retrieve the userID from the session
        Integer userID = (Integer) session.getAttribute("userID");

        if (userID != null) {
    %>
        <p>User ID: <%= userID %></p>
    <%
        } else {
    %>
        <p style="color: red;">User ID is not set in session.</p>
    <%
        }
    %>
</body>
</html>