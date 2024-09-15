package SqlClass;

import ERDClasses.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<Car> availableCars = FXCollections.observableArrayList();
    private static ObservableList<Car> rentedCars = FXCollections.observableArrayList();

    public CarDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert car method
    public void insertCar(Car car) {
        // Check if a car with the same primary key already exists
        if (carExists(car.getCarNo())) {
            System.out.println("Car with car_No " + car.getCarNo() + " already exists.");
            return;
        }

        String sql = "INSERT INTO Car (car_No, car_color, rental_price, model, mileage, physical_condition) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, car.getCarNo());
            preparedStatement.setString(2, car.getColor());
            preparedStatement.setDouble(3, car.getRentalPrice());
            preparedStatement.setString(4, car.getModel());
            preparedStatement.setDouble(5, car.getMileage());
            preparedStatement.setString(6, car.getCondition());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                availableCars.add(car);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Car inserted successfully.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    // Delete car method
    public void deleteCar(int carNo) {
        // Check if the car exists before attempting to delete
        if (!carExists(carNo)) {
            System.out.println("Car with car_No " + carNo + " does not exist.");
            return;
        }

        String sql = "DELETE FROM Car WHERE car_No = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, carNo);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Car Deleted successfully.");
                alert.showAndWait();
            } else {
                System.out.println("Car with car_No " + carNo + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    // Update car method
    public void updateCar(Car car, int oldCarNo) {
        // Check if the old car number exists before attempting to update
        if (!carExists(oldCarNo)) {
            System.out.println("Car with car_No " + oldCarNo + " does not exist.");
            return;
        }

        // If the new car number is different, check if it exists
        if (car.getCarNo() != oldCarNo && carExists(car.getCarNo())) {
            System.out.println("Car with car_No " + car.getCarNo() + " already exists.");
            return;
        }

        String sql = "UPDATE Car SET car_No = ?, car_color = ?, rental_price = ?, model = ?, mileage = ?, physical_condition = ? " +
                "WHERE car_No = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, car.getCarNo()); // Update car number
            preparedStatement.setString(2, car.getColor());
            preparedStatement.setDouble(3, car.getRentalPrice());
            preparedStatement.setString(4, car.getModel());
            preparedStatement.setDouble(5, car.getMileage());
            preparedStatement.setString(6, car.getCondition());
            preparedStatement.setInt(7, oldCarNo); // Use old car number for WHERE clause

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Car updated successfully.");
            } else {
                System.out.println("Failed to update car.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    // Method to check if a car exists
    public boolean carExists(int carNo) {
        String sql = "SELECT COUNT(*) FROM Car WHERE car_No = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, carNo);
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

    public static List<Car> availableCars() {
        List<Car> availableCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE car_No NOT IN (SELECT car_No FROM Rental_Contract)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int carNo = resultSet.getInt("car_No");
                    String color = resultSet.getString("car_color");
                    double rentalPrice = resultSet.getDouble("rental_price");
                    String model = resultSet.getString("model");
                    double mileage = resultSet.getDouble("mileage");
                    String condition = resultSet.getString("physical_condition");
                    System.out.println("Car is found" + model);
                    Car car = new Car(color, model, condition, carNo, rentalPrice, mileage);
                    availableCars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return availableCars;
    }

    public static List<Car> nonAvailableCars() {
        List<Car> nonAvailableCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE car_No IN (SELECT car_No FROM Rental_Contract)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String color = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");

                Car car = new Car(color, model, condition, carNo, rentalPrice, mileage);
                nonAvailableCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return nonAvailableCars;
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
    public static int getNumberOfAvailableCars() {
        int numberOfAvailableCars = 0;
        String sql = "SELECT COUNT(*) AS availableCarsCount FROM Car WHERE car_No NOT IN (SELECT car_No FROM Rental_Contract)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    numberOfAvailableCars = resultSet.getInt("availableCarsCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return numberOfAvailableCars;
    }

    public static int getNumberOfRentedCars() {
        int numberOfRentedCars = 0;
        String sql = "SELECT COUNT(*) AS rentedCarsCount FROM Rental_Contract";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    numberOfRentedCars = resultSet.getInt("rentedCarsCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return numberOfRentedCars;
    }

    public static int getTotalNumberOfCars() {
        int totalNumberOfCars = 0;
        String sql = "SELECT COUNT(*) AS totalCarsCount FROM Car";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalNumberOfCars = resultSet.getInt("totalCarsCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return totalNumberOfCars;
    }
    public List<Car> searchCarsByPartialId(int partialId) {
        List<Car> foundCars = new ArrayList<>();
        try {
            String partialIdString = "%" + partialId + "%"; // Construct the partial ID pattern
            String sql = "SELECT * FROM Car WHERE CAST(car_No AS CHAR) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, partialIdString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String color = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");
                Car car = new Car(color, model, condition, carNo, rentalPrice, mileage);
                foundCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return foundCars;
    }
    public List<Car> searchCarsByModel(String model) {
        List<Car> foundCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE model LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Construct the partial model pattern
            String partialModel = "%" + model + "%";
            preparedStatement.setString(1, partialModel);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String color = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String carModel = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");

                // Create a new Car object with retrieved data
                Car car = new Car(color, carModel, condition, carNo, rentalPrice, mileage);
                foundCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return foundCars;
    }
    public List<Car> searchCarsByConditionAndColor(String condition, String color) {
        List<Car> foundCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE physical_condition LIKE ? AND car_color = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Construct the partial condition pattern
            String partialCondition = "%" + condition + "%";
            preparedStatement.setString(1, partialCondition);
            preparedStatement.setString(2, color);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String carColor = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String carCondition = resultSet.getString("physical_condition");

                // Create a new Car object with retrieved data
                Car car = new Car(carColor, model, carCondition, carNo, rentalPrice, mileage);
                foundCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return foundCars;
    }

public List<Car> searchCarsByCondition(String condition) {
    List<Car> foundCars = new ArrayList<>();
    String sql = "SELECT * FROM Car WHERE physical_condition LIKE ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        // Construct the partial condition pattern
        String partialCondition = "%" + condition + "%";
        preparedStatement.setString(1, partialCondition);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int carNo = resultSet.getInt("car_No");
            String color = resultSet.getString("car_color");
            double rentalPrice = resultSet.getDouble("rental_price");
            String model = resultSet.getString("model");
            double mileage = resultSet.getDouble("mileage");
            String carCondition = resultSet.getString("physical_condition");

            // Create a new Car object with retrieved data
            Car car = new Car(color, model, carCondition, carNo, rentalPrice, mileage);
            foundCars.add(car);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exceptions accordingly
    }

    return foundCars;
}



    public List<Car> searchCarsByColor(String color) {
        List<Car> foundCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE car_color = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, color);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String carColor = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");

                // Create a new Car object with retrieved data
                Car car = new Car(carColor, model, condition, carNo, rentalPrice, mileage);
                foundCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return foundCars;
    }
    public static List<Car> getAllCars() {
        List<Car> allCars = new ArrayList<>();
        String sql = "SELECT * FROM Car";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String color = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");

                Car car = new Car(color, model, condition, carNo, rentalPrice, mileage);
                allCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return allCars;
    }
    public List<Car> searchCarsByColorConditionAndModel(String color, String condition, String model) {
        List<Car> foundCars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE car_color = ? AND physical_condition = ? AND model LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, condition);
            preparedStatement.setString(3, "%" + model + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int carNo = resultSet.getInt("car_No");
                String carColor = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String carModel = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String carCondition = resultSet.getString("physical_condition");

                // Create a new Car object with retrieved data
                Car car = new Car(carColor, carModel, carCondition, carNo, rentalPrice, mileage);
                foundCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return foundCars;
    }

    public  static int getCountOfCarsByModel(String model) {
        int count = 0;
        String sql = "SELECT COUNT(*) AS carCount FROM Car WHERE model = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, model);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("carCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return count;
    }
    public static Car getCarById(int carId) {
        Car car = null;
        String sql = "SELECT * FROM Car WHERE car_No = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, carId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String color = resultSet.getString("car_color");
                double rentalPrice = resultSet.getDouble("rental_price");
                String model = resultSet.getString("model");
                double mileage = resultSet.getDouble("mileage");
                String condition = resultSet.getString("physical_condition");

                car = new Car(color, model, condition, carId, rentalPrice, mileage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
        return car;
    }


    public static ObservableList<Car> getAvailableCars() {
        return availableCars;
    }

    public static ObservableList<Car> getRentedCars() {
        return rentedCars;
    }
}
