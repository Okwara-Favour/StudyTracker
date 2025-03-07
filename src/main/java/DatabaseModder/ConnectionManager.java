package DatabaseModder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static HikariDataSource dataSource;

    static {
    	try {
    	    Class.forName("org.postgresql.Driver");  // Ensure the driver is registered
    	} catch (ClassNotFoundException e) {
    	    throw new RuntimeException("PostgreSQL JDBC Driver not found!", e);
    	}
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/studytracker");
        config.setUsername("postgres");
        config.setPassword("Postgresql2025");
        config.setMaximumPoolSize(10);  // Adjust as needed
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}