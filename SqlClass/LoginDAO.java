package SqlClass;

import ERDClasses.Login;

import java.sql.*;

import static SqlClass.EmployeeDAO.isEmployeeManager;

public class LoginDAO {
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertLogin(Login login) {
        String sql = "INSERT INTO Login (emp_id, password, manager_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, login.getEmp_id());
            preparedStatement.setString(2, login.getPassword());

            // Check if the employee is a manager
            boolean isManager = isEmployeeManager(login.getEmp_id());
            if (isManager) {
                preparedStatement.setInt(3, login.getEmp_id()); // If manager, set manager_id to emp_id
            } else {
                preparedStatement.setNull(3, Types.INTEGER); // If not manager, set manager_id to NULL
            }

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Login inserted successfully.");
            } else {
                System.out.println("Failed to insert login.");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting login: " + e.getMessage());
        }
    }
    public void insertLogin(Login login, String password, boolean isManager) {
        String sql = "INSERT INTO Login (emp_id, password, manager_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, login.getEmp_id());
            preparedStatement.setString(2, password);

            // Set manager_id based on the value of isManager
            if (isManager) {
                preparedStatement.setInt(3, login.getEmp_id()); // If manager, set manager_id to emp_id
            } else {
                preparedStatement.setNull(3, Types.INTEGER); // If not manager, set manager_id to NULL
            }

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Login is correct.");
            } else {
                System.out.println("Failed to login.");
            }
        } catch (SQLException e) {
            System.out.println("Error something wrong: " + e.getMessage());
        }
    }

    public boolean selectLogin(int empId, String password) {
        String sql = "SELECT emp_id FROM Login WHERE emp_id = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If the login exists, check if the employee is a manager
                    return isEmployeeManager(empId);
                } else {
                    System.out.println("Login not found for emp id: " + empId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in login: " + e.getMessage());
            return false;
        }
    }
    public boolean updatePassword(int empId, String newPassword) {
        String sql = "UPDATE Login SET password = ? WHERE emp_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, empId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password updated successfully for emp id: " + empId);
                return true;
            } else {
                System.out.println("No login found for emp id: " + empId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }
    public boolean passwordExists(int empId, String password) {
        String sql = "SELECT * FROM Login WHERE emp_id = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Return true if a matching login is found, indicating the password exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking password existence: " + e.getMessage());
        }
        return false; // Return false if an exception occurs or if the password doesn't exist for the given employee ID
    }
    public static boolean isManagerWithLogin(int empId, String password) {
        String sql = "SELECT emp_id FROM Login WHERE emp_id = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If the login exists, check if the employee is a manager
                    return isEmployeeManager(empId);
                } else {
                    System.out.println("Login not found for emp id: " + empId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in login: " + e.getMessage());
            return false;
        }
    }

}
