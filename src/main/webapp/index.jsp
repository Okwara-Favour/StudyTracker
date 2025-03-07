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
        $("#LogIn").submit(function(event) {
            event.preventDefault(); // Prevent the default form submission (full page refresh)

            console.log("Form submitted");
            var formData = $(this).serialize(); // Serialize form data

            $.ajax({
                url: 'MainPageServlet', // The servlet URL
                type: 'POST', // HTTP method
                data: formData,
                dataType: "json", // Data to be sent to the server
                success: function(response) {
                	if (response.redirect) {
                        window.location.href = response.redirect; // Redirect user
                    } 
                	if (response.message) {
                    	console.log("Success, server response:", response);
                        $("#loginResponse").html(response.message); // Assuming the servlet sends back HTML or a message
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
<h2>Study Tracker</h2>
    <form id = "LogIn">
    	<input type="hidden" name="action" value="logIn">
        Username: <input type="text" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"> <br>
        Password: <input type="password" name="password" value="<%= request.getAttribute("password") != null ? request.getAttribute("password") : "" %>"> <br>
        <input type="submit" value="Log in">
    </form>
    
    <div id="loginResponse" style = "color:red"></div>
    
    <br>
    <p> Don't have an account? <a href="signup.jsp">Sign Up</a></p>
</body>
</html>