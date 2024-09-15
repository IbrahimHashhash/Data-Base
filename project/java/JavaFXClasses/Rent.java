package JavaFXClasses;

import ERDClasses.Customer;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Rent {
    private static final Label label = new Label();
    Connection connection;
    public Rent(Connection connection){
        this.connection=connection;
    }
    public static VBox handleCustomer(){
        label.setText(null);
        TextField[] tf = new TextField[7];
        DatePicker datePicker = Style.datePicker();
         RadioButton[] g = new RadioButton[3];
        HBox hBox = Style.genders(g);
        GridPane gp = Style.personInfo(tf, hBox, datePicker);
        VBox vBox = Style.createVBox();


        Button button = new Button("Insert");
        Style.style(button);
        button.setOnAction(e->{
            boolean isEmpty = areFieldsEmpty(tf) || isDatePickerEmpty(datePicker);
            if (isEmpty) {
                label.setText("One or more fields are empty.");
                return;
            }
            String dateString = "2024-05-12"; // Example date string in yyyy-MM-dd format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String firstName = tf[0].getText();
            String lastName = tf[1].getText();
            int  id  = Integer.parseInt(tf[2].getText());
            Date date = null;
            try {
                date = (Date) dateFormat.parse(dateString);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            char gen;
            if(g[0].isSelected()){
                gen = 'M';
            }else if(g[1].isSelected()){
                gen = 'F';
            }else if(g[2].isSelected()){
                gen = 'N';
            }else{
                label.setText("Select gender");
                return;
            }
            String phone = tf[5].getText();
            String location = tf[6].getText();
            String driverLicense = tf[3].getText();
            int age = Integer.parseInt(tf[4].getText());
            Customer customer = new Customer(firstName,  lastName,age,location,phone,date,driverLicense,id,gen);
            String query = "INSERT INTO Customer (cus_id,emp_id, first_name, last_name, birth_date,driving_license, age, phone, address) VALUES" +"(" + id + "," + id + "," + firstName + "," + lastName + "," + button.boundsInLocalProperty() + "," + driverLicense + "," + age + "," + phone + "," + location + "," + gen +")";
            Style.getCustomers().add(customer);
            label.setText("Customer has been inserted successfully");
        });


        vBox.getChildren().addAll(gp,button,label);
        return vBox;
    }
    private static boolean areFieldsEmpty(TextField[] textFields) {
        for (TextField tf : textFields) {
            if (tf.getText().isEmpty()) {
                return true; // Found an empty text field
            }
        }
        return false; // All text fields are filled
    }

    private static boolean isDatePickerEmpty(DatePicker datePicker) {
        return datePicker.getValue() == null;
    }

}


