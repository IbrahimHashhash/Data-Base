package Layouts;

import ERDClasses.Employee;
import ERDClasses.Login;
import JavaFXClasses.MainScreen;
import JavaFXClasses.Manager;
import JavaFXClasses.SidePane;
import JavaFXClasses.Style;
import SqlClass.DatabaseConnection;
import SqlClass.EmployeeDAO;
import SqlClass.LoginDAO;
import TableViews.EmployeeTableView;
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

public class EmployeeLayout extends BorderPane {
    BorderPane borderPane = Style.borderPane();
    VBox vBox = Style.createVBox();
    Button search;

    LoginDAO loginDAO = new LoginDAO(DatabaseConnection.getConnection());
    EmployeeDAO employeeDAO = new EmployeeDAO(DatabaseConnection.getConnection());
    EmployeeTableView employeeTableView = new EmployeeTableView();
    public EmployeeLayout() throws SQLException {
        TextField searchBar = Style.createTextField("Enter ID");
        searchBar.setAlignment(Pos.CENTER);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue);
        });
        System.out.println("Is " + MainScreen.getEmpolyee().getFirstName() + " Manager: " + employeeDAO.isEmployeeManager(MainScreen.getEmpolyee().getId()));

        EmployeeDAO.getEmployees().clear();
        EmployeeDAO.getEmployees().addAll(EmployeeDAO.getAllEmployees());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem update = new MenuItem("Update");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(delete,update);
        employeeTableView.setOnMouseClicked(e -> {
            contextMenu.show(employeeTableView, e.getScreenX(), e.getScreenY()); // pops up a menu that contains delete and update when clicking on the table view
        });
        update.setOnAction(e -> {
            Employee employee = employeeTableView.getSelectionModel().getSelectedItem();

            handleUpdate(employee);
        });
        delete.setOnAction(e -> {
            Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
            boolean res = Style.showConfirmation("Are you sure you want to delete this item: " + employee.getFirstName() + " " + employee.getLastName() + "\nID: " + employee.getId());
            if(res){
                handleDelete(employee.getId());

            }
        });

        this.setCenter(employeeTableView);
        Button search = Style.createButton("Search");
        Button insert = Style.createButton("Insert");
        vBox.getChildren().addAll(search,insert);
        borderPane.setCenter(vBox);
        this.setRight(borderPane);
        Button back = new Button("Back");
        back.setOnAction(e->{
            borderPane.setCenter(vBox);
        });
        insert.setOnAction(e->{
            handleInsert();
        });
        HBox topH = new HBox();
        topH.setSpacing(10);
        topH.setAlignment(Pos.BASELINE_RIGHT);
        topH.setPadding(new Insets(10,10,10,10));
        topH.getChildren().add(back);
        borderPane.setTop(topH);

        this.setBottom(Style.search(searchBar));
    }

    void handleSearch(String value) {
        try {
            if (value.isEmpty()) {
                // If the search value is empty, retrieve all customers
                List<Employee> allEmployees = EmployeeDAO.getAllEmployees();
                if (!allEmployees.isEmpty()) {
                    // Set the items of the table view with all customers
                    employeeTableView.getItems().setAll(allEmployees);
                    // Select the first item in the table view
                    employeeTableView.getSelectionModel().selectFirst();
                }
            } else {
                // If the search value is not empty, perform partial ID search
                List<Employee> foundEmployees = employeeDAO.searchEmployeesByPartialId(Integer.parseInt(value));
                employeeTableView.getItems().clear(); // Clear the table view
                if (!foundEmployees.isEmpty()) {
                    // Set the items of the table view with the found customers
                    employeeTableView.getItems().setAll(foundEmployees);
                    // Select the first item in the table view
                    employeeTableView.getSelectionModel().selectFirst();
                }
            }
        } catch (NumberFormatException e) {
            alert("Please enter a valid ID for searching.");
        }
    }

    public void handleInsert() {
        // Create text fields, radio buttons, date picker, and grid pane
        TextField[] tf = new TextField[5];
        DatePicker datePicker = Style.datePicker();
        RadioButton[] radioButtons = new RadioButton[3];
        HBox genders = Style.genders(radioButtons);
        CheckBox checkBox = new CheckBox("Manager");
        GridPane gp = Style.employeeInfo(tf, genders, datePicker,checkBox);
        gp.setVgap(30);
        VBox insertEmp = Style.createInsertVBox();
        // Create insert button
        Button button = new Button("Insert");
        insertEmp.getChildren().addAll(gp,button);
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
                alert("Please Fill in All Fields");
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
            String fname = names[0];
            String lname = names[1];
            String address = tf[1].getText();
            if (!Manager.isNum(tf[2].getText())) {
                alert("Invalid Format");
                return;
            }
            int age = Manager.calculateAge(datePicker.getValue());
            if (age < 18) {
                alert("Age must be 18 or above");
                return;
            }
            char gen;
            if (radioButtons[0].isSelected()) {
                gen = 'M';
            } else if (radioButtons[1].isSelected()) {
                gen = 'F';
            } else if (radioButtons[2].isSelected()) {
                gen = 'N';
            } else {
                alert("Select Gender please");
                return;
            }

            int id = Integer.parseInt(tf[2].getText());
            String password = tf[4].getText();

            if(!Manager.isNum(tf[2].getText())){
                alert("Invalid Phone format");
                return;

            }
            if(!Manager.phoneCheck(Integer.parseInt(tf[2].getText()))){
                alert("Phone must be 7 digits");
                return;

            }

            String phone = tf[3].getText();
            Employee employee = new Employee(fname, lname, age, address, phone, Date.valueOf(datePicker.getValue()), id, gen);
            if (checkBox.isSelected()) {
                employeeDAO.insertEmployee(employee, employee.getId());
                Login login = new Login(employee.getId(), password);
                loginDAO.insertLogin(login);
            } else {
                employeeDAO.insertEmployee(employee);
                Login login = new Login(employee.getId(), password);
                loginDAO.insertLogin(login);
            }
            // Set insertEmp as the center of the border pane
        });
        borderPane.setCenter(insertEmp);
    }
    public void handleUpdate(Employee employee){
        TextField[] tf = new TextField[5];
        DatePicker datePicker = Style.datePicker();
        RadioButton[] radioButtons = new RadioButton[3];
        HBox genders = Style.genders(radioButtons);
        CheckBox[] c = new CheckBox[7];
        GridPane gp = Style.updateEmployeeInfo(tf, genders, datePicker,c);
        VBox updateEmp = Style.createInsertVBox();
        updateEmp.setSpacing(40);
        gp.setVgap(20);
        Button button = new Button("Update");
        updateEmp.getChildren().addAll(gp,button);
        Stage stage = new Stage();
        Scene scene = new Scene(updateEmp,480,600);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update: " + employee.getFirstName() + " " + employee.getLastName());
        stage.setResizable(false);
        stage.show();
        button.setOnAction(e->{
            handleUpdate( tf, datePicker, radioButtons, c, employee);
        });
    }
    public void handleUpdate(TextField[] tf,DatePicker datePicker,RadioButton[] radioButtons,CheckBox[] c,Employee employee){
        if(c[0].isSelected()){
            if(tf[0].getText().isEmpty()){
                alert("Enter Name please");
                return;
            }
            String[] names = tf[0].getText().split(" ");
            if(names.length!=2){
                alert("Enter Both First And Last Name");
                return;
            }

            employee.setFirstName(names[0]);
            employee.setLastName(names[1]);
            SidePane.bt[0].setText(names[0]);
        }
        if(c[1].isSelected()){
            if(tf[1].getText().isEmpty()){
                alert("Enter Address please");
            }
            employee.setAddress(tf[1].getText());

        }
        if(c[2].isSelected()){
            if(tf[2].getText().isEmpty()){
                alert("Enter ID please");
                return;
            }
            if(!Manager.isNum(tf[2].getText())){
                alert("Invalid Format");
                return;
            }
            if(!Manager.idCheck(Integer.parseInt(tf[2].getText()))){
                alert("ID must be 10 digits");
                return;

            }

                employee.setId(Integer.parseInt(tf[2].getText()));

        }
        if(c[3].isSelected()){
            if(datePicker.getValue()==null){
                alert("Enter Date please");
                return;
            }
            int age = Manager.calculateAge(datePicker.getValue());
            if(!Manager.isValidAge(age)){
                alert("Enter Valid Age please");
                return;
            }
            employee.setBirthDate(Date.valueOf(String.valueOf(datePicker.getValue())));
            employee.setAge(age);

        }
        if(c[4].isSelected()){
            char gen;
            if(radioButtons[0].isSelected()){
                gen = 'M';
            }else if(radioButtons[1].isSelected()){
                gen = 'F';
            }else if(radioButtons[2].isSelected()){
                gen = 'N';
            }else{
                alert("Select Gender please");
                return;
            }
            employee.setGender(gen);

        }
        if(c[5].isSelected()){
            if(tf[3].getText().isEmpty()){
                alert("Enter Phone please");
            }
            employee.setPhoneNumber(tf[3].getText());
        }
        employeeDAO.updateEmployee(employee);
        if(c[6].isSelected()){
            if(tf[4].getText().isEmpty()){
                alert("Enter Password please");
                return;
            }
            if(!Manager.isStrongPassword(tf[4].getText())){
                alert("Weak Password: " + "\nPassword must contain at least one uppercase letter, one lowercase letter, and one digit.");
                return;
            }

            loginDAO.updatePassword(employee.getId(),tf[4].getText());

        }
    }

    public void alert(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();

    }

    public void handleDelete(int id){
        employeeDAO.deleteEmployee(id);
    }

}
