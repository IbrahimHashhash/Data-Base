package SqlClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ERDClasses.Customer;
import ERDClasses.Employee;
import ERDClasses.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;

public class CustomerDAO {
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }


    public void insertCustomer(Customer customer, Employee currentEmployee) {
        try {
            String sql = "INSERT INTO Customer (cus_Id, emp_id, first_name, last_name, birth_date, driving_license, age, phone, address, gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setInt(2, currentEmployee.getId());
            preparedStatement.setString(3, customer.getFirstName());
            preparedStatement.setString(4, customer.getLastName());
            preparedStatement.setDate(5, customer.getBirthDate());
            preparedStatement.setString(6, customer.getLicenseNum());
            preparedStatement.setInt(7, customer.getAge());
            preparedStatement.setString(8, customer.getPhoneNumber());
            preparedStatement.setString(9, customer.getAddress());
            preparedStatement.setString(10, String.valueOf(customer.getGender()));
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer Inserted successfully.");
                customers.add(customer);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    public boolean deleteCustomer(int customerId) {
        try {
            String sql = "DELETE FROM Customer WHERE cus_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer deleted successfully.");
                alert.showAndWait();

                return true;
            } else {
                System.out.println("Customer with ID " + customerId + " not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Deletion failed due to exception
        }
    }

    public void updateCustomer(Customer customer, int oldCusId) {
        try {
            // Check if the new primary key already exists
            if (customerExists(customer.getId()) && oldCusId!=customer.getId()) {
                System.out.println("Customer with ID " +customer.getId() + " already exists.");
                return;
            }

            String sql = "UPDATE Customer SET cus_Id = ?, first_name = ?, last_name = ?, birth_date = ?, " +
                    "driving_license = ?, age = ?, phone = ?, address = ?, gender = ? " +
                    "WHERE cus_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customer.getId()); // Update the primary key
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setDate(4, customer.getBirthDate());
            preparedStatement.setString(5, customer.getLicenseNum());
            preparedStatement.setInt(6, customer.getAge());
            preparedStatement.setString(7, customer.getPhoneNumber());
            preparedStatement.setString(8, customer.getAddress());
            preparedStatement.setString(9, String.valueOf(customer.getGender()));
            preparedStatement.setInt(10, oldCusId); // Old primary key value

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Customer with ID " + customer.getId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean customerExists(int cusId) {
        try {
            String sql = "SELECT COUNT(*) FROM Customer WHERE cus_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cusId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public List<Customer> searchCustomers(String firstName, String lastName, String birthDate, String drivingLicense, int age, String phone, String address, String gender) {
        List<Customer> customers = new ArrayList<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Customer WHERE 1 = 1");
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
            if (drivingLicense != null) {
                sqlBuilder.append(" AND driving_license = ?");
                parameters.add(drivingLicense);
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
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstNameResult = resultSet.getString("first_name");
                String lastNameResult = resultSet.getString("last_name");
                Date birthDateResult = resultSet.getDate("birth_date");
                String drivingLicenseResult = resultSet.getString("driving_license");
                int ageResult = resultSet.getInt("age");
                String phoneResult = resultSet.getString("phone");
                String addressResult = resultSet.getString("address");
                String genderResult = resultSet.getString("gender");
                char genderRes = genderResult.charAt(0);
                Customer customer = new Customer( firstNameResult,  lastNameResult,  age, address,  phoneResult,
                birthDateResult,  drivingLicenseResult,  cusId, genderRes);

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customers;
    }
    public static List<Customer> getCustomersWithPayments() {
        List<Customer> customersWithPayments = new ArrayList<>();

        try {
            String sql = "SELECT DISTINCT c.* " +
                    "FROM Customer c " +
                    "INNER JOIN Payment p ON c.cus_Id = p.cus_Id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char genderRes = gender.charAt(0);

                Customer customer = new Customer(firstName, lastName, age, address, phone, birthDate, drivingLicense, cusId, genderRes);
                customersWithPayments.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return customersWithPayments;
    }

    public static Customer searchCustomerById(int cusId) {
        Customer customer = null;
        try {
            String sql = "SELECT * FROM Customer WHERE cus_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cusId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char genderRes = gender.charAt(0);
                 customer = new Customer( firstName,  lastName,  age, address,phone,
                        birthDate,  drivingLicense,  cusId, genderRes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customer;
    }
    public List<Customer> searchCustomersByName(String partialName) {
        List<Customer> customers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Customer WHERE first_name LIKE ? OR last_name LIKE ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialName + "%");
            preparedStatement.setString(2, "%" + partialName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstNameResult = resultSet.getString("first_name");
                String lastNameResult = resultSet.getString("last_name");
                Date birthDateResult = resultSet.getDate("birth_date");
                String drivingLicenseResult = resultSet.getString("driving_license");
                int ageResult = resultSet.getInt("age");
                String phoneResult = resultSet.getString("phone");
                String addressResult = resultSet.getString("address");
                String genderResult = resultSet.getString("gender");
                char genderRes = genderResult.charAt(0);
                Customer customer = new Customer(firstNameResult, lastNameResult, ageResult, addressResult, phoneResult,
                        birthDateResult, drivingLicenseResult, cusId, genderRes);

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customers;
    }
    public static List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Customer";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char genderRes = gender.charAt(0);

                Customer customer = new Customer(firstName, lastName, age, address, phone, birthDate, drivingLicense, cusId, genderRes);
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return allCustomers;
    }
    public static int getTotalNumberOfCustomers() {
        int totalNumberOfCustomers = 0;
        String sql = "SELECT COUNT(*) AS totalCustomersCount FROM Customer";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalNumberOfCustomers = resultSet.getInt("totalCustomersCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return totalNumberOfCustomers;
    }
    public List<Customer> searchCustomersByPartialId(String partialId) {
        List<Customer> customers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Customer WHERE cus_Id LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialId + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstNameResult = resultSet.getString("first_name");
                String lastNameResult = resultSet.getString("last_name");
                Date birthDateResult = resultSet.getDate("birth_date");
                String drivingLicenseResult = resultSet.getString("driving_license");
                int ageResult = resultSet.getInt("age");
                String phoneResult = resultSet.getString("phone");
                String addressResult = resultSet.getString("address");
                String genderResult = resultSet.getString("gender");
                char genderRes = genderResult.charAt(0);
                Customer customer = new Customer(firstNameResult, lastNameResult, ageResult, addressResult, phoneResult,
                        birthDateResult, drivingLicenseResult, cusId, genderRes);

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customers;
    }
    public List<Customer> searchCustomersByPartialName(String partialName) {
        List<Customer> foundCustomers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Customer WHERE first_name LIKE ? OR last_name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialName + "%"); // Searching for partial first name
            preparedStatement.setString(2, "%" + partialName + "%"); // Searching for partial last name
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("cus_Id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone");
                Date birthDate = resultSet.getDate("birth_date");
                String licenseNum = resultSet.getString("driving_license");
                char gender = resultSet.getString("gender").charAt(0);

                Customer customer = new Customer(firstName, lastName, age, address, phoneNumber, birthDate, licenseNum, id, gender);
                foundCustomers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return foundCustomers;
    }
    public static Payment getCustomerPayment(int customerId) {
        Payment payment = null;

        try {
            String sql = "SELECT * FROM Payment WHERE cus_Id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int paymentId = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                double downPayment = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                Date payDate = resultSet.getDate("pay_date");
                int rentId = resultSet.getInt("rent_id");

                payment = new Payment(amount, downPayment, totalCost, paymentMethod, payDate, customerId, rentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return payment;
    }
    public List<Customer> searchCustomersByPartialIdWithPayments(String partialId) {
        List<Customer> customersWithPayments = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.* " +
                    "FROM Customer c " +
                    "INNER JOIN Payment p ON c.cus_Id = p.cus_Id " +
                    "WHERE c.cus_Id LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialId + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char genderRes = gender.charAt(0);

                Customer customer = new Customer(firstName, lastName, age, address, phone, birthDate, drivingLicense, cusId, genderRes);
                customersWithPayments.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customersWithPayments;
    }
    public List<Customer> searchCustomersByPartialNameWithPayments(String partialName) {
        List<Customer> customersWithPayments = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.* " +
                    "FROM Customer c " +
                    "INNER JOIN Payment p ON c.cus_Id = p.cus_Id " +
                    "WHERE c.first_name LIKE ? OR c.last_name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialName + "%");
            preparedStatement.setString(2, "%" + partialName + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int cusId = resultSet.getInt("cus_Id");
                int empId = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                char genderRes = gender.charAt(0);

                Customer customer = new Customer(firstName, lastName, age, address, phone, birthDate, drivingLicense, cusId, genderRes);
                customersWithPayments.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customersWithPayments;
    }



    public static ObservableList<Customer> getCustomers() {
        return customers;
    }
}