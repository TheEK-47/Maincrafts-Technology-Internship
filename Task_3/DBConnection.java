/**
 * DBConnection.java
 * Manages the JDBC connection to the MySQL database.
 * Centralises connection logic so it is written once and reused everywhere.
 *
 * Author      : Anuska Roy
 * Project     : JDBC-Based Student Management System – Task 3
 * Organisation: Maincrafts Technology
 *
 * JDBC URL format:
 *   jdbc:mysql://<host>:<port>/<database>?useSSL=false&allowPublicKeyRetrieval=true
 *
 *
 * Concepts demonstrated:
 *   - DriverManager.getConnection()
 *   - Connection object (java.sql.Connection)
 *   - Exception handling for database connectivity
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/student_db"
                                            + "?useSSL=false&allowPublicKeyRetrieval=true"
                                            + "&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "password";   

    
    // getConnection()
    // Returns a live Connection object, or null on failure.
    // Caller is responsible for closing the connection.
    
    public static Connection getConnection() {
        try {
            // From JDBC 4.0 the driver loads automatically via Service Provider.
            // Explicit Class.forName() kept for JDK 8 compatibility & clarity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("[!] JDBC Driver not found. Add mysql-connector-j.jar to classpath.");
            System.out.println("    Download: https://dev.mysql.com/downloads/connector/j/");
        } catch (SQLException e) {
            System.out.println("[!] Database connection failed: " + e.getMessage());
            System.out.println("    Check: MySQL running | credentials correct | student_db exists");
        }
        return null;
    }

    
    // testConnection()
    // Quick connectivity check called on application startup.
    
    public static boolean testConnection() {
        Connection con = getConnection();
        if (con != null) {
            try { con.close(); } catch (SQLException ignored) {}
            return true;
        }
        return false;
    }
}
