package TableViews;

import ERDClasses.Payment;
import SqlClass.PaymentDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class PaymentTableView extends TableView<Payment> {
    public PaymentTableView(){
        this(PaymentDAO.getPayments());

    }
    public PaymentTableView(ObservableList<Payment> items) {
        this.setPadding(new Insets(20,20,20,20));
        TableColumn<Payment, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setPrefWidth(150);
        TableColumn<Payment, Integer> paymentIDColumn = new TableColumn<>("Payment ID");
        paymentIDColumn.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        paymentIDColumn.setPrefWidth(150);

        TableColumn<Payment, Integer> downPayColumn = new TableColumn<>("Down Payment");
        downPayColumn.setCellValueFactory(new PropertyValueFactory<>("downPay"));
        downPayColumn.setPrefWidth(150);
        TableColumn<Payment, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paymentMethodColumn.setPrefWidth(180);

        TableColumn<Payment, LocalDate> paydateColumn = new TableColumn<>("Payment Date");
        paydateColumn.setCellValueFactory(new PropertyValueFactory<>("paydate"));
        paydateColumn.setPrefWidth(150);
        getColumns().addAll(amountColumn, paymentIDColumn, downPayColumn, paymentMethodColumn, paydateColumn);
        this.setItems(items);
    }
}
