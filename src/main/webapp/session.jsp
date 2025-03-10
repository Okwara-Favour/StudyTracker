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
<script src="https://cdn.jsdelivr.net/npm/jwt-decode@3.1.2/build/jwt-decode.min.js"></script>
<script src="Javascript/session.js"></script>
</head>
<body>
<h2> Study Session Page</h2>
<div id = "Username"></div>

	<table>
    <tr>
        <td>
		    <h3>Select a Session Action</h3>
		    <form id="formSelector">
		        <label for="actionDropdown">Choose an action:</label>
		        <select id="actionDropdown">
		            <option value="ViewDropdown">View Session</option>
		            <option value="StartDropdown">Start Session</option>
		            <option value="UpdateDropdown">Update Session</option>
		            <option value="DeleteDropdown">Delete Session</option>
		        </select>
		    </form>
		
		    <div id="startForm" style="display:none;">
		        <h3>Start Session</h3>
		        <form id="StartSession" action="your-server-endpoint" method="post">
		            <input type="hidden" name="action" value="startsession">
		            Topic: <input type="text" name="topic"><br>
		            Comment: <input type="text" name="comment"><br>
		            <input type="submit" value="Start">
		            <br><br>
		            <input type="submit" value="Save">
		        </form>
		        <div id = "addResponse" style = "color:red"></div>
			    <p>Start Time: <span id="startTime">None</span></p>
				<p>Elapsed Time: <span id="elapsedTime">None</span></p>
				<p>End Time: <span id="endTime">None</span></p>
		    </div>
		
		    <div id="updateForm" style="display:none;">
		        <h3>Update Session</h3>
		        <form id="UpdateSession" action="your-server-endpoint" method="post">
		            <input type="hidden" name="action" value="updatesession">
		            Previous Topic: <input type="text" name="prevTopic"><br>
		            Topic: <input type="text" name="topic"><br>
		            Comment: <input type="text" name="comment"><br>
		            Date<i>(yyyy-mm-dd)</i>: <input type="text" name="date"><br>
		            Start Time<i>(hh:mm:ss)</i>: <input type="text" name="starttime"><br>
		            End Time<i>(hh:mm:ss)</i>: <input type="text" name="endtime"><br>
		            <input type="submit" value="Update">
		        </form>
		        <div id = "updateResponse" style = "color:red"></div>
		    </div>
		
		    <div id="viewForm" style="display:block;">
		        <h3>View Session</h3>
		        <form id="ViewSession" action="your-server-endpoint" method="post">
		            <input type="hidden" name="action" value="viewsession">
		            Topic: <input type="text" name="topic"><br>
		            Date<i>(yyyy-mm-dd)</i>: <input type="text" name="date"><br>
		            Start Time<i>(hh:mm:ss)</i>: <input type="text" name="startTime"><br>
		            End Time<i>(hh:mm:ss)</i>: <input type="text" name="endTime"><br>
		            <input type="submit" value="View">
		        </form>
		    </div>
		
		    <div id="deleteForm" style="display:none;">
		        <h3>Delete Session</h3>
		        <form id="DeleteSession" action="your-server-endpoint" method="post">
		            <input type="hidden" name="action" value="deletesession">
		            Topic: <input type="text" name="topic"><br>
		            Comment: <input type="text" name="comment"><br>
		            Date<i>(yyyy-mm-dd)</i>: <input type="text" name="date"><br>
		            Start Time<i>(hh:mm:ss)</i>: <input type="text" name="starttime"><br>
		            End Time<i>(hh:mm:ss)</i>: <input type="text" name="endtime"><br>
		            <input id="dummyDelete" type="submit" value="Delete">
		            <input id="dummyClear" type="submit" value="Clear">
		            <br>
		            <div id="realDeleteDiv"></div>
		            <div id="realClearDiv"></div>
		        </form>
		        <div id = "deleteResponse" style = "color:red"></div>
		    </div>
		</td>
        <td>
        <h2>
        <b>Sessions</b> 
        </h2>
        <div id="SessionTable"></div>
        </td>
    </tr>
</table>
</body>
</html>