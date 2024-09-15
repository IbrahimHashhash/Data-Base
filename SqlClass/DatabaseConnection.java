package SqlClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/car_rent";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "tanimad2017";
    static Connection connection;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        }
        return connection;
    }
}
