package TableViews;

import ERDClasses.Rental;
import SqlClass.RentalDAO;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Date; // Import java.sql.Date

public class RentalTableView extends TableView<Rental> {
    public RentalTableView(){
        this(RentalDAO.getRentals());
    }
    public RentalTableView(ObservableList<Rental> rentals) {
        this.setPadding(new Insets(20,20,20,20));
        TableColumn<Rental, Date> rentDateCol = new TableColumn<>("Rent Date");
        rentDateCol.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        rentDateCol.setPrefWidth(200);

        TableColumn<Rental, Date> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(200);

        TableColumn<Rental, Integer> rentIDCol = new TableColumn<>("Rent ID");
        rentIDCol.setCellValueFactory(new PropertyValueFactory<>("rentID"));
        rentIDCol.setPrefWidth(200);

        TableColumn<Rental, String> cancellationCol = new TableColumn<>("Cancellation");
        cancellationCol.setCellValueFactory(new PropertyValueFactory<>("cancellation"));
        cancellationCol.setPrefWidth(200);

        this.getColumns().addAll(rentDateCol, returnDateCol, rentIDCol, cancellationCol);
        this.setItems(rentals);

    }
}
