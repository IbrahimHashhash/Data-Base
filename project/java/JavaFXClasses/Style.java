package JavaFXClasses;


import ERDClasses.Customer;
import ERDClasses.Employee;
import ERDClasses.Login;
import SqlClass.DatabaseConnection;
import SqlClass.EmployeeDAO;
import SqlClass.LoginDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Style {
    private static final ObservableList<Customer>  CUSTOMERS = FXCollections.observableArrayList();

    public static TextField getLocation() {
        TextField tf = new TextField();
        tf.setPromptText("Enter Address");
        style(tf);
        return tf;
    }

    public static Text createText(String s){
        Text text = new Text(s);
        text.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:13;");
        Color color = Color.rgb(251, 80, 1);
        text.setFill(color);
        return text;
    }
    public static TextField createTextField(String prompt){
        TextField tf = new TextField();
        tf.setMaxWidth(180);
        tf.setPromptText(prompt);
        style(tf);
        return tf;
    }
    public static PasswordField createPassword(String prompt){
        PasswordField tf = new PasswordField();
        tf.setMaxWidth(180);
        tf.setPromptText(prompt);
        style(tf);
        return tf;
    }

    public static HBox genders(RadioButton[] radioButtons){
        String[] label = {"Male","Female","Other"};
        for(int i=0;i<radioButtons.length;i++){
            radioButtons[i] = new RadioButton();
            radioButtons[i].setText(label[i]);
        }
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtons[0].setToggleGroup(toggleGroup);
        radioButtons[1].setToggleGroup(toggleGroup);
        radioButtons[2].setToggleGroup(toggleGroup);
        HBox hb = Style.createHBox();
        hb.getChildren().addAll(radioButtons[0],radioButtons[1],radioButtons[2]);
        return hb;

    }

    public static ObservableList<Customer> getCustomers() {
        return CUSTOMERS;
    }

    public static GridPane personInfo(TextField[] tf,HBox genders, DatePicker datePicker) {
        GridPane gp = new GridPane();
        for(int i=0;i<tf.length;i++){
            tf[i] = new TextField();
            Style.style(tf[i]);
        }
        String[] labels = {"Name","Address","ID", "BirthDate","Gender","Driving License","Phone","Password"};
        datePicker.setPromptText("MM - DD - YYYY");
        for (int i = 0; i < labels.length; i++) {
            gp.add(createText(labels[i]), 0, i);
        }
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(30);
        tf[0].setPromptText("Enter Name");
        tf[1].setPromptText("Enter Address");
        tf[2].setPromptText("Enter ID");
        tf[3].setPromptText("Enter Driving License");
        tf[4].setPromptText("Enter Phone");
        tf[5].setPromptText("Enter Password");
        gp.add(tf[0], 1, 0); // Name TextField
        gp.add(tf[1], 1, 1); // Age TextField
        gp.add(tf[2], 1, 2); // Name TextField
        gp.add(datePicker, 1, 3); // Age TextField
        gp.add(genders, 1, 4); // Gender ComboBox
        gp.add(tf[3], 1, 5); // Date DatePicker
        gp.add(tf[4], 1, 6); // District ComboBox
        gp.add(tf[5], 1, 7); // District ComboBox
        gp.add(tf[6], 1, 8); // District ComboBox
        return gp;
    }
    public static GridPane customerInfo(TextField[] tf,HBox genders,DatePicker datePicker) {
        GridPane gp = new GridPane();
        for(int i=0;i<tf.length;i++){
            tf[i] = new TextField();
            Style.style(tf[i]);
        }
        String[] labels = {"Name","Address","ID","Gender","Driving License","Phone","BirthDate"};
        datePicker.setPromptText("MM - DD - YYYY");
        for (int i = 0; i < labels.length; i++) {
            gp.add(createText(labels[i]), 0, i);
        }
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(30);
        tf[0].setPromptText("Enter Name");
        tf[1].setPromptText("Enter Address");
        tf[2].setPromptText("Enter ID");
        tf[3].setPromptText("Enter Driving License");
        tf[4].setPromptText("Enter Phone");
        gp.add(tf[0], 1, 0);
        gp.add(tf[1], 1, 1);
        gp.add(tf[2], 1, 2); //
        gp.add(genders, 1, 3);
        gp.add(tf[3], 1, 4);
        gp.add(tf[4], 1, 5);
        gp.add(datePicker,1,6);

        return gp;
    }
    public static GridPane updateCustomer(TextField[] tf,CheckBox[] checkBoxes,HBox genders,DatePicker datePicker) {
        GridPane gp = new GridPane();
        for(int i=0;i<tf.length;i++){
            tf[i] = new TextField();
            Style.style(tf[i]);
        }
        String[] labels = {"Name","Address","ID","Gender","Driving License","Phone","BirthDate"};
        datePicker.setPromptText("MM - DD - YYYY");
        for (int i = 0; i < labels.length; i++) {
            checkBoxes[i] = new CheckBox();
            gp.add(createText(labels[i]), 0, i);
            gp.add(checkBoxes[i],2,i);
        }
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(30);
        tf[0].setPromptText("Enter Name");
        tf[1].setPromptText("Enter Address");
        tf[2].setPromptText("Enter ID");
        tf[3].setPromptText("Enter Driving License");
        tf[4].setPromptText("Enter Phone");
        gp.add(tf[0], 1, 0);
        gp.add(tf[1], 1, 1);
        gp.add(tf[2], 1, 2); //
        gp.add(genders, 1, 3);
        gp.add(tf[3], 1, 4);
        gp.add(tf[4], 1, 5);
        gp.add(datePicker,1,6);

        return gp;
    }



    public static VBox createSideButtons(Button[] bt){
        VBox vBox = new VBox();
        String[] label = {"User","Dashboard","Customer","Storage","Loan","Payment","Rents","Car","Employee","Reports"};
        for(int i=0;i<bt.length;i++){
            bt[i] = new Button(label[i]);
            bt[i].setPrefSize(200,98);
            bt[i].setOpacity(0.73);
            styleMenu(bt[i]);
            vBox.getChildren().add(bt[i]);
        }
        // bt[0].setText(MainScreen.getEmpolyee().getFirstName());
        vBox.setSpacing(2);


        return vBox;

    }
    public static void handleLogin() throws SQLException {
        Employee emp = MainScreen.getEmpolyee();
        // ..............................
        BorderPane borderPane = new BorderPane();
        VBox updateBox = Style.createVBox();
        Button button = new Button("back");
        borderPane.setPadding(new Insets(10,10,10,10));
        borderPane.setTop(button);

        VBox original = Style.createVBox();
        Label changePassword = new Label(" Change Password ");
        changePassword.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:20;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1);");

            borderPane.setCenter(original);

        borderPane.setCenter(updateBox);
        updateBox.getChildren().addAll(changePassword,handleUpdatePass());
        button.setOnAction(e->{
            borderPane.setCenter(original);
        });

        Button logOut = new Button("Log out");
        Label title = new Label("  Login Settings  ");
        title.setStyle("-fx-text-fill: white;-fx-background-color:rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:20;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1)");
        changePassword.setStyle("-fx-text-fill: white;-fx-background-color:rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:20;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1)");

        Label names = new Label(" Employee: " + emp.getFirstName() + " " + emp.getLastName() +" ");
        Label id = new Label("    Employee ID: " + emp.getId() +"   ");
        id.setStyle("-fx-text-fill: white;-fx-background-color:rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:15;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1)");
        names.setStyle("-fx-text-fill: white;-fx-background-color:rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:15;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1)");

        Button update = new Button("Update");
        borderPane.setCenter(original);
        original.getChildren().addAll(title,names,id,update,logOut);
        update.setOnAction(e1->{
            borderPane.setCenter(updateBox);
        });
        Stage stage = new Stage();
        Scene scene = new Scene(borderPane,400,400);
        scene.getStylesheets().add(Style.class.getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Login Settings");
        stage.show();
        logOut.setOnAction(e1->{
            MainScreen.getStage1().close();
            try {
                MainScreen mainScreen = new MainScreen();
                Stage work = new Stage();
                mainScreen.start(work);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            stage.close();
        });

    }

    public static GridPane handleUpdatePass() throws SQLException {
        LoginDAO loginDAO = new LoginDAO(DatabaseConnection.getConnection());
        GridPane gp2 = Style.createGridPane();
        TextField oldp = createPassword("Old Password");
        TextField newp = createTextField("New Password");

        gp2.add(createText("Old Password"),0,0);
        gp2.add(createText("New Password"),0,1);
        gp2.add(oldp,1,0);
        gp2.add(newp,1,1);
        Button bt = new Button("Update");
        gp2.add(bt,0,2);

        bt.setOnAction(e->{
            if(oldp.getText().isEmpty() || newp.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All Fields are required");
                alert.showAndWait();
                return;
            }

            if(loginDAO.passwordExists(MainScreen.getEmpolyee().getId(),oldp.getText())){
                loginDAO.updatePassword(MainScreen.getEmpolyee().getId(),newp.getText());
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong Old Password");
                alert.showAndWait();
            }
        });
        return gp2;
    }
    public static GridPane employeeInfo(TextField[] tf,HBox genders, DatePicker datePicker,CheckBox c) {
        GridPane gp = new GridPane();
        for(int i=0;i<tf.length;i++){
            tf[i] = new TextField();
            Style.style(tf[i]);
        }

        String[] labels = {"Name","Address","ID", "BirthDate","Gender","Phone","Password"};
        datePicker.setPromptText("MM - DD - YYYY");
        for (int i = 0; i < labels.length; i++) {
            gp.add(createText(labels[i]), 0, i);
        }

        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(15);
        tf[0].setPromptText("Enter Name");
        tf[1].setPromptText("Enter Address");
        tf[2].setPromptText("Enter ID");
        tf[3].setPromptText("Enter Phone");
        tf[4].setPromptText("Enter Password");
        gp.add(tf[0], 1, 0); // Name TextField
        gp.add(tf[1], 1, 1); // Age TextField
        gp.add(tf[2], 1, 2); // Name TextField
        gp.add(datePicker, 1, 3); // Age TextField
        gp.add(genders, 1, 4); // Gender ComboBox
        gp.add(tf[3], 1, 5); // Date DatePicker
        gp.add(tf[4], 1, 6); // District ComboBox
        gp.add(c, 0, 7); // District ComboBox
        return gp;
    }
    public static GridPane updateEmployeeInfo(TextField[] tf,HBox genders, DatePicker datePicker,CheckBox[] checkBoxes) {
        GridPane gp = new GridPane();
        for(int i=0;i<tf.length;i++){
            tf[i] = new TextField();
            Style.style(tf[i]);
        }
        String[] labels = {"Name","Address","ID", "BirthDate","Gender","Phone","Password"};
        datePicker.setPromptText("MM - DD - YYYY");
        for (int i = 0; i < labels.length; i++) {
            gp.add(createText(labels[i]), 0, i);
            checkBoxes[i] = new CheckBox();
            gp.add(checkBoxes[i], 2, i);
        }

        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(15);
        tf[0].setPromptText("Enter Name");
        tf[1].setPromptText("Enter Address");
        tf[2].setPromptText("Enter ID");
        tf[3].setPromptText("Enter Phone");
        tf[4].setPromptText("Enter Password");
        gp.add(tf[0], 1, 0); // Name TextField
        gp.add(tf[1], 1, 1); // Age TextField
        gp.add(tf[2], 1, 2); // Name TextField
        gp.add(datePicker, 1, 3); // Age TextField
        gp.add(genders, 1, 4); // Gender ComboBox
        gp.add(tf[3], 1, 5); // Date DatePicker
        gp.add(tf[4], 1, 6); // District ComboBox
        return gp;
    }
    public static GridPane createGridPane(){
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(20);
        gp.setHgap(10);
        return gp;
    }
    public VBox navigate(){
        return new VBox();
    }

    public static HBox search(TextField tf){
        HBox hBox = Style.createHBox();
         hBox.setStyle("fx-background-color:white;");
         hBox.setAlignment(Pos.BOTTOM_LEFT);
         hBox.getChildren().add(tf);
         hBox.setPadding(new Insets(10,10,10,360));
        return hBox;
    }

    public static DatePicker datePicker(){
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("DD " + " MM " + " YYYY ");
        style(datePicker);
        return datePicker;
    }

    public static HBox createHBox(){
        HBox hb = new HBox();
        hb.setSpacing(6);
        hb.setAlignment(Pos.CENTER);
        return hb;
    }
    public static VBox createVBox(){
        VBox vBox = new VBox();
        vBox.setSpacing(40);
        vBox.setStyle("-fx-background-color:white;");
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
    public static VBox createInsertVBox(){
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public static BorderPane borderPane(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(480);
        borderPane.setStyle("-fx-background-color:white;");
        return borderPane;
    }
    public static VBox help(){
        VBox vBox = new VBox();
        TextArea ta = new TextArea();
        ta.setEditable(false);
        String instructions = """
                                                                 """;
        ta.setText(instructions);
        ta.setStyle("-fx-text-fill:black;-fx-font-size:13.5px;");
        vBox.getChildren().add(ta);
        vBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(ta, Priority.ALWAYS);
        return vBox;
    }
    public static FlowPane checkBoxes(CheckBox[] cb,String[] label){
        FlowPane fp = new FlowPane();
        for(int i = 0; i < cb.length; i++){
            cb[i] = new CheckBox(label[i]);
            cb[i].setMinWidth(60); // Set a fixed width for the checkboxes
            fp.getChildren().add(cb[i]);
        }
        fp.setHgap(10); // Adjust spacing as needed
        fp.setAlignment(Pos.CENTER);
        fp.setVgap(10);
        return fp;
    }

    public static HBox createColorBox(RadioButton[] radioButtons) {
        HBox colorBox = new HBox(30);
        colorBox.setPadding(new Insets(20));
        colorBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        ToggleGroup toggleGroup = new ToggleGroup();

        radioButtons[0] = new RadioButton("Red");
        radioButtons[1] = new RadioButton("Blue");
        radioButtons[2] = new RadioButton("White");
        radioButtons[3] = new RadioButton("Black");
        radioButtons[0].setToggleGroup(toggleGroup);
        radioButtons[1].setToggleGroup(toggleGroup);
        radioButtons[2].setToggleGroup(toggleGroup);
        radioButtons[3].setToggleGroup(toggleGroup);

        colorBox.getChildren().addAll(radioButtons[0], radioButtons[1], radioButtons[2], radioButtons[3]);

        return colorBox;
    }

    public static boolean showConfirmation(String message) {
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Adding custom buttons
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show and wait for user response
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.showAndWait();

        // Return true if user clicks Yes, false otherwise
        return alert.getResult() == yesButton;
    }

    public static Text dashText(String s){
        Text text = new Text(s);
        text.setStyle("-fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:13;");
        text.setFill(Color.WHITE);
        return text;
    }


    public static void style(Node x) {
        if (x instanceof TextField) {
            x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            x.setOnMouseMoved(e -> {
                x.setStyle("-fx-background-color: rgba(226, 72, 1, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseClicked(e -> {
                x.setStyle("-fx-background-color: rgba(176, 56, 1, 1); -fx-border-color: rgba(0, 0, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseExited(e -> {
                x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });

        } else if(x instanceof DatePicker){
            x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; ");
            x.setOnMouseMoved(e -> {
                x.setStyle("-fx-background-color: rgba(226, 72, 1, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS';");
            });
            x.setOnMouseClicked(e -> {
                x.setStyle("-fx-background-color: rgba(176, 56, 1, 1); -fx-border-color: rgba(0, 0, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS';");
            });
            x.setOnMouseExited(e -> {
                x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS';");
            });

        } else

    {
        if(x instanceof Button) {
            x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:20; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            x.setOnMouseMoved(e -> {
                x.setStyle("-fx-background-color: rgba(226, 72, 1, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:20; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseClicked(e -> {
                x.setStyle("-fx-background-color: rgba(176, 56, 1, 1); -fx-border-color: rgba(0, 0, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:20; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseExited(e -> {
                x.setStyle("-fx-background-color: rgba(251, 80, 1);-fx-border-color:black; -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:20; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
        }else{
            x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            x.setOnMouseMoved(e -> {
                x.setStyle("-fx-background-color: rgba(226, 72, 1, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseClicked(e -> {
                x.setStyle("-fx-background-color: rgba(176, 56, 1, 1); -fx-border-color: rgba(0, 0, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });
            x.setOnMouseExited(e -> {
                x.setStyle("-fx-background-color: rgba(251, 80, 1);-fx-border-color:black; -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;-fx-border-radius: 50;");
            });

        }
    }
    }
    public static void styleMenu(Node x) {
        x.setOpacity(0.73);
        x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:25; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 0;");
        x.setOnMouseMoved(e -> {
            x.setStyle("-fx-background-color: rgba(226, 72, 1, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:25; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 0;");
            x.setOpacity(1);
        });
        x.setOnMouseClicked(e -> {
            x.setStyle("-fx-background-color: rgba(176, 56, 1, 1); -fx-border-color: rgba(0, 0, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:25; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 0;");
            x.setOpacity(1);
        });
        x.setOnMouseExited(e -> {
            x.setStyle("-fx-background-color: rgba(251, 80, 1); -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:25; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 0;");
            x.setOpacity(0.73);
        });
    }

    public static void styleCar(Node x, Color color) {
        String colorString = String.format("rgba(%d, %d, %d, %f)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255), color.getOpacity());
        x.setStyle("-fx-background-color: " + colorString + "; -fx-border-color: rgba(255, 140, 0, 0); -fx-text-fill: rgba(255, 255, 255, 1); -fx-font-size:25; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 0;");
        x.setOpacity(0.73);
        x.setOnMouseMoved(e -> {
            x.setOpacity(1);
        });
        x.setOnMouseClicked(e -> {
            x.setOpacity(1);
        });

        x.setOnMouseExited(e -> {
            x.setOpacity(0.73);
        });
    }

    public static void styleDash(Node x,String color) {
        x.setOpacity(0.73);
        x.setStyle("-fx-background-color: " + color + "; " + " -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;");
        x.setOnMouseMoved(e -> {
            x.setStyle("-fx-background-color: " + color + "; " + " -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;");
            x.setOpacity(1);
        });
        x.setOnMouseClicked(e -> {
            x.setStyle("-fx-background-color: " + color + "; " + " -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;");
            x.setOpacity(1);
        });
        x.setOnMouseExited(e -> {
            x.setStyle("-fx-background-color: " + color + "; " + " -fx-font-size:13; -fx-font-family: 'Trebuchet MS'; -fx-background-radius: 50;");
            x.setOpacity(0.73);
        });
    }
    public static void styleChart(Node x) {
        x.setOpacity(0.73);
        x.setOnMouseMoved(e -> {
            x.setOpacity(1);
        });
        x.setOnMouseClicked(e -> {
            x.setOpacity(1);
        });
        x.setOnMouseExited(e -> {
            x.setOpacity(0.73);
        });
    }
    public static Button createButton(String text){
        Button button = new Button(text);
        style(button);
        button.setPrefSize(180,70);
        return button;
    }


    public static ImageView backgroundImage(String file) {
        Image image = new Image(file);
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    public static ImageView title(){
        Image image = new Image("C:/Users/ibrah/Downloads/800px-Payless_Car_Rental_logo.svg.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(240);
        return imageView;
    }
}
