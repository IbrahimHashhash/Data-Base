package Layouts;

import ERDClasses.*;
import JavaFXClasses.Manager;
import JavaFXClasses.Style;
import SqlClass.*;
import TableViews.PaymentTableView;
import TableViews.RentalTableView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaymentLayout extends BorderPane {
    PaymentTableView paymentTableView = new PaymentTableView();
    BorderPane borderPane = Style.borderPane();

    PaymentDAO paymentDAO = new PaymentDAO(DatabaseConnection.getConnection());
    RentalDAO rentalDAO = new RentalDAO(DatabaseConnection.getConnection());
    RentalTableView rentalTableView = new RentalTableView();
    VBox tableBox = Style.createVBox();

    VBox vBox = Style.createVBox();
    public PaymentLayout() throws SQLException {
        paymentTableView.getItems().clear();
        paymentTableView.getItems().addAll(PaymentDAO.getAllPayments());
        TextField searchBar = Style.createTextField("Enter ID");
        searchBar.setAlignment(Pos.CENTER);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
           handleSearch(newValue);
        });

        CustomerDAO.getCustomers().clear();
        CustomerDAO.getCustomers().addAll(CustomerDAO.getAllCustomers());
        RentalDAO.getRentals().clear();
        RentalDAO.getRentals().addAll(RentalDAO.getAllRentals());

        this.setStyle("-fx-background-color:white;");
        paymentTableView.setMaxHeight(200);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete,update);
        paymentTableView.setOnMouseClicked(e -> {
            contextMenu.show(paymentTableView, e.getScreenX(), e.getScreenY()); // pops up a menu that contains delete and update when clicking on the table view
        });
        update.setOnAction(e -> {
            if(paymentTableView.getSelectionModel()==null){
                showAlert("Select payment From TableView");
                return;
            }
            handleUpdate(paymentTableView.getSelectionModel().getSelectedItem());
        });
        delete.setOnAction(e -> {
            Payment payment = paymentTableView.getSelectionModel().getSelectedItem();
            boolean res = Style.showConfirmation("Are you sure you want to delete this payment: " + payment.getPaymentID() + "\nAmount: " + payment.getAmount() + "\nDate: " + payment.getPaydate());
            if(res) {
                handleDelete(payment.getPaymentID());
            }
        });
        VBox payment = Style.createInsertVBox();
        Button loan = Style.createButton("Loan");
        Button total = Style.createButton("Total");
        payment.getChildren().addAll(loan,total);

        tableBox.setSpacing(50);
        tableBox.getChildren().addAll(paymentTableView, rentalTableView);
        this.setCenter(tableBox);
        this.setRight(borderPane);
        Button search = Style.createButton("Search");
        search.setOnAction(e-> handleSearch());
        Button insert = Style.createButton("Insert");
        vBox.getChildren().addAll(search,insert);
        borderPane.setCenter(vBox);
        Button button = new Button("Back");
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10,10,10,10));
        topH.getChildren().add(button);
        borderPane.setTop(topH);
        button.setOnAction(e->{
            borderPane.setCenter(vBox);
        });
        loan.setOnAction(e->{
            downPay();
        });
        total.setOnAction(e->{
            handleInsert();
        });
        insert.setOnAction(e->{
            if(rentalTableView.getSelectionModel().getSelectedItem()!=null){
                borderPane.setCenter(payment);
            }else{
                showAlert("Select A Rental Please");
            }


        });
        this.setBottom(Style.search(searchBar));


    }
    public void handleSearch(){
        GridPane gp = Style.createGridPane();
        TextField tf = Style.createTextField("Enter Amount");
        RadioButton cash = new RadioButton("Cash");
        RadioButton cheque = new RadioButton("Cheque");
        ToggleGroup toggleGroup = new ToggleGroup();
        HBox hBox = Style.createHBox();
        hBox.getChildren().addAll(cash,cheque);
        cash.setToggleGroup(toggleGroup);
        cheque.setToggleGroup(toggleGroup);
        gp.add(Style.createText("Amount"),0,0);
        gp.add(tf,1,0);
        Button button = new Button("+");
        gp.add(button,2,0);
        gp.add(Style.createText("Type"),0,1);
        gp.add(hBox,1,1);
        VBox vBox1 = Style.createVBox();
        vBox1.getChildren().addAll(gp);
        borderPane.setCenter(vBox1);

        cash.setOnAction(e->{
            paymentTableView.getItems().clear();
            paymentTableView.getItems().addAll(paymentDAO.searchPaymentsByPartialType(cash.getText()));
        });
        cheque.setOnAction(e->{
            paymentTableView.getItems().clear();
            paymentTableView.getItems().addAll(paymentDAO.searchPaymentsByPartialType(cheque.getText()));

        });
        button.setOnAction(e->{
            if(tf.getText().isEmpty()){
                showAlert("Enter Amount");
                return;

            }
            if(!Manager.isNum(tf.getText())){
                showAlert("Invalid Amount");
                return;
            }
            paymentTableView.getItems().clear();
            paymentTableView.getItems().addAll(paymentDAO.searchPaymentsByAmount(BigDecimal.valueOf(Double.parseDouble(tf.getText()))));

        });

    }
    void handleSearch(String value) {
        try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all payments
                List<Payment> allPayments = PaymentDAO.getAllPayments();
                if (!allPayments.isEmpty()) {
                    // Set the items of the table view with all payments
                    paymentTableView.getItems().setAll(allPayments);
                    // Select the first item in the table view
                    paymentTableView.getSelectionModel().selectFirst();
                }
            } else {
                // If the search value is not empty, perform partial ID search
                List<Payment> foundPayments = paymentDAO.searchPaymentsByPartialId(Integer.parseInt(value));
                if (!foundPayments.isEmpty()) {
                    // Clear the table view
                    paymentTableView.getItems().clear();
                    // Set the items of the table view with the found payments
                    paymentTableView.getItems().setAll(foundPayments);
                    // Select the first item in the table view
                    paymentTableView.getSelectionModel().selectFirst();
                } else {
                    // If no payments found, show an alert or clear the table
                    paymentTableView.getItems().clear();
                    showAlert("No payments found for the given ID.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid ID for searching.");
        }
    }

    public void handleInsert() {
        Rental rent = rentalTableView.getSelectionModel().getSelectedItem();
        Car car = rentalDAO.selectCarByRentId(rentalTableView.getSelectionModel().getSelectedItem().getRentID());
        Customer customer = rentalDAO.selectCustomerByRentId(rentalTableView.getSelectionModel().getSelectedItem().getRentID());
        int price = car.getCarNo();
        RadioButton cash = new RadioButton("Cash");
        RadioButton cheque = new RadioButton("Cheque");
        Button button = new Button("Pay");
        ToggleGroup toggleGroup = new ToggleGroup();
        cash.setToggleGroup(toggleGroup);
        cheque.setToggleGroup(toggleGroup);
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Payment Method"), 0, 0);
        HBox hBox = new HBox(cash, cheque);
        hBox.setSpacing(10);
        gp.add(hBox, 1, 0);

        Date startDate = Date.valueOf(String.valueOf(LocalDate.now()));
        VBox paymentInsert = Style.createInsertVBox();
        paymentInsert.getChildren().addAll(gp, button);

        button.setOnAction(e -> {
            if(toggleGroup.getSelectedToggle()==null){
                showAlert("Select payment method");
                return;
            }
            String payMethod = null;
            if(cash.isSelected()){
                payMethod = "Cash";

            }
            if(cheque.isSelected()){
                payMethod = "Cheque";
            }
            int days = 0;
            if(rent instanceof DailyRental){
                days = ((DailyRental) rent).getNoOfDays();
            }
            if(rent instanceof WeeklyRental){
                days = ((WeeklyRental) rent).getNoOfWeeks()*7;
            }
            int total = days * price;

            Payment payment = new Payment(total,0,total,payMethod,startDate, customer.getId(), rent.getRentID());


            paymentDAO.insertPayment(payment);
        });
        borderPane.setCenter(paymentInsert);
    }
    public void downPay(){
        TextField amount = Style.createTextField("Enter Amount");
        int id = rentalTableView.getSelectionModel().getSelectedItem().getRentID();
        Rental rent;
        if(RentalDAO.checkRentalType(id) == 1){
            rent = RentalDAO.selectDailyRentalById(id);
        }else{
            rent = RentalDAO.selectWeeklyRentalById(id);
        }
        Car car = rentalDAO.selectCarByRentId(rentalTableView.getSelectionModel().getSelectedItem().getRentID());
        Customer customer = rentalDAO.selectCustomerByRentId(rentalTableView.getSelectionModel().getSelectedItem().getRentID());
        int price = car.getCarNo();

        RadioButton cash = new RadioButton("Cash");
        RadioButton cheque = new RadioButton("Cheque");
        Button button = new Button("Pay");
        ToggleGroup toggleGroup = new ToggleGroup();
        cash.setToggleGroup(toggleGroup);

        cheque.setToggleGroup(toggleGroup);
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Payment Method"), 0, 0);
        HBox hBox = new HBox(cash, cheque);
        hBox.setSpacing(10);
        gp.add(hBox, 1, 0);
        gp.add(Style.createText("Amount"), 0, 1);
        gp.add(amount, 1, 1);
        int days = 0;

        if(rent instanceof DailyRental){
            days = ((DailyRental) rent).getNoOfDays();
        }
        if(rent instanceof WeeklyRental){
            days = ((WeeklyRental) rent).getNoOfWeeks()*7;
        }

        double cost = car.getRentalPrice() * days;
        Date startDate = Date.valueOf(String.valueOf(LocalDate.now()));
        VBox paymentInsert = Style.createInsertVBox();
        paymentInsert.getChildren().addAll(Style.createText("Total: " + cost),gp, button);
        button.setOnAction(e -> {
            if(!Manager.isNum(amount.getText())){
                showAlert("Invalid Amount");
                return;
            }
            if(toggleGroup.getSelectedToggle()==null){
                showAlert("Select payment method");
                return;
            }
            if(amount.getText().isEmpty()){
                showAlert("Enter amount");
                return;

            }
            String payMethod = null;
            if(cash.isSelected()){
                payMethod = "Cash";
            }
            if(cheque.isSelected()){
                payMethod = "Cheque";
            }
            int day = 0;
            if(rent instanceof DailyRental){
                day = ((DailyRental) rent).getNoOfDays();
            }
            if(rent instanceof WeeklyRental){
                day = ((WeeklyRental) rent).getNoOfWeeks()*7;
            }

            int total = day * price;
            int loan =  Integer.parseInt(amount.getText());
            if(loan>total || loan == total){
                showAlert("Invalid Amount");
                return;

            }
            total = total - loan;

            Payment payment = new Payment(total,loan,total,payMethod,startDate, customer.getId(), rent.getRentID());

            paymentDAO.insertPayment(payment);
        });
        borderPane.setCenter(paymentInsert);

    }


    private void insertAction( ToggleGroup toggleGroup) {
        // Check for empty fields
        if (toggleGroup.getSelectedToggle() == null) {
            showAlert("Please fill in all fields.");
            return;
        }
        // All fields are filled, proceed with insertion logic here
        String paymentMethod = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
        System.out.println("Payment Method: " + paymentMethod);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleUpdate(Payment payment) {
        CheckBox[] c = new CheckBox[3];
        TextField amount = Style.createTextField("Enter Amount");
        RadioButton[] paymentMethods = new RadioButton[2];
        paymentMethods[0] = new RadioButton("Cash");
        paymentMethods[1] = new RadioButton("Cheque");

        for (int i = 0; i < c.length; i++) {
            c[i] = new CheckBox();
        }

        Button button = new Button("Update");
        TextField down = Style.createTextField("Down Payment");

        ToggleGroup paymentMethodToggleGroup = new ToggleGroup();
        paymentMethods[0].setToggleGroup(paymentMethodToggleGroup);
        paymentMethods[1].setToggleGroup(paymentMethodToggleGroup);

        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("Amount"), 0, 0);
        gp.add(amount, 1, 0);
        gp.add(Style.createText("Payment Method"), 0, 1);
        HBox paymentMethodHbox = new HBox(paymentMethods);
        paymentMethodHbox.setSpacing(10);
        gp.add(paymentMethodHbox, 1, 1);
        gp.add(Style.createText("Down Payment"), 0, 2);
        gp.add(down, 1, 2);

        for (int i = 0; i < c.length; i++) {
            gp.add(c[i], 2, i);
        }

        VBox updatePayment = Style.createVBox();
        updatePayment.getChildren().addAll(gp, button);

        Stage stage = new Stage();
        Scene scene = new Scene(updatePayment, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update Payment: " + payment.getPaymentID());
        stage.setResizable(false);
        stage.show();

        button.setOnAction(e -> {
            handleUpdate(amount, down, paymentMethodToggleGroup, c, payment);
            stage.close(); // Close the stage after update
        });
    }

    private void handleUpdate(TextField amount, TextField down, ToggleGroup paymentMethodToggleGroup, CheckBox[] c, Payment payment) {
        boolean updated = false;

        // Update amount if checkbox is selected
        if (c[0].isSelected()) {
            if (amount.getText().isEmpty()) {
                showAlert("Enter Amount");
                return;
            }
            if (!Manager.isNum(amount.getText())) {
                showAlert("Invalid Amount Format");
                return;
            }
            if (Double.parseDouble(amount.getText())<payment.getDownPay()) {
                showAlert("Invalid Amount, less than downPAy");
                return;
            }

            payment.setAmount(Integer.parseInt(amount.getText()));
            updated = true;
        }

        // Update payment method if checkbox is selected
        if (c[1].isSelected()) {
            if (paymentMethodToggleGroup.getSelectedToggle() == null) {
                showAlert("Select Payment Method");
                return;
            }
            payment.setPaymentMethod(((RadioButton) paymentMethodToggleGroup.getSelectedToggle()).getText());
            updated = true;
        }

        // Update down payment if checkbox is selected
        if (c[2].isSelected()) {
            if (down.getText().isEmpty()) {
                showAlert("Enter Down Payment");
                return;
            }
            if (!Manager.isNum(down.getText())) {
                showAlert("Invalid Down Payment Format");
                return;
            }
            payment.setDownPay(Double.parseDouble(down.getText()));
            double total = payment.getTotalCost() - payment.getDownPay();
            if (payment.getDownPay() > total) {
                showAlert("Invalid Amount");
                return;
            }
            payment.setTotalCost(total);
            updated = true;
        }

        // If any field is updated, perform the update operation
        if (updated) {
            paymentDAO.updatePayment(payment, payment.getPaymentID());
        } else {
            showAlert("Select at least one field to update.");
        }
    }

    public void handleDelete(int id){
        paymentDAO.deletePayment(id);
    }

}
