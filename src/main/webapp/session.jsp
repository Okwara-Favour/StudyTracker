<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>StudySessions</title>
<style>
        table {
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: left;
            vertical-align: top; /* Align text to the top-left */
        }
        
</style>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="Javascript/session.js"></script>
</head>
<body>
<h2> Study Session Page</h2>
	<%
		String username = (String) session.getAttribute("username");
		
        if (username != null) {
    %>
        <h2> Welcome <%= username %>. </h2>
    <%
        } else {
    %>
        <p style="color: red;">Welcome guest.</p>
    <%
        }
    %>

	<table>
    <tr>
        <td>
            <!-- Nested table in the first column with 3 rows -->
            <table>
                <tr>
                    <td>
                    <h3>Start Session</h3>
                    <form id = "StartSession">
				    	<input type="hidden" name="action" value="startsession">
				        Topic: <input type="text" name="topic"> <br>
				        Comment: <input type="text" name="comment"> <br>
				        <input type="submit" value="Start">
				        <br><br>
				        <input type="submit" value="Save">
				    </form>
				    <p>Start Time: <span id="startTime">None</span></p>
					<p>Elapsed Time: <span id="elapsedTime">None</span></p>
					<p>End Time: <span id="endTime">None</span></p>
                    </td>
                </tr>
                <tr>
                	<td>
                    <h3>Update Session</h3>
                    <form id = "UpdateSession">
				    	<input type="hidden" name="action" value="updatesession">
				        Topic: <input type="text" name="topic"> <br>
				        Comment: <input type="text" name="comment"> <br>
				        Date<i>(yyyy-mm-dd)</i>: <input type="text" name="date"> <br>
				        Start Time<i>(hh:mm:ss)</i>: <input type="text" name="starttime"> <br>
				        End Time<i>(hh:mm:ss)</i>: <input type="text" name="endtime"> <br>
				        <input type="submit" value="Update">
				    </form>
                    </td>
                </tr>
                <tr>
                    <td>
                    <h3>View Session</h3>
                    <form id = "ViewSession">
				    	<input type="hidden" name="action" value="viewsession">
				        Date<i>(yyyy-mm-dd)</i>: <input type="text" name="date"> <br>
				        Start Time<i>(hh:mm:ss)</i>: <input type="text" name="starttime"> <br>
				        End Time<i>(hh:mm:ss)</i>: <input type="text" name="endtime"> <br>
				        <input type="submit" value="View">
				    </form>
                    </td>
                </tr>
            </table>
        </td>
        <td>
        <h2>
        <b>Sessions</b> 
        </h2>
        </td>
    </tr>
</table>
</body>
</html>