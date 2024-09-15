package JavaFXClasses;

import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarChart extends PieChart {

    private Connection connection;

    public CarChart(Connection connection) {
        this.connection = connection;
        initData();
    }

    private void initData() {
        ObservableList<Data> pieChartData = getChartDataFromDatabase();
        setData(pieChartData);
    }

    private ObservableList<Data> getChartDataFromDatabase() {
        ObservableList<Data> pieChartData = FXCollections.observableArrayList();

        String sql = "SELECT model, COUNT(*) AS count FROM Car GROUP BY model";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String model = resultSet.getString("model");
                int count = resultSet.getInt("count");
                pieChartData.add(new Data(model, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }

        return pieChartData;
    }

    // Method to update the PieChart data
    public void updateChartData() {
        ObservableList<Data> updatedData = getChartDataFromDatabase();
        getData().clear();
        setData(updatedData);
    }
}
