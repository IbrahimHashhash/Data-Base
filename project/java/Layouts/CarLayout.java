package Layouts;

import ERDClasses.Car;
import JavaFXClasses.Manager;
import JavaFXClasses.Style;
import SqlClass.CarDAO;
import SqlClass.DatabaseConnection;
import TableViews.CarTableView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CarLayout extends BorderPane {
    BorderPane borderPane = Style.borderPane();
    CarDAO carDAO = new CarDAO(DatabaseConnection.getConnection());
    static CarTableView carTableView = new CarTableView();

    VBox vBox = Style.createVBox();
    public CarLayout() throws SQLException {
        TextField searchBar = Style.createTextField("Enter ID");
        searchBar.setAlignment(Pos.CENTER);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue,carTableView,carDAO);
        });

     /*  carTableView.getItems().clear();
        carTableView.getItems().addAll(CarDAO.getAllCars());

      */
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete,update);
        carTableView.setOnMouseClicked(e -> {
            contextMenu.show(carTableView, e.getScreenX(), e.getScreenY()); // pops up a menu that contains delete and update when clicking on the table view
        });
        update.setOnAction(e -> { // handing the update logic, it just takes the information from the table view item and it updates it
            Car car = carTableView.getSelectionModel().getSelectedItem();
            handleUpdate(car);
        });
        delete.setOnAction(e -> {
            Car car = carTableView.getSelectionModel().getSelectedItem();
            boolean res = Style.showConfirmation("Are you sure you want to delete this car: " + car.getCarNo() + "\nModel: " + car.getModel() + "\nCar Color: " + car.getColor());
            if(res){
                handleDelete(car);
            }

        });
        this.setCenter(carTableView);

        Button search = Style.createButton("Search");
        search.setOnAction(e->{
            handleOtherSearch(carTableView,borderPane,carDAO);
        });
        Button insert = Style.createButton("Insert");
        vBox.getChildren().addAll(search,insert);
        insert.setOnAction(e->{
            handleInsert();
        });
        this.setRight(borderPane);
        borderPane.setCenter(vBox);
        Button back = new Button("Back");
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10,10,10,10));
        topH.getChildren().add(back);
        borderPane.setTop(topH);

        back.setOnAction(e->{
            if(carTableView.getItems().isEmpty()){
                carTableView.getItems().addAll(CarDAO.getAllCars());
            }
            borderPane.setCenter(vBox);
        });
        this.setBottom(Style.search(searchBar));
    }
    public static void handleOtherSearch(CarTableView carTableView,BorderPane borderPane, CarDAO carDAO) {
        // Create RadioButtons for condition and colors
        RadioButton[] colors = new RadioButton[4];
        String[] colorLabels = {"Red", "Black", "White", "Blue"};
        ToggleGroup colorToggleGroup = new ToggleGroup();

        for (int i = 0; i < colors.length; i++) {
            colors[i] = new RadioButton(colorLabels[i]);
            colors[i].setToggleGroup(colorToggleGroup);
        }

        ToggleGroup conditionToggleGroup = new ToggleGroup();
        RadioButton[] condition = new RadioButton[3];
        String[] conLabel = {"Excellent", "Good", "Bad"};
        for (int i = 0; i < condition.length; i++) {
            condition[i] = new RadioButton(conLabel[i]);
            condition[i].setToggleGroup(conditionToggleGroup);
        }

        // Create HBox for condition and color options
        HBox conditionH = Style.createHBox();
        HBox colorsH = Style.createHBox();
        colorsH.getChildren().addAll(colors);
        conditionH.getChildren().addAll(condition);

        // Create TextField for model search
        TextField model = Style.createTextField("Enter model");

        // Create GridPane to layout the search options
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Colors: "), 0, 0);
        gp.add(colorsH, 1, 0);
        gp.add(Style.createText("Condition: "), 0, 1);
        gp.add(conditionH, 1, 1);
        gp.add(Style.createText("Model: "), 0, 2);
        gp.add(model, 1, 2);

        // Create VBox to contain the search options
        VBox search = Style.createInsertVBox();
        search.getChildren().addAll(gp);

        // Add the search VBox to the center of the BorderPane
        borderPane.setCenter(search);

        // Set actions for the search options
        // Action for model search
        Button btM = new Button("+");
        gp.add(btM, 2, 2);
        btM.setOnAction(e -> {
            if (model.getText().isEmpty()) {
                alert("Model is Empty");
                return;
            }
            // Clear the table view and add cars found by model search
            carTableView.getItems().clear();
            carTableView.getItems().addAll(carDAO.searchCarsByModel(model.getText()));
        });

        // Actions for color search
        for (RadioButton color : colors) {
            color.setOnAction(e -> {
                if (color.isSelected()) {
                    carTableView.getItems().clear();
                    carTableView.getItems().addAll(carDAO.searchCarsByColor(color.getText()));
                }
            });
        }

        // Actions for condition search
        for (RadioButton cond : condition) {
            cond.setOnAction(e -> {
                if (cond.isSelected()) {
                    carTableView.getItems().clear();
                    carTableView.getItems().addAll(carDAO.searchCarsByCondition(cond.getText()));
                }
            });
        }
        for (RadioButton color : colors) {
            color.setOnAction(e -> {
                if (color.isSelected()) {
                    // If both color and condition options are selected, perform search by both
                    if (conditionToggleGroup.getSelectedToggle() != null) {
                        String selectedCondition = ((RadioButton) conditionToggleGroup.getSelectedToggle()).getText();
                        carTableView.getItems().clear();
                        carTableView.getItems().addAll(carDAO.searchCarsByConditionAndColor(color.getText(), selectedCondition));
                    } else {
                        // Otherwise, perform search by color only
                        carTableView.getItems().clear();
                        carTableView.getItems().addAll(carDAO.searchCarsByColor(color.getText()));
                    }
                }
            });
        }

        for (RadioButton cond : condition) {
            cond.setOnAction(e -> {
                if (cond.isSelected()) {
                    // If both color and condition options are selected, perform search by both
                    if (colorToggleGroup.getSelectedToggle() != null) {
                        String selectedColor = ((RadioButton) colorToggleGroup.getSelectedToggle()).getText();
                        carTableView.getItems().clear();
                        carTableView.getItems().addAll(carDAO.searchCarsByConditionAndColor(selectedColor, cond.getText()));
                    } else {
                        // Otherwise, perform search by condition only
                        carTableView.getItems().clear();
                        carTableView.getItems().addAll(carDAO.searchCarsByCondition(cond.getText()));
                    }
                }
            });
        }
    }
    public void handleInsert() {
        // Create components
        TextField id = Style.createTextField("Enter Car ID");
        TextField model = Style.createTextField("Enter Model");
        TextField rental = Style.createTextField("Enter Rental Price");
        TextField mileage = Style.createTextField("Enter Mileage");
        RadioButton[] condition = new RadioButton[3];
        condition[0] = new RadioButton("Good");
        condition[1] = new RadioButton("Average");
        condition[2] = new RadioButton("Bad");
        RadioButton[] colors = new RadioButton[4];
        String[] colorLabels = {"Red","Black","White","Blue"};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new RadioButton(colorLabels[i]);
        }
        Button button = new Button("Enter");

        // Create toggle groups
        ToggleGroup conditionToggleGroup = new ToggleGroup();
        condition[0].setToggleGroup(conditionToggleGroup);
        condition[1].setToggleGroup(conditionToggleGroup);
        condition[2].setToggleGroup(conditionToggleGroup);

        ToggleGroup colorToggleGroup = new ToggleGroup();
        for (RadioButton color : colors) {
            color.setToggleGroup(colorToggleGroup);
        }

        // Create grid pane
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Car ID"), 0, 0);
        gp.add(id, 1, 0);
        gp.add(Style.createText("Model"), 0, 1);
        gp.add(model, 1, 1);
        gp.add(Style.createText("Condition"), 0, 2);
        HBox conditionHbox = new HBox(condition);
        conditionHbox.setSpacing(10);
        gp.add(conditionHbox, 1, 2);
        gp.add(Style.createText("Color"), 0, 3);
        HBox colorHbox = new HBox(colors);
        colorHbox.setSpacing(10);
        gp.add(colorHbox, 1, 3);
        gp.add(Style.createText("Rental Price"), 0, 4);
        gp.add(rental, 1, 4);
        gp.add(Style.createText("mileage"), 0, 5);
        gp.add(mileage, 1, 5);

        // Create insert vbox
        VBox insertCar = Style.createVBox();
        insertCar.getChildren().addAll(gp, button);

        // Set insert as the right of the border pane
        // Set action for the enter button
        button.setOnAction(e -> {
            // Call insertion action
            insertAction(id, model,mileage, conditionToggleGroup ,colorToggleGroup, rental);
        });
        borderPane.setCenter(insertCar);


    }

    private void insertAction(TextField id, TextField model, TextField mileage,ToggleGroup conditionToggleGroup, ToggleGroup colorToggleGroup, TextField rental) {
        // Check for empty fields
        if (id.getText().isEmpty() || model.getText().isEmpty() || rental.getText().isEmpty() ||
                conditionToggleGroup.getSelectedToggle() == null || colorToggleGroup.getSelectedToggle() == null || mileage.getText().isEmpty() ) {
            alert("All Fields are required");
            return;
        }

        String condition = ((RadioButton) conditionToggleGroup.getSelectedToggle()).getText();
        String color = ((RadioButton) colorToggleGroup.getSelectedToggle()).getText();

        String idS =  id.getText();
        String modelS =  model.getText();
        if(!Manager.isNum(idS) || !Manager.isNum(rental.getText()) || !Manager.isNum(mileage.getText()) ){
            alert("Invalid Number Format");
            return;
        }
        int idN = Integer.parseInt(idS);
        double price = Double.parseDouble(rental.getText());
        int mil = Integer.parseInt(mileage.getText());
        Car car = new Car( color,  modelS,  condition,  idN, price,mil);
        carDAO.insertCar(car);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleUpdate(Car car){
        CheckBox[] c = new CheckBox[6];
        TextField id = Style.createTextField("Enter Car ID");
        TextField model = Style.createTextField("Enter Model");
        TextField rental = Style.createTextField("Enter Rental Price");
        TextField mileage = Style.createTextField("Enter Mileage");
        RadioButton[] condition = new RadioButton[3];
        condition[0] = new RadioButton("Good");
        condition[1] = new RadioButton("Average");
        condition[2] = new RadioButton("Bad");
        RadioButton[] colors = new RadioButton[4];
        String[] colorLabels = {"Red","Black","White","Blue"};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new RadioButton(colorLabels[i]);
        }
        for (int i=0;i<c.length;i++){
            c[i] = new CheckBox();
        }
        Button button = new Button("Update");

        // Create toggle groups
        ToggleGroup conditionToggleGroup = new ToggleGroup();
        condition[0].setToggleGroup(conditionToggleGroup);
        condition[1].setToggleGroup(conditionToggleGroup);
        condition[2].setToggleGroup(conditionToggleGroup);

        ToggleGroup colorToggleGroup = new ToggleGroup();
        for (RadioButton color : colors) {
            color.setToggleGroup(colorToggleGroup);
        }

        // Create grid pane
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Car ID"), 0, 0);
        gp.add(id, 1, 0);
        gp.add(Style.createText("Model"), 0, 1);
        gp.add(model, 1, 1);
        gp.add(Style.createText("Condition"), 0, 2);
        HBox conditionHbox = new HBox(condition);
        conditionHbox.setSpacing(10);
        gp.add(conditionHbox, 1, 2);
        gp.add(Style.createText("Color"), 0, 3);
        HBox colorHbox = new HBox(colors);
        colorHbox.setSpacing(10);
        gp.add(colorHbox, 1, 3);
        gp.add(Style.createText("Rental Price"), 0, 4);
        gp.add(rental, 1, 4);

        gp.add(Style.createText("Mileage"), 0, 5);
        gp.add(mileage, 1, 5);

        gp.add(c[0], 2, 0);
        gp.add(c[1], 2, 1);
        gp.add(c[2], 2, 2);
        gp.add(c[3], 2, 3);
        gp.add(c[4], 2, 4);
        gp.add(c[5], 2, 5);

        // Create insert vbox
        VBox insertCar = Style.createVBox();
        insertCar.getChildren().addAll(gp, button);
        Stage stage = new Stage();
        Scene scene = new Scene(insertCar,510,580);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update: " + car.getModel());
        stage.setResizable(false);
        stage.show();

        button.setOnAction(e->{
            handleUpdate(id,model,mileage,conditionToggleGroup,colorToggleGroup,rental,c,car);
        });

    }
    private void handleUpdate(TextField id, TextField model, TextField mileage,ToggleGroup conditionToggleGroup, ToggleGroup colorToggleGroup, TextField rental,CheckBox[] c,Car car) {
        int oldId = car.getCarNo();
        // Check for empty fields
        if(c[0].isSelected()){
            if(id.getText().isEmpty()){
                alert("Enter ID");
                return;
            }
            if(!Manager.isNum(id.getText())){
                alert("Invalid Format");
                return;
            }
            if(carDAO.carExists(Integer.parseInt(id.getText()))){
                alert("Updated Car ID already exists, show a new one");
                return;

            }
            car.setCarNo(Integer.parseInt(id.getText()));
        }
        if(c[1].isSelected()){
            if(model.getText().isEmpty()){
                alert("Enter Car Model.");
                return;
            }
            car.setModel(model.getText());
        }

        if(c[2].isSelected()){
            if(conditionToggleGroup.getSelectedToggle() == null){
                alert("Select Car Condition please.");
                return;
            }
            car.setCondition(((RadioButton) conditionToggleGroup.getSelectedToggle()).getText());
        }
        if(c[3].isSelected()){
            if(colorToggleGroup.getSelectedToggle() == null){
                alert("Select Car Color please.");
                return;
            }
            car.setColor(((RadioButton) colorToggleGroup.getSelectedToggle()).getText());

        }
        if(c[4].isSelected()){
            if(rental.getText().isEmpty()){
                alert("Enter Rental Price please.");
                return;
            }
            if(!Manager.isNum(rental.getText())){
                alert("Invalid Format");
                return;
            }

            car.setRentalPrice(Integer.parseInt(rental.getText()));

        }
        if(c[5].isSelected()){
            if(mileage.getText().isEmpty()){
                alert("Enter Rental Price please.");
                return;
            }
            if(!Manager.isNum(mileage.getText())){
                alert("Invalid Format");
                return;
            }

            car.setMileage(Integer.parseInt(mileage.getText()));
        }
        carDAO.updateCar(car,oldId);
    }

    public void handleDelete(Car car){
        carDAO.deleteCar(car.getCarNo());
        if(carDAO.getAvailableCars().contains(car)) {
            carDAO.getAvailableCars().remove(car);
        }

        if(carDAO.getRentedCars().contains(car)){
            carDAO.getRentedCars().remove(car);
        }


    }
    public static void handleSearch(String value,CarTableView carTableView, CarDAO carDAO) {
        try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all cars
                List<Car> allCars = CarDAO.availableCars();
                if (!allCars.isEmpty()) {
                    // Set the items of the table view with all cars
                    carTableView.getItems().setAll(allCars);
                    // Select the first item in the table view
                    carTableView.getSelectionModel().selectFirst();
                }
            } else {
                // If the search value is not empty, perform partial ID search
                List<Car> foundCars = carDAO.searchCarsByPartialId(Integer.parseInt(value));
                carTableView.getItems().clear(); // Clear the table view
                if (!foundCars.isEmpty()) {
                    // Set the items of the table view with the found cars
                    carTableView.getItems().setAll(foundCars);
                    // Select the first item in the table view
                    carTableView.getSelectionModel().selectFirst();
                }
            }
        } catch (NumberFormatException e) {
            alert("Please enter a valid ID for searching.");
        }
    }

    public static void alert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
