$(document).ready(function () {
	var startTime = null;
	var currentTime = new Date();
	var endTime = null;
	var intervalId = null;
	
	
	/*
    $.ajax({
        url: 'SessionServlet', // Servlet to handle session data
        type: 'GET', // Use GET since we're retrieving data
        dataType: 'json', // Expect JSON response
        success: function (response) {
            if (response.profileName) {
                $("#profileName").text(response.profileName); // Insert profile name into h2
            } else {
                $("#profileName").text("Guest"); // Default if profile name not found
            }
        },
        error: function () {
            $("#profileName").text("Error loading profile");
        }
    });
    */
    $("#StartSession").submit(function(event) {
    	event.preventDefault(); // Prevent the form from submitting normally
		var formData = $(this).serialize();
		
        // Get the value of the button that was clicked
        var buttonVal = $("input[type='submit']:focus").val();

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
                console.log(elapsedTime(startTime, currentTime));
            }, 1000);
            // Handle logic for the "Start" button
        } else if (buttonVal === "Save") {
            console.log("Save button was pressed");
			endTime = new Date();
			
			if (startTime != null)
			{
				$("#endTime").text(toTimeString(endTime));
			}
			
            // Handle logic for the "Save" button
			if (intervalId) {
                clearInterval(intervalId); // Stop updating elapsed time
            }
        }
    })
	
	if (startTime != null)
	{
		console.log(elapsedTime(startTime, currentTime));	
	}
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