$(document).ready(function () {
	var startTime = null;
	var currentTime = new Date();
	var endTime = null;
	var intervalId = null;
	
	var sessionDataObject = {};
	sessionDataObject["action"] = "viewallsessions";
	
    $.ajax({
        url: 'SessionServlet', // Servlet to handle session data
        type: 'GET', // Use GET since we're retrieving data
        data: sessionDataObject,
        success: function (response) {
			console.log(response)
            if (response.table) {
                $("#SessionTable").html(response.table); // Insert profile name into h2
            } 
        },
        error: function () {
            $("#SessionTable").text("Error loading session");
        }
    });
    
    $("#StartSession").submit(function(event) {
    	event.preventDefault(); // Prevent the form from submitting normally
		var formData = new FormData(this);
		// Convert to an object and append additional data
		var dataObject = {};
		
		formData.forEach(function(value, key) {
		    dataObject[key] = value
		});
		
        // Get the value of the button that was clicked
		var buttonVal = event.submitter ? event.submitter.value : $("input[type='submit']:focus").val();

        // Check which button was pressed
        if (buttonVal === "Start") {
            console.log("Start button was pressed");
            startTime = new Date();
			
			$("#startTime").text(toTimeString(startTime)); // Update start time in HTML
            $("#elapsedTime").text("00:00:00");
			
			if (intervalId) {
                clearInterval(intervalId);
            }

            // Start interval to update elapsed time every second
            intervalId = setInterval(function() {
                currentTime = new Date();
				$("#elapsedTime").text(elapsedTime(startTime, currentTime));
                //console.log(elapsedTime(startTime, currentTime));
            }, 1000);
            // Handle logic for the "Start" button
        } else if (buttonVal === "Save") {
            console.log("Save button was pressed");
			endTime = new Date();
			
			if (startTime != null)
			{
				
				$("#endTime").text(toTimeString(endTime));
				dataObject["startTime"] = toTimeString(startTime);
				dataObject["endTime"] = toTimeString(endTime);
				
				$.ajax({
				    url: 'SessionServlet',
				    type: 'POST',
				    data: dataObject, // Send as key-value pairs
				    success: function(response) {
				        console.log("Data sent successfully!", response);
						if (response.message) {
	                        $("#addResponse").html(response.message); // Assuming the servlet sends back HTML or a message
	                    }
				    },
				    error: function(xhr, status, error) {
				        console.error("Error sending data:", error);
				    }
				});
				
			}
			
			startTime = null;
            // Handle logic for the "Save" button
			if (intervalId) {
                clearInterval(intervalId); // Stop updating elapsed time
            }
        }
    })
	
	$("#ViewSession").submit(function(event) {
        event.preventDefault(); // Prevent the default form submission (full page refresh)

        var formData = $(this).serialize(); // Serialize form data

        $.ajax({
            url: 'SessionServlet', // The servlet URL
            type: 'Get', // HTTP method
            data: formData, // Data to be sent to the server
			success: function (response) {
				console.log(response)
	            if (response.table) {
	                $("#SessionTable").html(response.table); // Insert profile name into h2
	            }
				if (response.message)
				{
					console.log(response.message)
				}
	        },
	        error: function () {
	            $("#SessionTable").html("Error loading session");
	        }
        });
    });
	
	$("#UpdateSession").submit(function(event) {
	    event.preventDefault(); // Prevent the default form submission (full page refresh)

	    var formData = $(this).serialize(); // Serialize form data
		
	    $.ajax({
	        url: 'SessionServlet', // The servlet URL
	        type: 'Post', // HTTP method
	        data: formData, // Data to be sent to the server
			success: function (response) {
				console.log(response)
				if (response.message)
				{
					$("#updateResponse").html(response.message);
				}
	        },
	        error: function () {
	            console.error("Error sending data:", error);
	        }
	    });
	});
	
	$("#DeleteSession").submit(function(event) {
	    event.preventDefault(); // Prevent the default form submission (full page refresh)

		var formData = new FormData(this);
				// Convert to an object and append additional data
		var dataObject = {};
		
		formData.forEach(function(value, key) {
		    dataObject[key] = value
		});
		
		dataObject["clear"] = "False";
		
		//var buttonVal = event.submitter ? event.submitter.value : $("input[type='submit']:focus").val();
		var buttonId = event.submitter ? event.submitter.id : $("input[type='submit']:focus").attr('id');
		
		if (buttonId === "dummyDelete")
		{
			$("#realDeleteDiv").html(
				"Are you sure you wish to perform this operation" + " " +
				" <input id = 'realDelete' type='submit' value='Confirm'> " + " " +
				" <input id = 'cancelDelete' type='submit' value='Cancel'> "
			)
		}
		else if	(buttonId === "dummyClear")
		{
			$("#realClearDiv").html(
				"Warning: !Operation will clear all sessions! <br> Are you sure you wish to perform this operation" + " " +
				" <input id = 'realClear' type='submit' value='Confirm'> " + " " +
				" <input id = 'cancelClear' type='submit' value='Cancel'> "
			)
		}
		else if (buttonId == "realDelete")
		{
			$.ajax({
		        url: 'SessionServlet', // The servlet URL
		        type: 'Post', // HTTP method
		        data: dataObject, // Data to be sent to the server
				success: function (response) {
					console.log(response)
					if (response.message)
					{
						$("#deleteResponse").html(response.message);
					}
		        },
		        error: function () {
		            console.error("Error sending data:", error);
		        }
		    });
			
			$("#realDeleteDiv").html("")
		}
		else if (buttonId == "realClear")
		{
			dataObject["clear"] = "True";	
			
			$.ajax({
		        url: 'SessionServlet', // The servlet URL
		        type: 'Post', // HTTP method
		        data: dataObject, // Data to be sent to the server
				success: function (response) {
					console.log(response)
					if (response.message)
					{
						$("#deleteResponse").html(response.message);
					}
		        },
		        error: function () {
		            console.error("Error sending data:", error);
		        }
		    });
			
			$("#realClearDiv").html("")
		}
		else if (buttonId == "cancelDelete")
		{
			$("#realDeleteDiv").html("")
		}
		else if (buttonId == "cancelClear")
		{
			$("#realClearDiv").html("")
		}
	});
	
	$('#actionDropdown').change(function(event) {
		event.preventDefault();
		
		
        var selectedAction = $(this).val(); // Get selected option

        // Dynamically update form based on selection
		switch (selectedAction) {
		    case 'ViewDropdown':
		        $('#startForm').css('display', 'none');
		        $('#updateForm').css('display', 'none');
		        $('#viewForm').css('display', 'block');
		        $('#deleteForm').css('display', 'none');
		        break;
		    case 'StartDropdown':
		        $('#startForm').css('display', 'block');
		        $('#updateForm').css('display', 'none');
		        $('#viewForm').css('display', 'none');
		        $('#deleteForm').css('display', 'none');
		        break;
		    case 'UpdateDropdown':
		        $('#startForm').css('display', 'none');
		        $('#updateForm').css('display', 'block');
		        $('#viewForm').css('display', 'none');
		        $('#deleteForm').css('display', 'none');
		        break;
		    case 'DeleteDropdown':
		        $('#startForm').css('display', 'none');
		        $('#updateForm').css('display', 'none');
		        $('#viewForm').css('display', 'none');
		        $('#deleteForm').css('display', 'block');
		        break;
		}
    });
});


function elapsedTime(previousTime, currentTime)
{
	// Calculate the difference in milliseconds
	var timeDifference = currentTime - previousTime;

	// Convert milliseconds into seconds, minutes, and hours
	var seconds = Math.floor(timeDifference / 1000); // Convert to seconds
	var minutes = Math.floor(seconds / 60); // Convert to minutes
	var hours = Math.floor(minutes / 60); // Convert to hours

	// Get remaining seconds and minutes
	seconds = seconds % 60;
	minutes = minutes % 60;
	
	var formattedTime = 
	        hours.toString().padStart(2, '0') + ":" +
	        minutes.toString().padStart(2, '0') + ":" +
	        seconds.toString().padStart(2, '0');

	return formattedTime;
}

function toTimeString(date)
{
	var hours = date.getHours().toString().padStart(2, '0');
	var minutes = date.getMinutes().toString().padStart(2, '0');
	var seconds = date.getSeconds().toString().padStart(2, '0');
	return hours+":"+minutes+":"+seconds;
}