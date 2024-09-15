package Layouts;

import JavaFXClasses.Style;
import SqlClass.DatabaseConnection;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.itextpdf.text.Document;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportLayout extends BorderPane {

    public ReportLayout() {
        Stage stage = new Stage();
        Scene scene = new Scene(this, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Report");
        Label saveAs = new Label("Save As");
        saveAs.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:21;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1);");
        Label saveAs1 = new Label("Save As");
        saveAs1.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:21;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1);");

        Label title = new Label("Choose Report");
        title.setStyle("-fx-text-fill: rgba(251, 80, 1); -fx-font-family: 'Trebuchet MS';-fx-font-weight:bold;-fx-font-size:21;-fx-background-radius: 50;-fx-border-radius: 50;-fx-border-width:3px;-fx-border-color:rgba(251, 80, 1);");

        VBox rentBox = Style.createVBox();

        VBox payBox = Style.createVBox();
        payBox.setPadding(new Insets(10, 10, 10, 10));
        Button bt1R = new Button("PDF");
        Button bt2R = new Button("Text");

        bt1R.setOnAction(e -> {
            createPdfDocument("rental_contracts_report.pdf", "Rental Contracts Report", "Customer Name", "Rent Date", "Car Rented", "End Date");
        });

        bt2R.setOnAction(e -> {
            createTextDocument("rental_contracts_report.txt");
        });

        bt1R.setPrefSize(120, 60);
        bt2R.setPrefSize(120, 60);
        Button bt1P = new Button("PDF");
        Button bt2P = new Button("Text");

        bt1P.setOnAction(e -> {
            createPDFPaymentDoc("payment_report.pdf");
        });

        bt2P.setOnAction(e -> {
            createTextPaymentDoc("payment_report.txt");
        });
        bt1P.setPrefSize(120, 60);
        bt2P.setPrefSize(120, 60);


        rentBox.setPadding(new Insets(10, 10, 10, 10));
        bt1R.setStyle("-fx-font-size:15px;");
        bt2R.setStyle("-fx-font-size:15px;");

        rentBox.getChildren().addAll(saveAs1,bt1R, bt2R);
        payBox.getChildren().addAll(saveAs, bt1P, bt2P);

        VBox vBox1 = Style.createVBox();
        Button rent = new Button("Rents");
        Button pay = new Button("Payments");

        rent.setPrefSize(190, 80);
        rent.setStyle("-fx-font-size:17;");
        pay.setStyle("-fx-font-size:17px;");

        vBox1.setPadding(new Insets(10, 10, 10, 10));
        pay.setPrefSize(190, 80);

        vBox1.getChildren().addAll(title,rent, pay);

        this.setCenter(vBox1);

        Stage stage1 = new Stage();
        Scene scene1 = new Scene(rentBox, 400, 300);
        scene1.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage1.setScene(scene1);
        stage1.setResizable(false);



        Stage stage2 = new Stage();
        Scene scene2 = new Scene(payBox, 400, 300);
        scene2.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
        stage2.setScene(scene2);
        stage2.setResizable(false);

        rent.setOnAction(e -> {
            stage1.show();
        });

        pay.setOnAction(e -> {
            stage2.show();
        });
    }

    private void createPdfDocument(String fileName, String reportTitle, String... headers) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            PdfPTable table = new PdfPTable(headers.length); // Number of columns based on headers
            addTableHeader(table, headers);
            addRows(table);

            document.add(new Paragraph(reportTitle));
            document.add(table);

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Paragraph(header));
            table.addCell(cell);
        }
    }

    private void addRows(PdfPTable table) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT c.first_name, c.last_name, rc.rent_date, rc.return_date, ca.model " +
                    "FROM Rental_Contract rc " +
                    "JOIN Customer c ON rc.cus_id = c.cus_Id " +
                    "JOIN Car ca ON rc.car_No = ca.car_No";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String customerName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                String rentDate = resultSet.getString("rent_date");
                String returnDate = resultSet.getString("return_date");
                String carRented = resultSet.getString("model");

                // Correct the order of adding cells to match the header order
                table.addCell(customerName);
                table.addCell(rentDate);
                table.addCell(carRented); // Corrected: Car Rented should come before End Date
                table.addCell(returnDate); // Corrected: End Date should come after Car Rented
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTextDocument(String fileName) {
        try (Connection conn = DatabaseConnection.getConnection();
             FileWriter writer = new FileWriter(fileName)) {

            String sql = "SELECT c.first_name, c.last_name, rc.rent_date, rc.return_date, ca.model " +
                    "FROM Rental_Contract rc " +
                    "JOIN Customer c ON rc.cus_id = c.cus_Id " +
                    "JOIN Car ca ON rc.car_No = ca.car_No";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String customerName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                String rentDate = resultSet.getString("rent_date");
                String returnDate = resultSet.getString("return_date");
                String carRented = resultSet.getString("model");

                // Construct the text line to write to the file
                String line = String.format("Customer Name: %s, Rent Date: %s, Car Rented: %s, End Date: %s%n",
                        customerName, rentDate, carRented, returnDate);
                writer.write(line);
            }

            System.out.println("Text file generated successfully.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createPDFPaymentDoc(String fileName) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            PdfPTable table = new PdfPTable(8); // Number of columns based on the data
            addTableHeader(table); // Adding headers to the table

            // Retrieve data from the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT p.payment_id, p.amount, p.pay_date, p.down_payment, p.total_cost, " +
                        "p.payment_method, p.cus_Id, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                        "FROM Payment p " +
                        "JOIN Customer c ON p.cus_Id = c.cus_Id";

                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int paymentId = resultSet.getInt("payment_id");
                    double amount = resultSet.getDouble("amount");
                    String payDate = resultSet.getString("pay_date");
                    double downPayment = resultSet.getDouble("down_payment");
                    double totalCost = resultSet.getDouble("total_cost");
                    String paymentMethod = resultSet.getString("payment_method");
                    int customerId = resultSet.getInt("cus_Id");
                    String customerName = resultSet.getString("customer_name");

                    // Add data to the PDF table
                    table.addCell(String.valueOf(paymentId));
                    table.addCell(String.valueOf(amount));
                    table.addCell(payDate);
                    table.addCell(String.valueOf(downPayment));
                    table.addCell(String.valueOf(totalCost));
                    table.addCell(paymentMethod);
                    table.addCell(String.valueOf(customerId));
                    table.addCell(customerName);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Add table to the document
            document.add(new Paragraph("Payment Report"));
            document.add(table);

            document.close();
            System.out.println("PDF file generated successfully.");

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell("Payment ID");
        table.addCell("Amount");
        table.addCell("Payment Date");
        table.addCell("Down Payment");
        table.addCell("Total Cost");
        table.addCell("Payment Method");
        table.addCell("Customer ID");
        table.addCell("Customer Name");
    }

    private void createTextPaymentDoc(String fileName) {
        try (Connection conn = DatabaseConnection.getConnection();
             FileWriter writer = new FileWriter(fileName)) {

            String sql = "SELECT p.payment_id, p.amount, p.pay_date, p.down_payment, p.total_cost, " +
                    "p.payment_method, p.cus_Id, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                    "FROM Payment p " +
                    "JOIN Customer c ON p.cus_Id = c.cus_Id";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int paymentId = resultSet.getInt("payment_id");
                double amount = resultSet.getDouble("amount");
                String payDate = resultSet.getString("pay_date");
                double downPayment = resultSet.getDouble("down_payment");
                double totalCost = resultSet.getDouble("total_cost");
                String paymentMethod = resultSet.getString("payment_method");
                int customerId = resultSet.getInt("cus_Id");
                String customerName = resultSet.getString("customer_name");

                // Construct the text line to write to the file
                String line = String.format("Payment ID: %d, Amount: %.2f, Payment Date: %s, Down Payment: %.2f, " +
                                "Total Cost: %.2f, Payment Method: %s, Customer ID: %d, Customer Name: %s%n",
                        paymentId, amount, payDate, downPayment, totalCost, paymentMethod, customerId, customerName);
                writer.write(line);
            }

            System.out.println("Text file generated successfully.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
