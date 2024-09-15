package TableViews;

import ERDClasses.Employee;
import SqlClass.EmployeeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date; // Import java.sql.Date (instead of java.util.Date)

public class EmployeeTableView extends TableView<Employee> {
    public EmployeeTableView(){
        this(EmployeeDAO.getEmployees());
    }

    public EmployeeTableView(ObservableList<Employee> employees) {
        this.setPadding(new Insets(20,20,20,20));
        // Define table columns
        TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(100);

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty().concat(" ").concat(cellData.getValue().lastNameProperty()));
        nameColumn.setPrefWidth(110);

        TableColumn<Employee, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setPrefWidth(100);

        TableColumn<Employee, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setPrefWidth(120);

        TableColumn<Employee, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberColumn.setPrefWidth(150);

        TableColumn<Employee, Date> birthDateColumn = new TableColumn<>("Birth Date");
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        birthDateColumn.setPrefWidth(100);

        TableColumn<Employee, Character> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderColumn.setPrefWidth(100);

        // Add columns to table
        this.getColumns().addAll(idColumn, nameColumn, ageColumn, addressColumn, phoneNumberColumn, birthDateColumn, genderColumn);
        this.setItems(employees);

        // Set data to the table
    }

}