package JavaFXClasses;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerChart extends LineChart<Number, Number> {
    public CustomerChart(Connection connection) {
        super(new NumberAxis(1, 30, 1), new NumberAxis());
        createLineChart(connection);
    }

    public void createLineChart(Connection connection) {
        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        // Preparing the data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Number of Rentals for Current Month");

        // Retrieve the number of rentals for each day of the current month from the database
        try {
            String sql = "SELECT DAY(rent_date) AS day, COUNT(*) AS num_rentals " +
                    "FROM Rental_Contract " +
                    "WHERE MONTH(rent_date) = ? AND YEAR(rent_date) = ? " +
                    "GROUP BY DAY(rent_date)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentMonth);
            preparedStatement.setInt(2, currentYear);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Populate the series with the retrieved data
            while (resultSet.next()) {
                int day = resultSet.getInt("day");
                int numRentals = resultSet.getInt("num_rentals");
                // Add the data to the series, day to x-axis and numRentals to y-axis
                series.getData().add(new XYChart.Data<>(day, numRentals));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setting axis labels
        ((NumberAxis) getXAxis()).setLabel("Day of the Month");
        ((NumberAxis) getYAxis()).setLabel("Number of Rentals");

        // Adding the series to the chart
        this.getData().add(series);
    }
}
