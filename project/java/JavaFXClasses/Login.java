package JavaFXClasses;

import ERDClasses.Employee;
import SqlClass.DatabaseConnection;
import SqlClass.EmployeeDAO;
import SqlClass.LoginDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Login extends BorderPane {
    Button bt1 = new Button("Log in");
    TextField ID = Style.createTextField("Enter ID");
    PasswordField passwordField = Style.createPassword("Enter Password");
    SidePane sidePane;
    Button bt2 = new Button("Sign Up");
    Button bt3 = new Button("Exit");
    Label label = new Label();
    VBox mainLogin = Style.createVBox();
    LoginDAO loginDAO = new LoginDAO(DatabaseConnection.getConnection());
    EmployeeDAO employeeDAO = new EmployeeDAO(DatabaseConnection.getConnection());

    public Login() throws SQLException {
        sidePane = new SidePane();
        Stage stage = new Stage();
        GridPane gp = Style.createGridPane();
        gp.add(Style.createText("ID"),0,0);
        gp.add(ID,1,0);
        gp.add(Style.createText("Password"),0,1);
        gp.add(passwordField,1,1);
        mainLogin.setSpacing(25);
        Label text = new Label("LOG IN");
        text.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:40;");
        Color color = Color.rgb(251, 80, 1);
        text.setTextFill(color);
        bt1.setPrefWidth(100);
        bt2.setPrefWidth(100);
        bt3.setPrefWidth(100);
        text.setAlignment(Pos.CENTER);
        ImageView imageView = Style.backgroundImage("C:/Users/ibrah/Downloads/images (2).jpg");
        imageView.setFitWidth(200);
        imageView.fitHeightProperty().bind(this.heightProperty());
        this.setCenter(mainLogin);
        this.setLeft(imageView);
        bt2.setOnAction(e->{
           checkManager();
        });

        mainLogin.getChildren().addAll(text,gp,bt1,bt2,bt3,label);
        gp.setPadding(new Insets(10,10,10,10));
        mainLogin.setPadding( new Insets(10,10,10,10));


    }
    public void handleSignIn(){
            label.setText(null);
                TextField[] tf = new TextField[5];
                DatePicker datePicker = Style.datePicker();
                RadioButton[] g = new RadioButton[3];
                CheckBox c = new CheckBox("Manager");
                HBox gender = Style.genders(g);
                GridPane gp = Style.employeeInfo(tf, gender, datePicker, c);
                VBox vBox = Style.createVBox();
                HBox hb = Style.createHBox();
                Button cancel = new Button("Cancel");
                Button button = new Button("Sign Up");
                hb.getChildren().addAll(button, cancel);
                hb.setSpacing(10);
                vBox.getChildren().addAll(gp, hb, label);
                vBox.setSpacing(20); // right,
                vBox.setPadding(new Insets(20, 20, 20, 20));


                button.setOnAction(e1 -> {
                    boolean isEmpty = areFieldsEmpty(tf) || isDatePickerEmpty(datePicker);
                    if (isEmpty) {
                        label.setText("One or more fields are empty.");
                        return;
                    }
                    char gen;
                    if (g[0].isSelected()) {
                        gen = 'M';
                    } else if (g[1].isSelected()) {
                        gen = 'F';
                    } else if (g[2].isSelected()) {
                        gen = 'N';
                    } else {
                        label.setText("Select gender");
                        return;
                    }
                    String[] names = tf[0].getText().split(" ");
                    if (names.length != 2) {
                        label.setText("Enter First Name and Full Name");
                        return;
                    }
                    String firstName = names[0];
                    String lastName = names[1];
                    int id = Integer.parseInt(tf[2].getText());
                    int age = Manager.calculateAge(datePicker.getValue());
                    if (!Manager.isValidAge(age)) {
                        label.setText("Invalid Age");
                        return;
                    }
                    String address = tf[1].getText();
                    String phoneNumber = tf[3].getText();
                    String password = tf[4].getText();
                    Date birthDate = Date.valueOf(datePicker.getValue());
                    Employee employee = new Employee(firstName, lastName, age, address, phoneNumber, birthDate, id, gen);
                    ERDClasses.Login login = new ERDClasses.Login(id, password);
                    if (employeeDAO.searchEmployeeById(id)) {
                        label.setText("Employee ID already exists");
                        return;

                    }
                    if (c.isSelected()) {
                        employeeDAO.insertEmployee(employee, employee.getId());
                    } else {
                        employeeDAO.insertEmployee(employee);
                    }

                    loginDAO.insertLogin(login);
                    for (TextField textField : tf) {
                        textField.clear();
                    }
                    label.setText("Employee has been inserted successfully");
                });
                cancel.setOnAction(e1 -> {
                    this.setCenter(mainLogin);
                });
                this.setCenter(vBox);

    }
    public void checkManager(){
        GridPane gp = Style.createGridPane();
        TextField id = Style.createTextField("Enter ID");
        PasswordField passwordField = Style.createPassword("Enter Password");
        gp.add(Style.createText("Manager ID: "),0,0);
        gp.add(id,1,0);
        gp.add(Style.createText("Password: "),0,1);
        gp.add(passwordField,1,1);
        Stage stage = new Stage();
        Button button = new Button("Check");
        VBox vBox = Style.createVBox();
        vBox.getChildren().addAll(gp,button);
        Scene scene = new Scene(vBox,400,200);
        stage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        button.setOnAction(e->{
            if(id.getText().isEmpty() || passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All Fields are required");
                alert.showAndWait();
                return;
            }
            boolean managerWithLogin = loginDAO.isManagerWithLogin(Integer.parseInt(id.getText()), passwordField.getText());
            if(managerWithLogin) {

                handleSignIn();
                stage.close(); // Close the stage after successful login
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid ID or password.");
                alert.showAndWait();
            }
        });
        // Return the value of isManager after the button click
    }
    private boolean areFieldsEmpty(TextField[] textFields) {
        for (TextField tf : textFields) {
            if (tf.getText().isEmpty()) {
                return true; // Found an empty text field
            }
        }
        return false; // All text fields are filled
    }

    // Method to check if the date picker is empty
    private boolean isDatePickerEmpty(DatePicker datePicker) {
        return datePicker.getValue() == null;
    }

    public Button getBt1() {
        return bt1;
    }

    public Button getBt3() {
        return bt3;
    }

    public Label getLabel() {
        return label;
    }

    public TextField getID() {
        return ID;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }
}
