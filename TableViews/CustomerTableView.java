package TableViews;

import ERDClasses.Customer;
import SqlClass.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date; // Import java.sql.Date

public class CustomerTableView extends TableView<Customer> {
    public CustomerTableView(){
        this(CustomerDAO.getCustomers());
    }
    public CustomerTableView(ObservableList<Customer> customers) {
        // Create columns
        this.setPadding(new Insets(20,20,20,20));
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName")); // Assuming you want to display the first name
        nameColumn.setPrefWidth(100);

        TableColumn<Customer, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setPrefWidth(100);

        TableColumn<Customer, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberColumn.setPrefWidth(100);

        TableColumn<Customer, Date> birthDateColumn = new TableColumn<>("Birth Date");
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        birthDateColumn.setPrefWidth(100);

        TableColumn<Customer, String> licenseNumColumn = new TableColumn<>("License Number");
        licenseNumColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNum"));
        licenseNumColumn.setPrefWidth(150);

        TableColumn<Customer, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(100);

        TableColumn<Customer, Character> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderColumn.setPrefWidth(100);
        // Add columns to TableView
        this.getColumns().addAll(nameColumn, ageColumn, addressColumn, phoneNumberColumn, birthDateColumn, licenseNumColumn, idColumn, genderColumn);
        this.setItems(customers);
    }
}
