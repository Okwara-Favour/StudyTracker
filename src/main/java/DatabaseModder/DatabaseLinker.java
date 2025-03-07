package DatabaseModder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import com.zaxxer.hikari.HikariDataSource;

import DatabaseModder.User;

public class DatabaseLinker
{
	public static void CreateSession(int contactId, Session session)
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
	}
	
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
	}
	
	public static void ClearSessions(int contactId)
	{
		String sql = "DELETE FROM sessions WHERE contact_id = ?";
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, contactId);
			
			int rows = preparedStatement.executeUpdate();
			if (rows > 0)
			{
				System.out.println("all sessions cleared");
			}
			else
			{
				System.out.println("no session recorded");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Execution of sessions clear failed during statement preparation");
		}
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
            
			UpdateSession(user.getId(), new Session(date, startTime, endTime, topic, comment));
			
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