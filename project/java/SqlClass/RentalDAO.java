package SqlClass;

import ERDClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<Rental> rentals = FXCollections.observableArrayList();

    public RentalDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertRentalContract(Rental rental) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(rentalContractExists(rental.getRentID())){
            return;
        }
        try {
            // Insert into Rental_Contract table
            String query = "INSERT INTO Rental_Contract (rent_id, rent_date, return_date, cancellation_details, emp_id, car_No, cus_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rental.getRentID());
            preparedStatement.setDate(2, rental.getRentDate());
            preparedStatement.setDate(3, rental.getReturnDate());
            preparedStatement.setString(4, rental.getCancellation());
            preparedStatement.setInt(5, rental.getEmp_id());
            preparedStatement.setInt(6, rental.getCar_num());
            preparedStatement.setInt(7, rental.getCus_ID());
            preparedStatement.executeUpdate();

            // Insert into subclass table based on rental type
            if (rental instanceof DailyRental) {
                DailyRental dailyRental = (DailyRental) rental;
                query = "INSERT INTO Daily_Rental (rent_id, no_of_days) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, dailyRental.getRentID());
                preparedStatement.setInt(2, dailyRental.getNoOfDays());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Rental Inserted successfully.");
                alert.showAndWait();
                rentals.add(rental);
            } else if (rental instanceof WeeklyRental) {
                WeeklyRental weeklyRental = (WeeklyRental) rental;
                query = "INSERT INTO Weekly_Rental (rent_id, no_of_weeks) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, weeklyRental.getRentID());
                preparedStatement.setInt(2, weeklyRental.getNoOfWeeks());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Rental Inserted successfully.");
                alert.showAndWait();
                rentals.add(rental);
            } else {
                throw new IllegalArgumentException("Unsupported rental type");
            }
            preparedStatement.executeUpdate();
            System.out.println("Rental inserted successfully.");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public boolean rentalContractExists(int rentId) {
        String sql = "SELECT COUNT(*) FROM Rental_Contract WHERE rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Return true if count is greater than 0, indicating the rental contract exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return false; // Return false if an exception occurs or if no rental contract exists
    }

    public void deleteRentalContract(int rentId) {
        // Check if the rental contract exists before attempting to delete
        if (!rentalContractExists(rentId)) {
            System.out.println("Rental contract with ID " + rentId + " does not exist.");
            return;
        }

        // Delete the rental contract from the Rental_Contract table
        String sql = "DELETE FROM Rental_Contract WHERE rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Rental Deleted successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to delete rental contract.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

    }

    // Update method

    // Search method

    // Other methods

  public static List<Rental> getAllRentals() {
        List<Rental> allRentals = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Rental_Contract";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int rentId = resultSet.getInt("rent_id");
                java.sql.Date rentDate = resultSet.getDate("rent_date");
                java.sql.Date returnDate = resultSet.getDate("return_date");
                String cancellationDetails = resultSet.getString("cancellation_details");
                int carNum = resultSet.getInt("car_No");
                int empId = resultSet.getInt("emp_id");
                int cusId = resultSet.getInt("cus_id");

                Rental rentalContract = new Rental(returnDate, rentDate, rentId, cancellationDetails, carNum, empId, cusId);
                allRentals.add(rentalContract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return allRentals;
    }

   

    public void updateRentalContract(Rental contract) {
        // Check if the rental contract exists before attempting to update
        if (!rentalContractExists(contract.getRentID())) {
            System.out.println("Rental contract with ID " + contract.getRentID() + " does not exist.");
            return;
        }

        String updateRentalContractSQL = "UPDATE Rental_Contract SET rent_date = ?, return_date = ?, cancellation_details = ?, car_No = ?, emp_id = ?, cus_id = ? WHERE rent_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateRentalContractSQL)) {
            updateStatement.setDate(1, contract.getRentDate());
            updateStatement.setDate(2, contract.getReturnDate());
            updateStatement.setString(3, contract.getCancellation());
            updateStatement.setInt(4, contract.getCar_num());
            updateStatement.setInt(5, contract.getEmp_id());
            updateStatement.setInt(6, contract.getCus_ID());
            updateStatement.setInt(7, contract.getRentID());

            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Rental contract updated successfully.");
            } else {
                System.out.println("Failed to update rental contract.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }
    public Customer selectCustomerByRentId(int rentId) {
        Customer customer = null;
        String sql = "SELECT c.* FROM Customer c INNER JOIN Rental_Contract r ON c.cus_Id = r.cus_id WHERE r.rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Assuming you have a constructor in Customer class to create objects from ResultSet
                char gen = resultSet.getString("gender").charAt(0);

                customer = new Customer(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"),
                        resultSet.getDate("birth_date"),
                        resultSet.getString("driving_license"),
                        resultSet.getInt("cus_Id"),
                        gen
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return customer;
    }


    public static Rental returnRentByRentId(int rentId) {
        Rental rental = null;
        String sql = "SELECT * FROM Rental_Contract WHERE rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Assuming you have a constructor in Rental class to create objects from ResultSet
                rental = new Rental(
                        resultSet.getDate("rent_date"),
                        resultSet.getDate("return_date"),
                        resultSet.getInt("rent_id"),
                        resultSet.getString("cancellation_details"),
                        resultSet.getInt("car_No"),
                        resultSet.getInt("emp_id"),
                        resultSet.getInt("cus_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return rental;
    }

    public Car selectCarByRentId(int rentId) {
        Car car = null;
        String sql = "SELECT c.* FROM Car c INNER JOIN Rental_Contract r ON c.car_No = r.car_No WHERE r.rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                car = new Car(
                        resultSet.getString("car_color"),
                        resultSet.getString("model"),
                        resultSet.getString("physical_condition"),
                        resultSet.getInt("car_No"),
                        resultSet.getDouble("rental_price"),
                        resultSet.getDouble("mileage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return car;
    }
    public List<Rental> searchRentalsByPartialId(int partialId) {
        List<Rental> foundRentals = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Rental_Contract WHERE rent_id LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + partialId + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int rentId = resultSet.getInt("rent_id");
                java.sql.Date rentDate = resultSet.getDate("rent_date");
                java.sql.Date returnDate = resultSet.getDate("return_date");
                String cancellationDetails = resultSet.getString("cancellation_details");
                int carNum = resultSet.getInt("car_No");
                int empId = resultSet.getInt("emp_id");
                int cusId = resultSet.getInt("cus_id");

                Rental rentalContract = new Rental(returnDate, rentDate, rentId, cancellationDetails, carNum, empId, cusId);
                foundRentals.add(rentalContract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return foundRentals;
    }
    public static DailyRental selectDailyRentalById(int rentId) {
        DailyRental dailyRental = null;
        String sql = "SELECT * FROM Daily_Rental WHERE rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int noOfDays = resultSet.getInt("no_of_days");
                // Retrieve common rental fields
                Rental rental = returnRentByRentId(rentId);
                // Create DailyRental object
                dailyRental = new DailyRental(rental.getReturnDate(), rental.getRentDate(), rentId, rental.getCancellation(), rental.getCar_num(), rental.getEmp_id(), rental.getCus_ID(), noOfDays);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return dailyRental;
    }
    public static WeeklyRental selectWeeklyRentalById(int rentId) {
        WeeklyRental weeklyRental = null;
        String sql = "SELECT * FROM Weekly_Rental WHERE rent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int noOfWeeks = resultSet.getInt("no_of_weeks");
                // Retrieve common rental fields
                Rental rental = returnRentByRentId(rentId);
                // Create WeeklyRental object
                weeklyRental = new WeeklyRental(rental.getReturnDate(), rental.getRentDate(), rentId, rental.getCancellation(), rental.getCar_num(), rental.getEmp_id(), rental.getCus_ID(), noOfWeeks);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return weeklyRental;
    }
    public static int checkRentalType(int rentId) {
        String dailySql = "SELECT COUNT(*) FROM Daily_Rental WHERE rent_id = ?";
        String weeklySql = "SELECT COUNT(*) FROM Weekly_Rental WHERE rent_id = ?";

        try (PreparedStatement dailyStatement = connection.prepareStatement(dailySql);
             PreparedStatement weeklyStatement = connection.prepareStatement(weeklySql)) {
            dailyStatement.setInt(1, rentId);
            weeklyStatement.setInt(1, rentId);

            ResultSet dailyResult = dailyStatement.executeQuery();
            ResultSet weeklyResult = weeklyStatement.executeQuery();

            if (dailyResult.next() && dailyResult.getInt(1) > 0) {
                return 1; // Daily rental
            } else if (weeklyResult.next() && weeklyResult.getInt(1) > 0) {
                return 2; // Weekly rental
            } else {
                return 0; // Neither daily nor weekly
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
            return -1; // Error occurred
        }
    }


    public static ObservableList<Rental> getRentals() {
        return rentals;
    }
}
