package JavaFXClasses;

import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarAvailabilityChart extends PieChart {

    private Connection connection;

    public CarAvailabilityChart(Connection connection) {
        this.connection = connection;
        initData();
    }

    private void initData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        int availableCars = getAvailableCarsCount();
        int nonAvailableCars = getTotalCarsCount() - availableCars;

        pieChartData.add(new PieChart.Data("Available Cars", availableCars));
        pieChartData.add(new PieChart.Data("Non-Available Cars", nonAvailableCars));

        setData(pieChartData);
    }

    private int getAvailableCarsCount() {
        String sql = "SELECT COUNT(*) AS count FROM Car c WHERE NOT EXISTS (" +
                "SELECT * FROM Rental_Contract r WHERE r.car_No = c.car_No)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default to 0 if there's an error or no result
    }

    private int getTotalCarsCount() {
        String sql = "SELECT COUNT(*) AS count FROM Car";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default to 0 if there's an error or no result
    }
}
