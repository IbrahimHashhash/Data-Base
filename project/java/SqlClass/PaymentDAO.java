package SqlClass;

import ERDClasses.Customer;
import ERDClasses.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    static ObservableList<Payment> payments = FXCollections.observableArrayList();
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertPayment(Payment payment) {
        // Check if the payment ID already exists
        if (paymentExists(payment.getPaymentID())) {
            System.out.println("Payment with ID " + payment.getPaymentID() + " already exists.");
            return;
        }

        // Check if the customer ID exists
        if (!customerExists(payment.getCustomerId())) {
            System.out.println("Customer with ID " + payment.getCustomerId() + " does not exist.");
            return;
        }

        // Check if the rental contract ID exists
        if (!rentalContractExists(payment.getRental_Id())) {
            System.out.println("Rental contract with ID " + payment.getRental_Id() + " does not exist.");
            return;
        }

        String sql = "INSERT INTO Payment (payment_id, amount, pay_date, down_payment, total_cost, payment_method, cus_Id, rent_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, payment.getPaymentID());
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setDate(3, payment.getPaydate());
            preparedStatement.setDouble(4, payment.getDownPay());
            preparedStatement.setDouble(5, payment.getAmount());
            preparedStatement.setString(6, payment.getPaymentMethod());
            preparedStatement.setInt(7, payment.getCustomerId());
            preparedStatement.setInt(8, payment.getRental_Id()); // Set rent ID

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                payments.add(payment);
                alert.setContentText("Payment added successfully.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    // Delete method for removing a payment record based on the payment ID
    public void deletePayment(int paymentId) {
        // Check if the payment ID exists before deletion
        if (!paymentExists(paymentId)) {
            System.out.println("Payment with payment ID " + paymentId + " does not exist.");
            return;
        }
        Payment payment = getPaymentById(paymentId);

        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Payment added successfully.");
                alert.showAndWait();
                payments.remove(payment);

            } else {
                System.out.println("Payment with payment ID " + paymentId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }
    public Payment getPaymentById(int paymentId) {
        // Initialize the payment object
        Payment payment = null;
        String sql = "SELECT * FROM Payment WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Retrieve payment attributes from the ResultSet
                double amount = resultSet.getDouble("amount");
                double downPay = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                Date paydate = resultSet.getDate("pay_date");
                int customerId = resultSet.getInt("cus_Id");
                int rentalId = resultSet.getInt("rent_id");

                // Create a new Payment object with retrieved attributes
                payment = new Payment(amount, downPay, totalCost, paymentMethod, paydate, customerId, rentalId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return payment;
    }


    // Method to check if a payment exists based on the payment ID
    private boolean paymentExists(int paymentId) {
        String sql = "SELECT COUNT(*) FROM Payment WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return false;
    }

    public void updatePayment(Payment payment, int oldPaymentId) {
        // Check if the old payment ID exists before attempting to update
        if (!paymentExists(oldPaymentId)) {
            System.out.println("Payment with ID " + oldPaymentId + " does not exist.");
            return;
        }

        // Check if the customer ID exists
        if (!customerExists(payment.getCustomerId())) {
            System.out.println("Customer with ID " + payment.getCustomerId() + " does not exist.");
            return;
        }

        // Check if the rental contract ID exists
        if (!rentalContractExists(payment.getRental_Id())) {
            System.out.println("Rental contract with ID " + payment.getRental_Id() + " does not exist.");
            return;
        }

        String sql = "UPDATE Payment SET payment_id = ?, amount = ?, pay_date = ?, down_payment = ?, total_cost = ?, payment_method = ?, cus_Id = ?, rent_id = ? "
                + "WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, payment.getPaymentID());
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setDate(3, payment.getPaydate());
            preparedStatement.setDouble(4, payment.getDownPay());
            preparedStatement.setDouble(5, payment.getAmount());
            preparedStatement.setString(6, payment.getPaymentMethod());
            preparedStatement.setInt(7, payment.getCustomerId());
            preparedStatement.setInt(8, payment.getRental_Id()); // Set rent ID
            preparedStatement.setInt(9, oldPaymentId); // Set old payment ID

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Payment Updated successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to update payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    public List<Payment> searchPayments(String paymentMethod, BigDecimal minAmount, BigDecimal maxAmount,
                                        LocalDate minPayDate, LocalDate maxPayDate, BigDecimal minDownPayment, BigDecimal maxDownPayment,
                                        BigDecimal minTotalCost, BigDecimal maxTotalCost) {
        List<Payment> payments = new ArrayList<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Payment WHERE 1 = 1");
            List<Object> parameters = new ArrayList<>();

            if (paymentMethod != null) {
                sqlBuilder.append(" AND payment_method = ?");
                parameters.add(paymentMethod);
            }
            if (minAmount != null) {
                sqlBuilder.append(" AND amount >= ?");
                parameters.add(minAmount);
            }
            if (maxAmount != null) {
                sqlBuilder.append(" AND amount <= ?");
                parameters.add(maxAmount);
            }
            if (minPayDate != null) {
                sqlBuilder.append(" AND pay_date >= ?");
                parameters.add(Date.valueOf(minPayDate));
            }
            if (maxPayDate != null) {
                sqlBuilder.append(" AND pay_date <= ?");
                parameters.add(Date.valueOf(maxPayDate));
            }
            if (minDownPayment != null) {
                sqlBuilder.append(" AND down_payment >= ?");
                parameters.add(minDownPayment);
            }
            if (maxDownPayment != null) {
                sqlBuilder.append(" AND down_payment <= ?");
                parameters.add(maxDownPayment);
            }
            if (minTotalCost != null) {
                sqlBuilder.append(" AND total_cost >= ?");
                parameters.add(minTotalCost);
            }
            if (maxTotalCost != null) {
                sqlBuilder.append(" AND total_cost <= ?");
                parameters.add(maxTotalCost);
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int paymentId = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                java.sql.Date payDate = resultSet.getDate("pay_date");
                double downPayment = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethodResult = resultSet.getString("payment_method");
                int customerId = resultSet.getInt("cus_Id");
                int rentId = resultSet.getInt("rent_id"); // Retrieve rent ID

                Payment payment = new Payment( amount,downPayment,totalCost,paymentMethodResult,payDate,
                        customerId, rentId); // Pass rent ID to constructor
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
// Handle exceptions accordingly
        }
        return payments;
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
    public boolean rentalContractExists(int rentId) {
        try {
            String sql = "SELECT COUNT(*) FROM Rental_Contract WHERE rent_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, rentId);
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
    public boolean isCarAvailable(int carNo, LocalDate startDate, LocalDate endDate) {
        // First, check if the car exists in any rental contract
        String checkCarExistsSql = "SELECT COUNT(*) AS carCount " +
                "FROM Rental_Contract " +
                "WHERE JSON_EXTRACT(car_details, '$.car_No') = ?";

        try (PreparedStatement checkCarExistsStmt = connection.prepareStatement(checkCarExistsSql)) {
            checkCarExistsStmt.setInt(1, carNo);
            ResultSet carExistsResult = checkCarExistsStmt.executeQuery();

            if (carExistsResult.next() && carExistsResult.getInt("carCount") == 0) {
                // Car does not exist in any rental contract, so it's available
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
            return false;
        }

        // If car exists, check for overlapping rental periods
        String checkAvailabilitySql = "SELECT COUNT(*) AS overlapCount " +
                "FROM Rental_Contract " +
                "WHERE JSON_EXTRACT(car_details, '$.car_No') = ? " +
                "AND (rent_date <= ? AND return_date >= ?)";

        try (PreparedStatement checkAvailabilityStmt = connection.prepareStatement(checkAvailabilitySql)) {
            checkAvailabilityStmt.setInt(1, carNo);
            checkAvailabilityStmt.setDate(2, Date.valueOf(endDate));   // Check endDate first
            checkAvailabilityStmt.setDate(3, Date.valueOf(startDate)); // Check startDate second

            ResultSet availabilityResult = checkAvailabilityStmt.executeQuery();
            if (availabilityResult.next()) {
                int overlapCount = availabilityResult.getInt("overlapCount");
                return overlapCount == 0; // Car is available if no overlapping rentals
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return false; // Return false if an error occurs
    }

    public static ObservableList<Payment> getPayments() {
        return payments;
    }
    public static List<Payment> getAllPayments() {
        List<Payment> allPayments = new ArrayList<>();

        String sql = "SELECT * FROM Payment";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve payment attributes from the ResultSet
                int paymentId = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                double downPay = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                Date paydate = resultSet.getDate("pay_date");
                int customerId = resultSet.getInt("cus_Id");
                int rentalId = resultSet.getInt("rent_id");

                // Create a new Payment object with retrieved attributes
                Payment payment = new Payment(amount, downPay, totalCost, paymentMethod, paydate, customerId, rentalId);
                allPayments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return allPayments;
    }
    public List<Integer> getCustomersWithLoan() {
        List<Integer> customersWithNonZeroDownPaymentAndUnequalTotal = new ArrayList<>();
        String sql = "SELECT DISTINCT cus_Id FROM Payment WHERE down_payment <> 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int customerId = resultSet.getInt("cus_Id");
                // Check if the total payment is not equal to the down payment
                if (!isTotalEqualToDownPayment(customerId)) {
                    customersWithNonZeroDownPaymentAndUnequalTotal.add(customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customersWithNonZeroDownPaymentAndUnequalTotal;
    }

    private boolean isTotalEqualToDownPayment(int customerId) {
        String totalSql = "SELECT SUM(amount) AS total FROM Payment WHERE cus_Id = ?";
        String downPaymentSql = "SELECT down_payment FROM Payment WHERE cus_Id = ?";

        try (PreparedStatement totalStmt = connection.prepareStatement(totalSql);
             PreparedStatement downPaymentStmt = connection.prepareStatement(downPaymentSql)) {
            totalStmt.setInt(1, customerId);
            downPaymentStmt.setInt(1, customerId);

            ResultSet totalResult = totalStmt.executeQuery();
            ResultSet downPaymentResult = downPaymentStmt.executeQuery();

            if (totalResult.next() && downPaymentResult.next()) {
                double total = totalResult.getDouble("total");
                double downPayment = downPaymentResult.getDouble("down_payment");
                return total == downPayment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return false;
    }
    public List<Payment> getAllPaymentsWithDownPayment() {
        List<Payment> paymentsWithDownPayment = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE down_payment <> 0 AND down_payment != total_cost";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve payment attributes from the ResultSet
                int paymentId = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                double downPay = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                Date paydate = resultSet.getDate("pay_date");
                int customerId = resultSet.getInt("cus_Id");
                int rentalId = resultSet.getInt("rent_id");

                // Create a new Payment object with retrieved attributes
                Payment payment = new Payment(amount, downPay, totalCost, paymentMethod, paydate, customerId, rentalId);
                paymentsWithDownPayment.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return paymentsWithDownPayment;
    }
    public int getCustomerIdByPaymentId(int paymentId) {
        int customerId = -1; // Default value if not found
        String sql = "SELECT cus_Id FROM Payment WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customerId = resultSet.getInt("cus_Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customerId;
    }
    public int getRentalIdByPaymentId(int paymentId) {
        int rentalId = -1; // Default value if not found
        String sql = "SELECT rent_id FROM Payment WHERE payment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rentalId = resultSet.getInt("rent_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return rentalId;
    }
    public int getCarIdByPaymentId(int paymentId) {
        int carId = -1; // Default value if not found
        String sql = "SELECT car_No FROM Rental_Contract WHERE rent_id = (SELECT rent_id FROM Payment WHERE payment_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, paymentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                carId = resultSet.getInt("car_No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return carId;
    }
    public List<Payment> searchPaymentsByAmount(BigDecimal amount) {
        List<Payment> paymentsByAmount = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE amount = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, amount);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = extractPaymentFromResultSet(resultSet);
                paymentsByAmount.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return paymentsByAmount;
    }
    public List<Payment> searchPaymentsByPartialType(String paymentMethod) {
        List<Payment> paymentsByType = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE payment_method LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + paymentMethod + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = extractPaymentFromResultSet(resultSet);
                paymentsByType.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return paymentsByType;
    }

    public List<Payment> searchPaymentsByPartialId(int partialId) {
        List<Payment> matchingPayments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE payment_id LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Create a pattern to match the partialId at the beginning or end of the integer value
            String pattern = "%" + partialId + "%";

            preparedStatement.setInt(1, partialId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Extract payment details and create Payment object
                int paymentID = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                Date payDate = resultSet.getDate("pay_date");
                double downPayment = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                int customerId = resultSet.getInt("cus_Id");
                int rentalId = resultSet.getInt("rent_id");

                Payment payment = new Payment(amount, downPayment, totalCost, paymentMethod, payDate, customerId, rentalId);
                payment.setPaymentID(paymentID); // Setting the paymentID which is generated from the database.
                matchingPayments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly, e.g., log or throw further
        }

        return matchingPayments;
    }
    private Payment extractPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        int paymentId = resultSet.getInt("payment_id");
        double amount = resultSet.getDouble("amount");
        double downPay = resultSet.getDouble("down_payment");
        double totalCost = resultSet.getDouble("total_cost");
        String paymentMethod = resultSet.getString("payment_method");
        Date paydate = resultSet.getDate("pay_date");
        int customerId = resultSet.getInt("cus_Id");
        int rentalId = resultSet.getInt("rent_id");

        return new Payment(amount, downPay, totalCost, paymentMethod, paydate, customerId, rentalId);
    }
    public List<Customer> getCustomersByPaymentAmount() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* " +
                "FROM Payment p " +
                "JOIN Customer c ON p.cus_Id = c.cus_Id " +
                "WHERE p.amount < p.total_cost OR p.total_cost = 0";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int customerId = resultSet.getInt("cus_Id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthDate = resultSet.getDate("birth_date");
                String drivingLicense = resultSet.getString("driving_license");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");

                // Create Customer object and add it to the list
                Customer customer = new Customer(firstName, lastName, age, address, phone, birthDate, drivingLicense, customerId, gender.charAt(0));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customers;
    }

}



