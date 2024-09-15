package Layouts;

import ERDClasses.DailyRental;
import ERDClasses.Rental;
import ERDClasses.WeeklyRental;
import JavaFXClasses.CarList;
import JavaFXClasses.MainScreen;
import JavaFXClasses.Manager;
import JavaFXClasses.Style;
import SqlClass.*;
import TableViews.CarTableView;
import TableViews.CustomerTableView;
import TableViews.RentalTableView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalLayout extends BorderPane {

    BorderPane borderPane = Style.borderPane();

    RentalDAO rentalDAO = new RentalDAO(DatabaseConnection.getConnection());
    RentalTableView rentalTableView = new RentalTableView();
    CustomerTableView customerTableView = new CustomerTableView();
    public static CarTableView carTableView = new CarTableView();
    VBox originalTable = Style.createVBox();
    ScrollPane scrollPane = new ScrollPane();
    CarDAO carDAO = new CarDAO(DatabaseConnection.getConnection());
    CustomerDAO customerDAO = new CustomerDAO(DatabaseConnection.getConnection());

    VBox vBox = Style.createVBox();
    public RentalLayout() throws SQLException {
        carTableView.getItems().clear();
        CarDAO.getAvailableCars().clear();
        CarDAO.getAvailableCars().addAll(CarDAO.getAllAvailableCars());
        carTableView.setItems(CarDAO.getAvailableCars());

        rentalTableView.getItems().clear();
      //  rentalTableView.getItems().addAll(RentalDAO.getAllRentals());
        TextField searchBar = Style.createTextField("Enter Rental Info");
        searchBar.setAlignment(Pos.CENTER);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue);
        });

        TextField cusSearch = Style.createTextField("Enter Customer Info");
        cusSearch.setAlignment(Pos.CENTER);
        cusSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            CustomerLayout.handleSearch(newValue, customerDAO, customerTableView);
        });


        TextField carSearch = Style.createTextField("Enter Car Info");
        carSearch.setAlignment(Pos.CENTER);
        carSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            CarLayout.handleSearch(newValue, carTableView, carDAO);
        });
        CustomerDAO.getCustomers().clear();
  //     CustomerDAO.getCustomers().addAll(CustomerDAO.getAllCustomers());
        RentalDAO.getRentals().clear();
      //  RentalDAO.getRentals().addAll(RentalDAO.getAllRentals());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete, update);
        rentalTableView.setOnMouseClicked(e -> {
            contextMenu.show(rentalTableView, e.getScreenX(), e.getScreenY()); // pops up a menu that contains delete and update when clicking on the table view
        });
        update.setOnAction(e -> { // handing the update logic, it just takes the information from the table view item and it updates it
            Rental rent = rentalTableView.getSelectionModel().getSelectedItem();
            handleUpdate(rent);
        });
        delete.setOnAction(e -> {
            Rental rental = rentalTableView.getSelectionModel().getSelectedItem();
            boolean result = Style.showConfirmation("Are you sure want to delete this rental: " + rental.getRentID() + "\nStart Date: " + rental.getRentDate() + "\nReturn Date: " + rental.getReturnDate());
            if (result) {
                handleDelete(rental.getRentID());
            }
        });
        originalTable.getChildren().addAll(rentalTableView, customerTableView, carTableView);
        scrollPane.setContent(originalTable);
        this.setCenter(scrollPane);
        this.setRight(borderPane);
        borderPane.setCenter(vBox);
        Button back = new Button("Back");
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10, 10, 10, 10));
        topH.getChildren().add(back);
        borderPane.setTop(topH);
        back.setOnAction(e -> {
            borderPane.setCenter(vBox);
        });
        Button insert = Style.createButton("Insert");
        vBox.getChildren().addAll(insert);
        insert.setOnAction(e -> {
            if (carTableView.getSelectionModel().getSelectedItem() != null && customerTableView.getSelectionModel().getSelectedItem() != null) {
                handleInsert();
            } else {
                showAlert("Select both car and customer from the Table please");
            }

        });
        HBox hBox = Style.createHBox();
        hBox.getChildren().addAll(searchBar, cusSearch, carSearch);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.setPadding(new Insets(10, 10, 10, 360));
        hBox.setSpacing(10);
        this.setBottom(hBox);
    }

    void handleSearch(String value) {
        /*try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all customers
        //        List<Rental> allRentals = RentalDAO.getAllRentals();
                if (!allRentals.isEmpty()) {
                    // Set the items of the table view with all customers
                  //  rentalTableView.getItems().setAll(allRentals);
                    // Select the first item in the table view
                    rentalTableView.getSelectionModel().selectFirst();
                }
            } else {
                // If the search value is not empty, perform partial ID search
                List<Rental> foundRentals = rentalDAO.searchRentalsByPartialId(Integer.parseInt(value));
                rentalTableView.getItems().clear(); // Clear the table view
                if (!foundRentals.isEmpty()) {
                    // Set the items of the table view with the found customers
                    rentalTableView.getItems().setAll(foundRentals);
                    // Select the first item in the table view
                    rentalTableView.getSelectionModel().selectFirst();
                }
            }
        } catch (NumberFormatException e) {
            alert("Please enter a valid ID for searching.");
        }

        */
    }
    public void handleInsert() {
        // Create components
        TextField rentID = Style.createTextField("Enter Rent ID");
        TextField cusID = Style.createTextField("Enter Customer ID");
        TextField carID = Style.createTextField("Enter Car ID");
        RadioButton daily = new RadioButton("Daily");
        RadioButton weekly = new RadioButton("Weekly");
        RadioButton yes = new RadioButton("Yes");
        RadioButton no = new RadioButton("No");
        Button button = new Button("Insert");

        // Create toggle groups
        ToggleGroup typeToggleGroup = new ToggleGroup();
        daily.setToggleGroup(typeToggleGroup);
        weekly.setToggleGroup(typeToggleGroup);
        // Create grid pane
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Rent ID"), 0, 0);
        gp.add(rentID, 1, 0);
        gp.add(Style.createText("Customer ID"), 0, 1);
        gp.add(cusID, 1, 1);
        gp.add(Style.createText("Car ID"), 0, 2);
        gp.add(carID, 1, 2);

        gp.add(Style.createText("Type"), 0, 3);
        HBox typeHBox = new HBox(daily, weekly);
        typeHBox.setSpacing(10);
        gp.add(typeHBox, 1, 3);
        HBox cancellationHBox = new HBox(yes, no);
        cancellationHBox.setSpacing(10);
        TextField weeklyOrDaily = Style.createTextField(" ");
        // Create insert vbox
        VBox insertRent = Style.createVBox();
        insertRent.getChildren().addAll(gp, button);
        daily.setOnAction(e -> {
            removeNode(gp, 0, 4); // Remove previous nodes
            removeNode(gp, 1, 4); // Remove previous nodes


            if (daily.isSelected()) {
                weeklyOrDaily.setPromptText("Number Of Days");
                gp.add(Style.createText("Number Of Days"), 0, 4); // Add new nodes
                gp.add(weeklyOrDaily, 1, 4);
            }
        });

        weekly.setOnAction(e -> {
            removeNode(gp, 0, 4); // Remove previous nodes
            removeNode(gp, 1, 4); // Remove previous nodes
            if (weekly.isSelected()) {
                // Add the new nodes
                weeklyOrDaily.setPromptText("Number Of Weeks");
                gp.add(Style.createText("Number Of Weeks"), 0, 4);
                gp.add(weeklyOrDaily, 1, 4);
            }
        });
        cusID.setText(String.valueOf(customerTableView.getSelectionModel().getSelectedItem().getId()));
        carID.setText(String.valueOf(carTableView.getSelectionModel().getSelectedItem().getCarNo()));
        // Set insert as the center of the border pane
        borderPane.setCenter(insertRent);

        // Set action for the insert button
        button.setOnAction(e -> {
            // Call insertion action
            try {
                insertAction(cusID,carID,weeklyOrDaily,rentID, typeToggleGroup);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
    private void removeNode(GridPane gridPane, int columnIndex, int rowIndex) {
        ObservableList<Node> children = gridPane.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : children) {
            if (GridPane.getColumnIndex(node) == columnIndex && GridPane.getRowIndex(node) == rowIndex) {
                nodesToRemove.add(node);
            }
        }
        gridPane.getChildren().removeAll(nodesToRemove);
    }

    private void insertAction(TextField cus,TextField car,TextField weeklyOrDaily,TextField rentID, ToggleGroup typeToggleGroup) throws SQLException {
        // Check for empty fields
        if (rentID.getText().isEmpty() || weeklyOrDaily.getText().isEmpty()|| cus.getText().isEmpty()|| car.getText().isEmpty() || typeToggleGroup.getSelectedToggle() == null) {
            showAlert("Please fill in all fields.");
            return;
        }
        if(!Manager.isNum(weeklyOrDaily.getText()) || !Manager.isNum(rentID.getText())|| !Manager.isNum(cus.getText()) || !Manager.isNum(car.getText())){
            showAlert("Invalid Format.");
            return;
        }
        int carId = Integer.parseInt(car.getText());
        int cusID = Integer.parseInt(cus.getText());
        int no = Integer.parseInt(weeklyOrDaily.getText());
        int rentId = Integer.parseInt(rentID.getText());
        Date startDate = Date.valueOf(String.valueOf(LocalDate.now()));
        if(((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Daily")){
            //  public DailyRental(Date rentDate, Date returnDate, int rentID, String cancellation, int car_num, int emp_id, int cus_ID, IntegerProperty noOfDays) {
            Date endDate = Date.valueOf(String.valueOf(calculateEndDateDay(LocalDate.now(),no)));
                DailyRental dailyRental = new DailyRental(startDate,endDate,rentId,"None",carId, MainScreen.getEmpolyee().getId(), cusID,no);
            rentalDAO.insertRentalContract(dailyRental);

        }else if((((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Weekly"))){
            Date endDate = Date.valueOf(String.valueOf(calculateEndDateWeek(LocalDate.now(),no)));

            WeeklyRental weeklyRental = new WeeklyRental(startDate,endDate,rentId,"None",carId,MainScreen.getEmpolyee().getId(),cusID,no);
            rentalDAO.insertRentalContract(weeklyRental);
        }
        // All fields are filled, proceed with insertion logic here
        String type = ((RadioButton) typeToggleGroup.getSelectedToggle()).getText();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleUpdate(Rental rental) {
        TextField cancellation = Style.createTextField("Enter Cancellation Details");


        RadioButton daily = new RadioButton("Daily");
        RadioButton weekly = new RadioButton("Weekly");
        Button button = new Button("Update");

        // Create toggle groups
        ToggleGroup typeToggleGroup = new ToggleGroup();
        daily.setToggleGroup(typeToggleGroup);
        weekly.setToggleGroup(typeToggleGroup);

        // Create checkboxes
        CheckBox[] checkboxes = new CheckBox[2];
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = new CheckBox();
        }

        // Create grid pane

        GridPane gp = Style.createGridPane();


        HBox typeHBox = new HBox(daily, weekly);
        typeHBox.setSpacing(10);
        gp.add(Style.createText("Cancellation"), 0, 0);
        gp.add(cancellation, 1, 0);
        gp.add(Style.createText("Type"), 0, 1);
        gp.add(typeHBox, 1, 1);

        // Add checkboxes to grid pane
        for (int i = 0; i < checkboxes.length; i++) {
            gp.add(checkboxes[i], 2, i);
        }
        TextField weeklyOrDaily = Style.createTextField(" ");
        // Create update vbox
        VBox updateRent = Style.createVBox();
        updateRent.getChildren().addAll(gp, button);
        daily.setOnAction(e -> {
            removeNode(gp, 0, 2); // Remove previous nodes
            removeNode(gp, 1, 2); // Remove previous nodes



            if (daily.isSelected()) {
                weeklyOrDaily.setPromptText("Number Of Days");
                gp.add(Style.createText("Number Of Days"), 0, 2); // Add new nodes
                gp.add(weeklyOrDaily, 1, 2);
            }
        });

        weekly.setOnAction(e -> {
            removeNode(gp, 0, 2); // Remove previous nodes
            removeNode(gp, 1, 2); // Remove previous nodes

            if (weekly.isSelected()) {
                // Add the new nodes
                weeklyOrDaily.setPromptText("Number Of Weeks");
                gp.add(Style.createText("Number Of Weeks"), 0, 2);
                gp.add(weeklyOrDaily, 1, 2);
            }
        });
        ;

        // Set update as the center of the border pane
        Stage stage = new Stage();
        Scene scene = new Scene(updateRent,400,290);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update: " + rental.getReturnDate());
        stage.setResizable(false);
        stage.show();


        // Set action for the update button
        button.setOnAction(e -> {
            // Call update action
        });
    }
    private void updateAction(TextField weeklyOrDaily, TextField cancellation,ToggleGroup typeToggleGroup,Rental rental, CheckBox[] c) throws SQLException {
        // Check for empty fields
        if ( weeklyOrDaily.getText().isEmpty()|| cancellation.getText().isEmpty()|| typeToggleGroup.getSelectedToggle() == null) {
            showAlert("Please fill in all fields.");
            return;
        }
        if(c[0].isSelected()){
            if (cancellation.getText().isEmpty()) {
                showAlert("Please fill in all fields.");
                return;
            }
            rental.setCancellation(cancellation.getText());
        }

        int no = Integer.parseInt(weeklyOrDaily.getText());
        Date startDate = Date.valueOf(String.valueOf(LocalDate.now()));

        if(c[1].isSelected()){
            if (typeToggleGroup.getSelectedToggle() == null) {
                showAlert("Please fill in all fields.");
                return;
            }
            if(weeklyOrDaily.getText().isEmpty()){
                showAlert("Please fill in all fields.");
                return;
            }
            if(((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Daily") && rental instanceof DailyRental){
                //  public DailyRental(Date rentDate, Date returnDate, int rentID, String cancellation, int car_num, int emp_id, int cus_ID, IntegerProperty noOfDays) {
                Date endDate = Date.valueOf(String.valueOf(calculateEndDateDay(LocalDate.now(),no)));
                DailyRental dailyRental = new DailyRental(startDate,endDate,rental.getRentID(),"None",rental.getCar_num(), MainScreen.getEmpolyee().getId(), rental.getCus_ID(),no);
                rental.setReturnDate(endDate);
            }
            if((((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Weekly")) & rental instanceof WeeklyRental){
                Date endDate = Date.valueOf(String.valueOf(calculateEndDateWeek(LocalDate.now(),no)));
                WeeklyRental weeklyRental = new WeeklyRental(startDate,endDate,rental.getRentID(),"None",rental.getCar_num(),MainScreen.getEmpolyee().getId(),rental.getCus_ID(),no);
                rental.setReturnDate(endDate);
            }



            if(((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Daily") && !(rental instanceof DailyRental)){
                //  public DailyRental(Date rentDate, Date returnDate, int rentID, String cancellation, int car_num, int emp_id, int cus_ID, IntegerProperty noOfDays) {
                Date endDate = Date.valueOf(String.valueOf(calculateEndDateDay(LocalDate.now(),no)));
                DailyRental dailyRental = new DailyRental(startDate,endDate,rental.getRentID(),"None",rental.getCar_num(), MainScreen.getEmpolyee().getId(), rental.getCus_ID(),no);
                rentalDAO.deleteRentalContract(rental.getRentID());
                rentalDAO.insertRentalContract(dailyRental);

            }
            if((((RadioButton)typeToggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("Weekly")) & !(rental instanceof WeeklyRental)){
                Date endDate = Date.valueOf(String.valueOf(calculateEndDateWeek(LocalDate.now(),no)));
                WeeklyRental weeklyRental = new WeeklyRental(startDate,endDate,rental.getRentID(),"None",rental.getCar_num(),MainScreen.getEmpolyee().getId(),rental.getCus_ID(),no);
                rental.setReturnDate(endDate);
                rentalDAO.deleteRentalContract(rental.getRentID());
                rentalDAO.insertRentalContract(weeklyRental);

            }



        }

        // All fields are filled, proceed with insertion logic here
    }

    public void handleDelete(int id){
        rentalDAO.deleteRentalContract(id);
    }
    public static LocalDate calculateEndDateWeek(LocalDate startDate, int weeksToAdd) {
        return startDate.plusWeeks(weeksToAdd);
    }
    public static LocalDate calculateEndDateDay(LocalDate startDate, int daysToAdd) {
        return startDate.plusDays(daysToAdd);
    }
    public void alert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();

    }

}
