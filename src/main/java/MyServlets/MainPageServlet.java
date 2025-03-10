package MyServlets;

//import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import io.jsonwebtoken.*;

import DatabaseModder.DatabaseLinker;
import DatabaseModder.User;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MainPageServlet
 */
@WebServlet("/MainPageServlet")
public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String action = request.getParameter("action");
		JsonObject jsonResponse = new JsonObject();
		
		response.setContentType("application/json");
		
		if ("logIn".equals(action))
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			User user = DatabaseLinker.LogInUser(username, password);
			
			String logInResponse = null;
			if (user == null)
			{
				jsonResponse.addProperty("message", "User does not exist in database");
				logInResponse = jsonResponse.toString();
			}
			else
			{
				String jwtToken = Jwts.builder()
				        .subject(user.getProfileName())
				        .claim("userId", user.getId())
				        .compact();

				// Send the JWT token to the client
				Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
				jwtCookie.setHttpOnly(true);  // To prevent JavaScript access
				jwtCookie.setSecure(true);    // Only sent over HTTPS (optional, for security)
				jwtCookie.setPath("/");  // Only sent to /session.jsp
				response.addCookie(jwtCookie);
				
				jsonResponse.addProperty("redirect", "session.jsp?jwtToken=" + jwtToken);
				logInResponse = jsonResponse.toString();
				//HttpSession session = request.getSession();
				//session.setAttribute("username", user.getProfileName());
				//session.setAttribute("userID", user.getId()); // Store user ID in session
			}
			
	        response.getWriter().write(logInResponse);
		}
		else if("create".equals(action))
		{
			//pageReturn = "signup.jsp";
			
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			Boolean isCreated = DatabaseLinker.CreateContact(username, password, firstname, lastname);
			
			String createResponse = null;
			if (isCreated)
			{
				jsonResponse.addProperty("message", "Contact successfully created");
				createResponse = jsonResponse.toString();
			}
			else
			{
				jsonResponse.addProperty("message", "Missing information or contact already exists in database");
				createResponse = jsonResponse.toString();
			}
	        response.getWriter().write(createResponse);
		}
		
	    // Forward the request to the index.jsp page
	    //RequestDispatcher dispatcher = request.getRequestDispatcher(pageReturn);
	    //dispatcher.forward(request, response);
	}

}
