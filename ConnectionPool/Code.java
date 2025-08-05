package ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Code {
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db";
    private static final String USER = "root";
    private static final String PASS = "password";
    
    public static void main(String[] args) {
        Code code = new Code();
        code.makeDatabaseCalls();
    }
    
    // Static block to load the MySQL driver explicitly
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL JDBC Driver: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void makeDatabaseCalls() {
        System.out.println("Starting 500 database calls with naive implementation...");
        
        for (int i = 1; i <= 500; i++) {
            try {
                // Create a new connection for each call (naive approach)
                Connection connection = createConnection();
                
                // Execute a simple query
                executeQuery(connection, i);
                
                // Close the connection
                connection.close();
                
                if (i % 50 == 0) {
                    System.out.println("Completed " + i + " database calls");
                }
                
            } catch (SQLException e) {
                System.err.println("Error in call " + i + ": " + e.getMessage());
            }
        }
        
        System.out.println("Completed all 500 database calls!");
    }
    
    private Connection createConnection() throws SQLException {
        // Create a new connection each time (naive approach)
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    
    private void executeQuery(Connection connection, int callNumber) throws SQLException {
        // Simple query to test database connection
        String sql = "SELECT 1 as result, ? as call_number";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, callNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int result = rs.getInt("result");
                    int callNum = rs.getInt("call_number");
                    
                    // Uncomment the line below if you want to see each result
                    // System.out.println("Call " + callNum + " returned: " + result);
                }
            }
        }
    }
}
