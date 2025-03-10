package MyServlets;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import io.jsonwebtoken.*;

import DatabaseModder.DatabaseLinker;
import DatabaseModder.Session;

/**
 * Servlet implementation class SessionServlet
 */
@WebServlet("/SessionServlet")
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//HttpSession session = request.getSession(false);
		
		//int userID = (int)session.getAttribute("userID");
		int userID = getUserID(request, response);
		
		String action = request.getParameter("action");
		JsonObject jsonResponse = new JsonObject();
		
		response.setContentType("application/json");
		
		jsonResponse.addProperty("console", action);
		
		if("viewsession".equals(action) || "viewallsessions".equals(action))
		{
			String topic = request.getParameter("topic");
			String date = request.getParameter("date");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			
			topic = "".equals(topic) ? null : topic;
			LocalDate dDate = DatabaseLinker.isValidDateFormat(date) ? LocalDate.parse(date) : null;
			LocalTime sTime = DatabaseLinker.isValidTimeFormat(startTime) ? LocalTime.parse(startTime) : null;
			LocalTime eTime = DatabaseLinker.isValidTimeFormat(endTime) ? LocalTime.parse(endTime) : null;
			
			List<Session> sessionList = DatabaseLinker.ViewSessions(userID, new Session(dDate, sTime, eTime, topic, null));
			
			StringBuilder tableHtml = new StringBuilder();
	        tableHtml.append("<table border='1'>");

	        // Table headers
	        tableHtml.append("<tr>");
	        tableHtml.append("<th>Date</th>");
	        tableHtml.append("<th>Start Time</th>");
	        tableHtml.append("<th>End Time</th>");
	        tableHtml.append("<th>Topic</th>");
	        tableHtml.append("<th>Comment</th>");
	        tableHtml.append("</tr>");

	        // Loop through the session list and create a table row for each session
	        for (Session sessionData : sessionList) {
	            tableHtml.append("<tr>");
	            tableHtml.append("<td>").append(sessionData.getDate()).append("</td>");
	            tableHtml.append("<td>").append(sessionData.getStartTime()).append("</td>");
	            tableHtml.append("<td>").append(sessionData.getEndTime()).append("</td>");
	            tableHtml.append("<td>").append(sessionData.getTopic()).append("</td>");
	            tableHtml.append("<td>").append(sessionData.getComment()).append("</td>");
	            tableHtml.append("</tr>");
	        }

	        // Close the table
	        tableHtml.append("</table>");

	        // Add the table HTML string to the JSON response
	        //jsonResponse.addProperty("message", sTime + " " + eTime + " " + startTime + " " + endTime);
	        jsonResponse.addProperty("table", tableHtml.toString());
		}
		response.getWriter().write(jsonResponse.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//HttpSession session = request.getSession(false);
		
		//int userID = (int)session.getAttribute("userID");
		int userID = getUserID(request, response);
		
		String action = request.getParameter("action");
		JsonObject jsonResponse = new JsonObject();
		
		response.setContentType("application/json");
		
		jsonResponse.addProperty("console", action);
		
		//jsonResponse.addProperty("uiade", testUserID);
		
		if ("startsession".equals(action))
		{
			String topic = request.getParameter("topic");
			String comment = request.getParameter("comment");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			
			topic = "".equals(topic) ? "None" : topic;
			comment = "".equals(comment) ? null : comment;
			
			Boolean isSessionAdded = DatabaseLinker.CreateSession(userID, 
					new Session(LocalDate.now(), LocalTime.parse(startTime), LocalTime.parse(endTime), topic, comment));
			
			if (isSessionAdded)
			{
				jsonResponse.addProperty("message", "Session successfully added");
			}
			else
			{
				jsonResponse.addProperty("message", "Session failed to add");
			}
			
		}
		else if("updatesession".equals(action))
		{
			String topic = request.getParameter("topic");
			String prevTopic = request.getParameter("prevTopic");
			String comment = request.getParameter("comment");
			String date = request.getParameter("date");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			
			topic = "".equals(topic) ? null : topic;
			prevTopic = "".equals(prevTopic) ? null : prevTopic;
			comment = "".equals(comment) ? null : comment;
			LocalDate dDate = DatabaseLinker.isValidDateFormat(date) ? LocalDate.parse(date) : null;
			LocalTime sTime = DatabaseLinker.isValidTimeFormat(startTime) ? LocalTime.parse(startTime) : null;
			LocalTime eTime = DatabaseLinker.isValidTimeFormat(endTime) ? LocalTime.parse(endTime) : null;
			
			String updateFeedback = DatabaseLinker.UpdateSession(userID, 
					new Session(dDate, sTime, eTime, topic, comment), prevTopic);
			
			
			jsonResponse.addProperty("message", updateFeedback);
			
		}
		else if("deletesession".equals(action))
		{
			String topic = request.getParameter("topic");
			String comment = request.getParameter("comment");
			String date = request.getParameter("date");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String clear = request.getParameter("clear");
			
			topic = "".equals(topic) ? null : topic;
			comment = "".equals(comment) ? null : comment;
			LocalDate dDate = DatabaseLinker.isValidDateFormat(date) ? LocalDate.parse(date) : null;
			LocalTime sTime = DatabaseLinker.isValidTimeFormat(startTime) ? LocalTime.parse(startTime) : null;
			LocalTime eTime = DatabaseLinker.isValidTimeFormat(endTime) ? LocalTime.parse(endTime) : null;
			
			//jsonResponse.addProperty("randomMessage", clear);
			
			if (clear.equals("False"))
			{
				String deleteFeedback = DatabaseLinker.DeleteSession(userID, 
						new Session(dDate, sTime, eTime, topic, comment));
				jsonResponse.addProperty("message", deleteFeedback);
			}
			else
			{
				String clearFeedback = DatabaseLinker.ClearSessions(userID);
				jsonResponse.addProperty("message", clearFeedback);
			}
			
		}
		response.getWriter().write(jsonResponse.toString());
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Default empty implementation
	}
	
	protected int getUserID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String jwtToken = request.getParameter("jwtToken");

	    if (jwtToken != null) {
	        try {
	            String[] chunks = jwtToken.split("\\.");
	            
	            Base64.Decoder decoder = Base64.getUrlDecoder();
	            
	            //String header = new String(decoder.decode(chunks[0]));
	            String payload = new String(decoder.decode(chunks[1]));
	            
	            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
	            return jsonObject.get("userId").getAsInt();

	        } catch (Exception e) {
	            // Handle invalid JWT token (e.g., expired or tampered)
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token.");
	            
	        }
	    } else {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT token missing.");
	    }
	    return -1;
	}
}
