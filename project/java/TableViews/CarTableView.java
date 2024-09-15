package TableViews;

import ERDClasses.Car;
import SqlClass.CarDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CarTableView extends TableView<Car> {
    public CarTableView(){
        this(CarDAO.getAllCarsArray());
    }

    public CarTableView(ObservableList<Car> cars) {
        this.setPadding(new Insets(20,20,20,20));
        TableColumn<Car, String> colorCol = new TableColumn<>("Color");
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorCol.setPrefWidth(120);
        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(200);
        TableColumn<Car, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        conditionCol.setPrefWidth(150);

        TableColumn<Car, Integer> carNoCol = new TableColumn<>("Car No");
        carNoCol.setCellValueFactory(new PropertyValueFactory<>("carNo"));
        carNoCol.setPrefWidth(200);

        TableColumn<Car, Double> rentalPriceCol = new TableColumn<>("Rental Price");
        rentalPriceCol.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));
        rentalPriceCol.setPrefWidth(130);
        this.getColumns().addAll(colorCol, modelCol, conditionCol, carNoCol, rentalPriceCol);
        this.setItems(cars);
    }
}
