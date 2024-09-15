package Layouts;

import ERDClasses.*;
import JavaFXClasses.Manager;
import JavaFXClasses.Style;
import SqlClass.*;
import TableViews.CustomerTableView;
import TableViews.PaymentTableView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanLayOut extends BorderPane {
    BorderPane borderPane = Style.borderPane();
    PaymentDAO paymentDAO = new PaymentDAO(DatabaseConnection.getConnection());
    CustomerTableView customerTableView = new CustomerTableView();
    CustomerDAO customerDAO = new CustomerDAO(DatabaseConnection.getConnection());
    VBox tableBox = Style.createVBox();
    VBox vBox = Style.createVBox();

    public LoanLayOut() throws SQLException {
        TextField searchBar = Style.createTextField("Enter ID");
        searchBar.setAlignment(Pos.CENTER);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue);
        });

        CustomerDAO.getCustomers().clear();
        CustomerDAO.getCustomers().addAll(paymentDAO.getCustomersWithDownPayment());

        this.setStyle("-fx-background-color:white;");
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete, update);
        tableBox.setSpacing(50);
        tableBox.getChildren().addAll(customerTableView);
        this.setCenter(tableBox);
        this.setRight(borderPane);
        Button insert = Style.createButton("Insert");
        vBox.getChildren().addAll(insert);
        borderPane.setCenter(vBox);
        Button button = new Button("Back");
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10, 10, 10, 10));
        topH.getChildren().add(button);
        borderPane.setTop(topH);
        button.setOnAction(e -> {
            borderPane.setCenter(vBox);
        });
        insert.setOnAction(e -> {
            if (customerTableView.getSelectionModel().getSelectedItem() != null) {
                downPay();
            } else {
                showAlert("Select A Rental Please");
            }


        });
        this.setBottom(Style.search(searchBar));


    }
    public void downPay(){
        Payment payment = CustomerDAO.getCustomerPayment(customerTableView.getSelectionModel().getSelectedItem().getId());
        int payId = payment.getPaymentID();
        int carID = paymentDAO.getCarIdByPaymentId(payId);
        int rentID = paymentDAO.getRentalIdByPaymentId(payId);
        int customer_id = paymentDAO.getCustomerIdByPaymentId(payId);
        Rental rent;
        if(RentalDAO.checkRentalType(rentID)==1){
            rent = RentalDAO.selectDailyRentalById(rentID);
        }else{
            rent = RentalDAO.selectWeeklyRentalById(rentID);
        }

        TextField amount = Style.createTextField("Enter new amount");
        Car car = CarDAO.getCarById(carID);
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

            double total = payment.getTotalCost() - Integer.parseInt(amount.getText());
            double newDown = payment.getDownPay() + Integer.parseInt(amount.getText());

            if(newDown>total || newDown == total){
                showAlert("Invalid Amount");
                return;

            }

            Payment newPayment = new Payment(total,newDown,total,payMethod,startDate,customer_id , rent.getRentID());
            paymentDAO.updatePayment(newPayment,payId);
        });
        borderPane.setCenter(paymentInsert);

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    void handleSearch(String value) {
        try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all customers
                List<Customer> allCustomers = paymentDAO.getCustomersWithDownPayment();
                if (!allCustomers.isEmpty()) {
                    // Set the items of the table view with all customers
                    customerTableView.getItems().setAll(allCustomers);
                    // Select the first item in the table view
                    customerTableView.getSelectionModel().selectFirst();
                }
            } else {
                if (isNumeric(value)) {
                    // If the search value is numeric, perform ID search
                    int id = Integer.parseInt(value);
                    searchCustomersById(id);
                } else {
                    // If the search value is not numeric, perform name search
                    searchCustomersByName(value);
                }
            }
        } catch (NumberFormatException e) {
            alert("Please enter a valid ID for searching.");
        }
    }

    // Method to check if a string is numeric
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // Method to search customers by ID
    public void searchCustomersById(int id) {
        try {
            List<Customer> foundCustomers = customerDAO.searchCustomersByPartialIdWithPayments(String.valueOf(id));
            customerTableView.getItems().clear(); // Clear the table view
            if (!foundCustomers.isEmpty()) {
                // Set the items of the table view with the found customers
                customerTableView.getItems().setAll(foundCustomers);
                // Select the first item in the table view
                customerTableView.getSelectionModel().selectFirst();
            }
        } catch (NumberFormatException e) {
            alert("Please enter a valid ID for searching.");
        }
    }

    // Method to search customers by partial name
// Method to search customers by partial name
    public void searchCustomersByName(String partialName) {
        List<Customer> foundCustomers = customerDAO.searchCustomersByPartialNameWithPayments(partialName);

        customerTableView.getItems().clear(); // Clear the table view
        if (!foundCustomers.isEmpty()) {
            // Set the items of the table view with the found customers
            customerTableView.setItems(FXCollections.observableArrayList(foundCustomers));
            // Select the first item in the table view
            customerTableView.getSelectionModel().selectFirst();
        }
    }

    public void handleUpdate(){

    }
    public void alert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();

    }

    public void handleDelete(int id){
        paymentDAO.deletePayment(id);
    }

}
