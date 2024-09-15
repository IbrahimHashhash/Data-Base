package Layouts;

import ERDClasses.Customer;
import JavaFXClasses.MainScreen;
import JavaFXClasses.Manager;
import JavaFXClasses.Style;
import SqlClass.CustomerDAO;
import SqlClass.DatabaseConnection;
import TableViews.CustomerTableView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CustomerLayout extends BorderPane {
    BorderPane borderPane = Style.borderPane();
    VBox vBox = Style.createVBox();

    Button button = new Button("Back");
    static CustomerDAO customerDAO;

    static {
        try {
            customerDAO = new CustomerDAO(DatabaseConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static CustomerTableView customerTableView = new CustomerTableView();

    public CustomerLayout() throws SQLException {
        TextField searchBar = Style.createTextField("Enter Information");
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue,customerDAO,customerTableView);
        });
        CustomerDAO.getCustomers().clear();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete,update);
        customerTableView.setOnMouseClicked(e -> {
            contextMenu.show(customerTableView, e.getScreenX(), e.getScreenY()); // pops up a menu that contains delete and update when clicking on the table view
        });
        update.setOnAction(e -> { // handing the update logic, it just takes the information from the table view item and it updates it
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();

            handleUpdate(customer);
        });
        delete.setOnAction(e -> {
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            boolean res = Style.showConfirmation("Are you sure you want to delete this item: " + customer.getFirstName() + " " + customer.getLastName() + "\nID: " + customer.getId());
            if(res) {
                handleDelete(customer);
            }
        });
        this.setCenter(customerTableView);
        this.setRight(borderPane);
        Button insert = Style.createButton("Insert");
        insert.setOnAction(e->handleInsert());
        vBox.getChildren().addAll(insert);
        borderPane.setCenter(vBox);
        button.setOnAction(e->{
            borderPane.setCenter(vBox);
        });
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10,10,10,10));
        topH.getChildren().add(button);
        borderPane.setTop(topH);
        this.setBottom(Style.search(searchBar));

    }

    static void handleSearch(String value,CustomerDAO customerDAO,CustomerTableView customerTableView) {
        try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all customers
                List<Customer> allCustomers = customerDAO.getAllCustomers();
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
    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // Method to search customers by ID
    public static void searchCustomersById(int id) {
        try {
            List<Customer> foundCustomers = customerDAO.searchCustomersByPartialId(String.valueOf(id));
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
    public static void searchCustomersByName(String partialName) {
        List<Customer> foundCustomers = customerDAO.searchCustomersByPartialName(partialName);
        System.out.println(customerDAO.searchCustomersByName(partialName).toString());

        customerTableView.getItems().clear(); // Clear the table view
        if (!foundCustomers.isEmpty()) {
            // Set the items of the table view with the found customers
            customerTableView.setItems(FXCollections.observableArrayList(foundCustomers));
            // Select the first item in the table view
            customerTableView.getSelectionModel().selectFirst();
        }
    }
    public void handleInsert() {
        // Create text fields, radio buttons, date picker, and grid pane
        int id = MainScreen.getEmpolyee().getId();
        TextField[] tf = new TextField[5];
        RadioButton[] radioButtons = new RadioButton[3];
        HBox genders = Style.genders(radioButtons);
        DatePicker datePicker = Style.datePicker();
        GridPane gridPane = Style.customerInfo(tf, genders, datePicker);
        VBox vBox = Style.createVBox();

        // Create insert button
        Button button = new Button("Insert");
        button.setOnAction(e -> {
            // Check if any text field is empty
            boolean anyEmpty = false;
            for (TextField textField : tf) {
                if (textField.getText().isEmpty()) {
                    anyEmpty = true;
                    break;
                }
            }

            // Check if any radio button is not selected
            boolean noGenderSelected = true;
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.isSelected()) {
                    noGenderSelected = false;
                    break;
                }
            }

            // Check if date picker value is null
            boolean dateNotSelected = datePicker.getValue() == null;

            // If any field is empty or not selected, show error message
            if (anyEmpty || noGenderSelected || dateNotSelected) {
                alert("Please fill in all fields");
            } else {
                // All fields are filled, proceed with insertion logic here
                // For now, just print the values of the text fields, radio buttons, and date picker
                for (TextField textField : tf) {
                    System.out.println(textField.getText());
                }
                for (RadioButton radioButton : radioButtons) {
                    if (radioButton.isSelected()) {
                        System.out.println(radioButton.getText());
                        break;
                    }
                }
                System.out.println(datePicker.getValue());
            }
            String[] names = tf[0].getText().split(" ");
            if(names.length!=2){
                alert("Enter first name and final name please");
                return;
            }
            String firstName = names[0];
            String lastName = names[1];
            String address = tf[1].getText();
            if(!Manager.isNum(tf[2].getText())){
                alert("Invalid Format");
                return;
            }

            String lic = tf[3].getText();
            if(!Manager.phoneCheck(Integer.parseInt(tf[4].getText()))){
                alert("Phone must be 7 digits");
                return;
            }


            String phone = tf[4].getText();
            int age = Manager.calculateAge(datePicker.getValue());
            if(!Manager.isValidAge(age)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Age must be 18 or above.");
                alert.showAndWait();
                return;
            }
            Date birthDate = Date.valueOf(datePicker.getValue());
            char gen = ' ';
            if(radioButtons[0].isSelected()){
                gen = 'M';
            }else if(radioButtons[1].isSelected()){
                gen = 'F';
            }else if(radioButtons[2].isSelected()){
                gen = 'N';
            }

            Customer customer = new Customer( firstName,  lastName,  age,  address,  phone,  birthDate,  lic,  id, gen);
            if(customerDAO.customerExists(id)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Customer ID already Exists.");
                alert.showAndWait();
            }
            customerDAO.insertCustomer(customer, MainScreen.getEmpolyee());
        });

        // Add grid pane and insert button to the vbox
        vBox.getChildren().addAll(gridPane, button);
        // Set vbox as the center of the border pane

        borderPane.setCenter(vBox);
    }

    public void handleUpdate(Customer customer){
        // Create text fields, radio buttons, date picker, and grid pane
        TextField[] tf = new TextField[5];
        RadioButton[] radioButtons = new RadioButton[3];
        HBox genders = Style.genders(radioButtons);
        DatePicker datePicker = Style.datePicker();
        CheckBox[] c = new CheckBox[7];
        GridPane gridPane = Style.updateCustomer(tf, c,genders, datePicker);
        VBox vBox = Style.createVBox();

        // Create update button
        Button button = new Button("Update");
        button.setOnAction(e -> {
        });
        // Add grid pane and update button to the vbox
        vBox.getChildren().addAll(gridPane, button);
        button.setOnAction(e->{
            int id = customer.getId();
            if(c[0].isSelected()){
                if(tf[0].getText().isEmpty()){
                    alert("Enter Name please");
                    return;
                }
                String[] names = tf[0].getText().split(" ");
                if(names.length==1){
                    customer.setFirstName(names[0]);
                }else if(names.length==2){
                    customer.setFirstName(names[0]);
                    customer.setLastName(names[1]);
                }
            }
            if(c[1].isSelected()){
                if(tf[1].getText().isEmpty()){
                 alert("Enter Address please");
                    return;
                }
                customer.setAddress(tf[1].getText());

            }
            if(c[2].isSelected()){
                if(tf[2].getText().isEmpty()){
                   alert("Enter ID please.");
                    return;
                }
                int idd = Integer.parseInt(tf[2].getText());

                if(!Manager.idCheck(idd)){
                    alert("ID must be 10 Digits");
                    return;
                }

                customer.setId(idd);

            }
            if(c[3].isSelected()){
                char gen;
                if(radioButtons[0].isSelected()){
                    gen = 'M';
                    customer.setGender(gen);
                }else if(radioButtons[1].isSelected()){
                    gen = 'F';
                    customer.setGender(gen);
                }else if(radioButtons[2].isSelected()){
                    gen = 'N';
                    customer.setGender(gen);
                }else{
                 alert("Select Gender pelase.");
                    return;
                }


            }
            if(c[4].isSelected()){
                if(tf[3].getText().isEmpty()){
                   alert("Enter Driving license please");
                    return;
                }
                customer.setLicenseNum(tf[3].getText());
            }
            if(c[5].isSelected()){
                if(tf[4].getText().isEmpty()){
                   alert("Enter Phone please.");
                    return;
                }
                customer.setPhoneNumber(tf[4].getText());

            }
            if(c[6].isSelected()){
                if(datePicker.getValue()==null){
                   alert("Enter Date please.");
                    return;
                }
                int age = Manager.calculateAge(datePicker.getValue());
                if(age<18){
                  alert("Age must be 18 or above");
                    return;
                }
                customer.setBirthDate(Date.valueOf(String.valueOf(datePicker.getValue())));
                customer.setAge(age);


            }
            customerDAO.updateCustomer(customer,id);

        });
        // Set vbox as the center of the border pane
        Stage stage = new Stage();
        Scene scene = new Scene(vBox,480,600);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update: " + customer.getFirstName() + " " + customer.getLastName());
        stage.setResizable(false);
        stage.show();
    }
    public void handleDelete( Customer cus) {
        boolean deletionSuccessful = customerDAO.deleteCustomer(cus.getId());
        if (deletionSuccessful) {
            // Inform the user about successful deletion
            CustomerDAO.getCustomers().remove(cus);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Customer deleted successfully.");
            successAlert.showAndWait();
        } else {
            // Inform the user about deletion failure
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete customer.");
            alert.showAndWait();
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


