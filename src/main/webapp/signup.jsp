<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudyTracker</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>
    $(document).ready(function() {
        // Handle the login form submission using AJAX
        $("#CreateAccount").submit(function(event) {
            event.preventDefault(); // Prevent the default form submission (full page refresh)

            var formData = $(this).serialize(); // Serialize form data

            $.ajax({
                url: 'MainPageServlet', // The servlet URL
                type: 'POST', // HTTP method
                data: formData, // Data to be sent to the server
                success: function(response) {
                	if (response.message) {
                        $("#createResponse").html(response.message); // Assuming the servlet sends back HTML or a message
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });
        });
    });
</script>
</head>
<body>
<h2>Create account</h2>
<form id = "CreateAccount">
   	<input type="hidden" name="action" value="create">
   	First Name: <input type="text" name="firstname"> <br>
   	Last Name: <input type="text" name="lastname"> <br>
    Username: <input type="text" name="username"> <br>
    Password: <input type="password" name="password"> <br>
    <input type="submit" value="Sign up">
</form>

<div id="createResponse" style = "color:red"></div>

<br>
    <p> <a href="index.jsp">Return to log in page</a></p>
</body>
</html>