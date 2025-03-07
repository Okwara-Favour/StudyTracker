package MyServlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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
				jsonResponse.addProperty("redirect", "session.jsp");
				logInResponse = jsonResponse.toString();
				HttpSession session = request.getSession();
				session.setAttribute("userID", user.getId()); // Store user ID in session
				//response.sendRedirect("session.jsp");
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
				createResponse = "Contact successfully created";
			}
			else
			{
				createResponse = "Missing information or contact already exists in database";
			}
			
		    response.setContentType("text/html");
	        response.getWriter().write(createResponse);
		}
		
	    // Forward the request to the index.jsp page
	    //RequestDispatcher dispatcher = request.getRequestDispatcher(pageReturn);
	    //dispatcher.forward(request, response);
	}

}
