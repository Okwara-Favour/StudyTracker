package DatabaseModder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;

import DatabaseModder.User;

public class DatabaseLinker
{
	public static Boolean CreateSession(int contactId, Session session)
	{
		String sql = "INSERT INTO sessions (contact_id, date, start_time, end_time, topic, comment)" + " VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, contactId);
			preparedStatement.setDate(2, java.sql.Date.valueOf(session.getDate()));  // Ensures proper format
	        preparedStatement.setTime(3, java.sql.Time.valueOf(session.getStartTime()));
	        preparedStatement.setTime(4, java.sql.Time.valueOf(session.getEndTime()));
			preparedStatement.setString(5, session.getTopic());
			preparedStatement.setString(6, session.getComment());
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("new session inserted");
				return true;
			}
			else
			{
				System.out.println("Session failed to create");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of session creation failed during statement preparation");
		}
		return false;
	}
	
	/*
	public static void UpdateSession(int contactId, Session session) {
	    String sql = "UPDATE sessions SET topic = ?, comment = ? WHERE contact_id = ? AND date = ? AND start_time = ? AND end_time = ?";
	    try (Connection connection = ConnectionManager.getConnection()) {
	        PreparedStatement preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, session.getTopic());
	        preparedStatement.setString(2, session.getComment());
	        preparedStatement.setInt(3, contactId);
	        preparedStatement.setDate(4, java.sql.Date.valueOf(session.getDate()));
	        preparedStatement.setTime(5, java.sql.Time.valueOf(session.getStartTime()));
	        preparedStatement.setTime(6, java.sql.Time.valueOf(session.getEndTime()));

	        int rows = preparedStatement.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Session updated successfully");
	        } else {
	            System.out.println("No session found with the given details");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Execution of session update failed during statement preparation");
	    }
	}
	*/
	
	public static String UpdateSession(int contactId, Session session, String prevTopic) {
	    String sql = "UPDATE sessions SET ";
	    List<String> updates = new ArrayList<>();
	    List<Object> params = new ArrayList<>();

	    if (session.getTopic() != null) {
	        updates.add("topic = ?");
	        params.add(session.getTopic());
	    }
	    if (session.getComment() != null) {
	        updates.add("comment = ?");
	        params.add(session.getComment());
	    }

	    if (updates.isEmpty()) {
	        //System.out.println("No fields to update.");
	        return "No fields to update.";
	    }
	    
	    if (prevTopic == null && session.getDate() == null)
	    {
	    	return "Previous topic or date must be specified";
	    }

	    sql += String.join(", ", updates) + " WHERE contact_id = ?";
	    params.add(contactId);
	    
	    if (prevTopic != null) {
	        sql += " AND topic = ?";
	        params.add(prevTopic);
	    }
	    
	    if (session.getDate() != null) {
	        sql += " AND date = ?";
	        params.add(java.sql.Date.valueOf(session.getDate()));
	    }
	    
	    if (session.getStartTime() != null) {
	        sql += " AND start_time = ?";
	        params.add(java.sql.Time.valueOf(session.getStartTime()));
	    }
	    if (session.getEndTime() != null) {
	        sql += " AND end_time = ?";
	        params.add(java.sql.Time.valueOf(session.getEndTime()));
	    }

	    try (Connection connection = ConnectionManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        for (int i = 0; i < params.size(); i++) {
	            preparedStatement.setObject(i + 1, params.get(i));
	        }

	        int rows = preparedStatement.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Session updated successfully");
	            return "Session updated successfully";
	        } else {
	            System.out.println("No session found with the given details");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Execution of session update failed during statement preparation");
	    }
	    
	    return "Execution of session update failed during statement preparation";
	}
	
	public static boolean isValidDateFormat(String date) {
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        formatter.parse(date); // Try parsing the date
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	
	public static boolean isValidTimeFormat(String time) {
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	        formatter.parse(time); // Try parsing the time
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public static List<Session> ViewSessions(int contactId, Session session) {
	    String sql = "SELECT * FROM sessions WHERE contact_id = ?";

	    // Add conditions for the date and time formats
	    if (session.getDate() != null) {
	        sql += " AND date = ?";
	    }
	    if (session.getStartTime() != null) {
	        sql += " AND start_time = ?";
	    }
	    if (session.getEndTime() != null) {
	        sql += " AND end_time = ?";
	    }
	    
	    // Add condition for topic if it's not 'None'
	    if (session.getTopic() != null) {
	        sql += " AND topic = ?";
	    }

	    List<Session> sessionList = new ArrayList<>();

	    try (Connection connection = ConnectionManager.getConnection()) {
	        PreparedStatement preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setInt(1, contactId);

	        int parameterIndex = 2;
	        // Set date parameter if valid
	        if (session.getDate() != null) {
	            preparedStatement.setDate(parameterIndex++, java.sql.Date.valueOf(session.getDate()));
	        }

	        // Set startTime parameter if valid
	        if (session.getStartTime() != null) {
	            preparedStatement.setTime(parameterIndex++, java.sql.Time.valueOf(session.getStartTime()));
	        }

	        // Set endTime parameter if valid
	        if (session.getEndTime() != null) {
	            preparedStatement.setTime(parameterIndex++, java.sql.Time.valueOf(session.getEndTime()));
	        }

	        // Set topic parameter if not 'None'
	        if (session.getTopic() != null) {
	            preparedStatement.setString(parameterIndex++, session.getTopic());
	        }

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            // Retrieve data from the result set and create session objects
	            Session newSession = new Session(
	                resultSet.getDate("date").toLocalDate(),
	                resultSet.getTime("start_time").toLocalTime(),
	                resultSet.getTime("end_time").toLocalTime(),
	                resultSet.getString("topic"),
	                resultSet.getString("comment")
	            );

	            sessionList.add(newSession);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Execution of session view failed during statement preparation");
	    }

	    return sessionList;
	}
	
	/*
	public static void DeleteSession(int contactId, Session session)
	{
		String sql = "DELETE FROM sessions WHERE contact_id = ? AND date = ? AND start_time = ? AND end_time = ?";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, contactId);
			preparedStatement.setDate(2, java.sql.Date.valueOf(session.getDate()));  // Ensures proper format
	        preparedStatement.setTime(3, java.sql.Time.valueOf(session.getStartTime()));
	        preparedStatement.setTime(4, java.sql.Time.valueOf(session.getEndTime()));
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("session deleted");
			}
			else
			{
				System.out.println("session not found");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of session delete failed during statement preparation");
		}
	}*/


	public static String DeleteSession(int contactId, Session session) {
		if (session.getTopic() == null && session.getComment() == null &&
			session.getDate() == null && session.getStartTime() == null && 
			session.getEndTime() == null)
		{
			return "Cannot delete without a specified row type. Use a clear request instead";
		}
		
		
	    String sql = "DELETE FROM sessions WHERE contact_id = ?";
	    List<Object> params = new ArrayList<>();
	    params.add(contactId);
	    
	    if (session.getTopic() != null) {
	        sql += " AND topic = ?";
	        params.add(session.getTopic());
	    }
	    
	    if (session.getComment() != null) {
	        sql += " AND comment = ?";
	        params.add(session.getComment());
	    }
	    
	    if (session.getDate() != null) {
	        sql += " AND date = ?";
	        params.add(java.sql.Date.valueOf(session.getDate()));
	    }
	    if (session.getStartTime() != null) {
	        sql += " AND start_time = ?";
	        params.add(java.sql.Time.valueOf(session.getStartTime()));
	    }
	    if (session.getEndTime() != null) {
	        sql += " AND end_time = ?";
	        params.add(java.sql.Time.valueOf(session.getEndTime()));
	    }

	    try (Connection connection = ConnectionManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        for (int i = 0; i < params.size(); i++) {
	            preparedStatement.setObject(i + 1, params.get(i));
	        }

	        int rows = preparedStatement.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Session deleted successfully");
	            return "Session deleted successfully";
	        } else {
	            System.out.println("No session found with the given details");
	            return "No session found with the given details";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Execution of session delete failed during statement preparation");
	    }
	    return "Execution of session delete failed during statement preparation";
	}
	

	
	public static String ClearSessions(int contactId)
	{
		String sql = "DELETE FROM sessions WHERE contact_id = ?";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, contactId);
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("all sessions cleared");
				return "all sessions cleared";
			}
			else
			{
				System.out.println("no session recorded");
				return "no session recorded";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of sessions clear failed during statement preparation");
		}
		return "Execution of sessions clear failed during statement preparation";
	}
	
	public static Boolean CreateContact(String profile_name, String password, String first_name, String last_name)
	{
		String sql = "INSERT INTO contacts (profile_name, password, first_name, last_name)" + " VALUES (?, ?, ?, ?)";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, profile_name);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, first_name);
			preparedStatement.setString(4, last_name);
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("new contact inserted");
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of contact creation failed during statement preparation");
		}
		
		return false;
	}
	
	public static User LogInUser(String profile_name, String password)
	{
		String sql = "SELECT id, profile_name FROM contacts WHERE profile_name = ? AND password = ?";
		User user = null;
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, profile_name);
			preparedStatement.setString(2, password);
			
			
			ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the query returned any result
            if (resultSet.next()) {
                // If result is found, create a User object and populate it
                int userId = resultSet.getInt("id");
                user = new User(userId, profile_name);
                System.out.println("Log in success, " + user.toString()); 
            }
            else
            {
            	System.out.println("Log in failed during query, User not found"); 
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Log In failed during statement preparation");
		}
		
		return user;
	}
	
	public static void UpdateProfileName(int userId, String newProfileName) {
	    String sql = "UPDATE contacts SET profile_name = ? WHERE id = ?";
	    try (Connection connection = ConnectionManager.getConnection()) {
	        PreparedStatement preparedStatement = connection.prepareStatement(sql);
	        preparedStatement.setString(1, newProfileName);  // New profile name
	        preparedStatement.setInt(2, userId);  // Contact ID to identify the row

	        int rows = preparedStatement.executeUpdate();
	        if (rows > 0) {
	            System.out.println("Profile name updated successfully");
	        } else {
	            System.out.println("Profile name update failed. It may already exist.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error updating profile name.");
	    }
	}
	
	public static void DeleteContact(String profile_name, String password)
	{
		User user = LogInUser(profile_name, password);
		
		ClearSessions(user.getId());
		
		String sql = "DELETE FROM contacts WHERE profile_name = ? AND password = ?";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, profile_name);
			preparedStatement.setString(2, password);
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("contact deleted");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of contact deletion failed during statement preparation");
		}
	}
	
	public static void testConnection()
	{
		String jdbcURL = "jdbc:postgresql://localhost:5432/studytracker";
		String username = "postgres";
		String password = "Postgresql2025";
		
		try (Connection connection = ConnectionManager.getConnection()) {
			System.out.println("Connection opened");
			
			/*
			String sql = "INSERT INTO contacts (profile_name, password)" + " VALUES ('Abyss', 'abyss123')";
			Statement statement = connection.createStatement();
			int rows = statement.executeUpdate(sql);
			
			if (rows > 0)
			{
				System.out.println("new contact inserted");
			}
			*/
			
			//CreateContact("Jane", "doe234", null, null);
			//DeleteContact("Jane", "doe234");
			User user = LogInUser("Jane", "doe234");
			LocalDate date = LocalDate.of(2025, 3, 5); // Current date
            LocalTime startTime = LocalTime.of(15, 30, 0); // 14:30:00
            LocalTime endTime = LocalTime.of(16, 30, 0);   // 15:30:00
            String topic = "Meeting";
            String comment = "Some guy changed the discussion";
            
			UpdateSession(user.getId(), new Session(date, startTime, endTime, topic, comment), null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection failed");
		}

	}
	public static void main(String[] args)
	{
		//testConnection();
	}
}