package SqlClass;

import ERDClasses.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<Employee> employees = FXCollections.observableArrayList();
    public EmployeeDAO(Connection connection) {
        this.connection = connection;

    }

    public void insertEmployee(Employee employee, int managerId) {
        try {
            if (!employeeExists(managerId) &&  employee.getId() != managerId) {
                System.out.println("Employee with ID " + employee.getId() + " does not exist.");
                return; // Exit the method if the employee ID doesn't exist
            }

            String sql = "INSERT INTO Employee (emp_id, first_name, last_name, birth_date, age, phone, address, manager_id, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setDate(4, employee.getBirthDate());
            preparedStatement.setInt(5, employee.getAge());
            preparedStatement.setString(6, employee.getPhoneNumber());
            preparedStatement.setString(7, employee.getAddress());
            preparedStatement.setInt(8, managerId);
            preparedStatement.setString(9, String.valueOf(employee.getGender()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Employee updated successfully.");
                alert.showAndWait();
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insertEmployee(Employee employee) {
        try {
            if (!employeeExists(employee.getId())) {
                System.out.println("Employee with ID " + employee.getId() + " does not exist.");
                return; // Exit the method if the employee ID doesn't exist
            }

            String sql = "INSERT INTO Employee (emp_id, first_name, last_name, birth_date, age, phone, address, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setDate(4, employee.getBirthDate());
            preparedStatement.setInt(5, employee.getAge());
            preparedStatement.setString(6, employee.getPhoneNumber());
            preparedStatement.setString(7, employee.getAddress());
            preparedStatement.setString(8, String.valueOf(employee.getGender()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Employee inserted successfully.");
                alert.showAndWait();
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int empId) {
        try {
            // Check if the employee exists
            if (!employeeExists(empId)) {
                System.out.println("Employee with ID " + empId + " does not exist.");
                return;
            }

            // Check if the employee is a manager with associated employees
            String checkManagerSql = "SELECT COUNT(*) FROM Employee WHERE manager_id = ?";
            PreparedStatement checkManagerStatement = connection.prepareStatement(checkManagerSql);
            checkManagerStatement.setInt(1, empId);
            ResultSet managerResult = checkManagerStatement.executeQuery();

            if (managerResult.next()) {
                int count = managerResult.getInt(1);
                if (count > 1) {
                    System.out.println("Manager with ID " + empId + " cannot be deleted because they have associated employees.");
                    return;
                }
            }

            // Proceed with the deletion
            String sql = "DELETE FROM Employee WHERE emp_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Employee deleted successfully.");
                alert.showAndWait();

            } else {
                System.out.println("Employee with ID " + empId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    public void updateEmployee(Employee employee, int oldEmpId, int newManagerId) {
        try {
            if (!employeeExists(oldEmpId)) {
                System.out.println("Employee with ID " + oldEmpId + " does not exist.");
                return;
            }

            // Check if the new manager ID exists in the Employee table
            if (newManagerId != 0 && !employeeExists(newManagerId)) {
                System.out.println("New manager ID " + newManagerId + " does not exist.");
                return;
            }

            // Check if the new employee ID is already taken
            if (employeeExists(employee.getId())) {
                System.out.println("Employee ID " + employee.getId() + " is already taken.");
                return;
            }

            String sql;
            PreparedStatement preparedStatement;

            if (newManagerId != 0) {
                // Update manager ID as well
                sql = "UPDATE Employee SET first_name = ?, last_name = ?, birth_date = ?, age = ?, phone = ?, address = ?, gender = ?, manager_id = ? WHERE emp_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setDate(3, employee.getBirthDate());
                preparedStatement.setInt(4, employee.getAge());
                preparedStatement.setString(5, employee.getPhoneNumber());
                preparedStatement.setString(6, employee.getAddress());
                preparedStatement.setString(7, String.valueOf(employee.getGender()));
                preparedStatement.setInt(8, newManagerId); // Set new manager ID
                preparedStatement.setInt(9, oldEmpId); // Set old employee ID
            } else {
                // Only update employee information
                sql = "UPDATE Employee SET first_name = ?, last_name = ?, birth_date = ?, age = ?, phone = ?, address = ?, gender = ? WHERE emp_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setDate(3, employee.getBirthDate());
                preparedStatement.setInt(4, employee.getAge());
                preparedStatement.setString(5, employee.getPhoneNumber());
                preparedStatement.setString(6, employee.getAddress());
                preparedStatement.setString(7, String.valueOf(employee.getGender()));
                preparedStatement.setInt(8, oldEmpId); // Set old employee ID
            }

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Failed to update employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateEmployeeIncludeID(Employee employee, int oldEmpId, int newManagerId) {
        try {
            if (!employeeExists(oldEmpId)) {
                System.out.println("Employee with ID " + oldEmpId + " does not exist.");
                return;
            }

            // Check if the new manager ID exists in the Employee table
            if (newManagerId == 0 || !employeeExists(newManagerId)) {
                System.out.println("New manager ID " + newManagerId + " does not exist.");
                return;
            }

            // Check if the new employee ID is already taken
            if (employeeExists(employee.getId())) {
                System.out.println("Employee ID " + employee.getId() + " is already taken.");
                return;
            }

            // Delete the existing employee record
            deleteEmployee(oldEmpId);

            // Insert a new employee record with the updated ID
            insertEmployee(employee, newManagerId);

            System.out.println("Employee updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Employee> searchEmployees(String firstName, String lastName, String birthDate, int age, String phone, String address, String gender) {
        List<Employee> employees = new ArrayList<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Employee WHERE 1 = 1");
            List<Object> parameters = new ArrayList<>();

            if (firstName != null) {
                sqlBuilder.append(" AND first_name LIKE ?");
                parameters.add("%" + firstName + "%");
            }
            if (lastName != null) {
                sqlBuilder.append(" AND last_name LIKE ?");
                parameters.add("%" + lastName + "%");
            }
            if (birthDate != null) {
                sqlBuilder.append(" AND birth_date = ?");
                parameters.add(birthDate);
            }
            if (age > 0) {
                sqlBuilder.append(" AND age = ?");
                parameters.add(age);
            }
            if (phone != null) {
                sqlBuilder.append(" AND phone LIKE ?");
                parameters.add("%" + phone + "%");
            }
            if (address != null) {
                sqlBuilder.append(" AND address LIKE ?");
                parameters.add("%" + address + "%");
            }
            if (gender != null) {
                sqlBuilder.append(" AND gender = ?");
                parameters.add(gender);
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String firstNameResult = resultSet.getString("first_name");
                String lastNameResult = resultSet.getString("last_name");
                Date birthDateResult = resultSet.getDate("birth_date");
                int ageResult = resultSet.getInt("age");
                String phoneResult = resultSet.getString("phone");
                String addressResult = resultSet.getString("address");
                String genderResult = resultSet.getString("gender");
                char genderRes = genderResult.charAt(0);
                int managerId = resultSet.getInt("manager_id");
                Employee employee = new Employee(firstNameResult, lastNameResult, ageResult, addressResult, phoneResult,
                        birthDateResult, empId,genderRes);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return employees;
    }

    public boolean employeeExists(int empId) {
        try {
            String sql = "SELECT COUNT(*) FROM Employee WHERE emp_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1); // Retrieve the value of the first column
            return count > 0; // If count > 0, the employee exists; otherwise, it doesn't
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
            return false; // Return false in case of exceptions
        }
    }

    public boolean searchEmployeeById(int empId) {
        boolean found = false;
        try {
            String sql = "SELECT COUNT(*) FROM Employee WHERE emp_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                found = (count > 0); // Employee found if count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return found;
    }
    public Employee getEmployeeById(int empId) {
        Employee employee = null;
        try {
            String sql = "SELECT * FROM Employee WHERE emp_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char gen = gender.charAt(0);
                int managerId = resultSet.getInt("manager_id");
                employee = new Employee(firstName, lastName, age, address, phone, birthDate, empId, gen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return employee;
    }

    public List<Employee> searchEmployeesByName(String partialName) {
        List<Employee> employees = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Employee WHERE first_name LIKE ? OR last_name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialName + "%");
            preparedStatement.setString(2, "%" + partialName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                char gender = resultSet.getString("gender").charAt(0);
                Employee employee = new Employee(firstName, lastName, age, address, phone, birthDate, empId, gender);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return employees;
    }
    public static List<Employee> getAllEmployees() {
        List<Employee> allEmployees = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Employee";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char gen = gender.charAt(0);

                Employee employee = new Employee(firstName, lastName, age, address, phone, birthDate, empId, gen);
                allEmployees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return allEmployees;
    }
    public static int getTotalNumberOfEmployees() {
        String sql = "SELECT COUNT(*) AS totalEmployees FROM Employee";
        int totalEmployees = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                totalEmployees = resultSet.getInt("totalEmployees");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception here, you can log it or throw a custom exception
        }

        return totalEmployees;
    }
    public List<Employee> searchEmployeesByPartialId(int partialId) {
        List<Employee> foundEmployees = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Employee WHERE emp_id LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, partialId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char gen = gender.charAt(0);
                Employee employee = new Employee(firstName, lastName, age, address, phone, birthDate, empId, gen);
                foundEmployees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return foundEmployees;
    }
    public List<Employee> searchEmployeesByPartialName(String partialName) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE first_name LIKE ? OR last_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + partialName + "%");
            statement.setString(2, "%" + partialName + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("emp_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setBirthDate(resultSet.getDate("birth_date"));
                employee.setAge(resultSet.getInt("age"));
                employee.setPhoneNumber(resultSet.getString("phone"));
                employee.setAddress(resultSet.getString("address"));
                employee.setId(resultSet.getInt("manager_id"));
                employee.setGender(resultSet.getString("gender").charAt(0));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }


    public static boolean isEmployeeManager(int empId) {
        try {
            String sql = "SELECT COUNT(*) FROM Employee WHERE manager_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next(); // Move cursor to the first row
            int count = resultSet.getInt(1); // Retrieve the value of the first column
            return count > 0; // If count > 0, the employee is a manager; otherwise, they are not
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
            return false; // Return false in case of exceptions
        }
    }
    public static ObservableList<Employee> getEmployees() {
        return employees;
    }
    public void updateEmployee(Employee employee) {
        try {
            // Check if the employee exists
            if (!employeeExists(employee.getId())) {
                System.out.println("Employee with ID " + employee.getId() + " does not exist.");
                return;
            }

            // Construct the SQL query
            String sql = "UPDATE Employee SET first_name = ?, last_name = ?, birth_date = ?, age = ?, " +
                    "phone = ?, address = ?, gender = ? WHERE emp_id = ?";

            // Create a PreparedStatement with the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameters for the PreparedStatement
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setDate(3, employee.getBirthDate());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setString(6, employee.getAddress());
            preparedStatement.setString(7, String.valueOf(employee.getGender()));
            preparedStatement.setInt(8, employee.getId());

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Employee updated successfully.");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Failed to Update.");
                alert.showAndWait();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
