package JavaFXClasses;

import ERDClasses.Car;
import Layouts.*;
import SqlClass.*;
import TableViews.CustomerTableView;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class SidePane extends BorderPane {
    public static Button[] bt = new Button[10];
    CustomerLayout customerLayout = new CustomerLayout();
    EmployeeLayout employeeLayout = new EmployeeLayout();
    PaymentLayout paymentLayout = new PaymentLayout();
    RentalLayout rentalLayout = new RentalLayout();
    CarLayout carLayout = new CarLayout();
    BorderPane dashPane = new BorderPane();
    ScrollPane scrollPane = new ScrollPane();
    CustomerDAO customerDAO = new CustomerDAO(DatabaseConnection.getConnection());
    CarList[] carLists = new CarList[5];
    HBox charts = Style.createHBox();
    VBox mainBox = new VBox();


    public SidePane() throws SQLException {
        this.setStyle("-fx-background-color:white;");
        Button helpButton = new Button("?");
        Style.style(helpButton);
        VBox vBox2 = Style.help();
        helpButton.setOnAction(e->{
            this.setCenter(vBox2);
        });
        this.setCenter(dashPane);
        HBox hBox = new HBox();
        for(int i=0;i<carLists.length;i++) {
            carLists[i] = new CarList();
            hBox.getChildren().add(carLists[i]);
        }
        carLists[0].info("Total Cars",CarDAO.getTotalNumberOfCars());
        carLists[1].info("Available Cars",CarDAO.getNumberOfAvailableCars());
        carLists[2].info("Rented Cars",CarDAO.getNumberOfRentedCars());
        carLists[3].info("Total Employees", EmployeeDAO.getTotalNumberOfEmployees());
        carLists[4].info("Total Customers",CustomerDAO.getTotalNumberOfCustomers());
        Style.styleDash(carLists[0],"lightGreen");
        Style.styleDash(carLists[1],"red");
        Style.styleDash(carLists[2],"deepSkyBlue");
        Style.styleDash(carLists[3],"darkblue");
        Style.styleDash(carLists[4],"darkOrange");
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setAlignment(Pos.CENTER);

        // -------------------

        hBox.setSpacing(30);
        dashPane.setTop(hBox);
        ImageView backgroundImage = Style.backgroundImage("C:/Users/ibrah/Downloads/2018_bmw_i8_roadster_4k-2560x1440.jpg");
        backgroundImage.fitWidthProperty().bind(this.widthProperty());
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        CustomerChart lineChart = new CustomerChart(DatabaseConnection.getConnection());
        Style.styleChart(lineChart);
        Style.styleChart(new CarChart(DatabaseConnection.getConnection()));
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(70);
        CustomerDAO.getCustomers().clear();
        CustomerDAO.getCustomers().addAll(CustomerDAO.getAllCustomers());

        CustomerTableView customerTableView = new CustomerTableView();
        customerTableView.setMaxSize(866,400);
        scrollPane.setContent(mainBox);
        charts.getChildren().addAll(new CarChart(DatabaseConnection.getConnection()),new CarAvailabilityChart(DatabaseConnection.getConnection()));

        mainBox.getChildren().addAll(hBox,lineChart,charts,customerTableView);
        mainBox.setPadding(new Insets(40,40,40,100));
        dashPane.setCenter(scrollPane);
        this.setStyle("-fx-background-color: rgb(159,189,165,255);");
        VBox mainMenu = Style.createSideButtons(bt);
        this.setLeft(mainMenu);
        this.setStyle("-fx-background-color:white;");
        bt[0].setText(MainScreen.getEmpolyee().getFirstName());
        bt[0].setOnAction(e->{
            try {
                Style.handleLogin();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        handleCustomer();
        handleDashBoard();
        handleEmployee();
        handleReport();
        handleRental();
        handlePayment();
        handleLoans();
        handleViewCars();
        handleCar();
    }

    //         String[] label = {"User","Dashboard","Customer","Payment","Rental","Car","Employee","Insurance","Reports"};
    public void handleDashBoard(){
        bt[1].setOnAction(e->{
            carLists[0].info("Total Cars",CarDAO.getTotalNumberOfCars());
            carLists[1].info("Available Cars",CarDAO.getNumberOfAvailableCars());
            carLists[2].info("Rented Cars",CarDAO.getNumberOfRentedCars());
            carLists[3].info("Total Employees", EmployeeDAO.getTotalNumberOfEmployees());
            carLists[4].info("Total Customers",CustomerDAO.getTotalNumberOfCustomers());
            try {
                charts.getChildren().clear();
                charts.getChildren().addAll(new CarChart(DatabaseConnection.getConnection()), new CarAvailabilityChart(DatabaseConnection.getConnection()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            this.setCenter(dashPane);
        });
    }
    //         String[] label = {"User","Dashboard","Customer","View Cars","Loan","Payment","Rents","Car","Employee","Reports"};
    public void handleCustomer(){
        bt[2].setOnAction(e->{
            this.setCenter(customerLayout);
        });
    }
    public void handleEmployee(){
        bt[8].setOnAction(e->{
            checkManager();
        });
    }

    public void handlePayment(){
        bt[5].setOnAction(e->{
            this.setCenter(paymentLayout);
        });
    }

    public void handleRental(){
        bt[6].setOnAction(e->{
            this.setCenter(rentalLayout);
        });
    }

    public void handleViewCars() {
        bt[3].setOnAction(e -> {
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            List<Car> cars = CarDAO.getAllCars();
            int numRows = (cars.size() + 4) / 5; // Calculate the number of rows needed

            Random random = new Random();

            for (int i = 0; i < numRows; i++) {
                // Add row constraints
                RowConstraints row = new RowConstraints();
                row.setPercentHeight(100.0 / numRows);
                gridPane.getRowConstraints().add(row);

                for (int j = i * 5; j < Math.min((i + 1) * 5, cars.size()); j++) {
                    CarList carList = new CarList();
                    Style.styleMenu(carList);
                    carList.info(cars.get(j).getModel(), (int) cars.get(j).getMileage());

                    // Generate random color
                    Color randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    Style.styleCar(carList,randomColor);

                    int colIndex = j % 5;
                    gridPane.add(carList, colIndex, i); // Add the car to the grid pane
                }
            }

            // Add column constraints to ensure even distribution of columns
            for (int i = 0; i < 5; i++) {
                ColumnConstraints col = new ColumnConstraints();
                col.setPercentWidth(100.0 / 5);
                gridPane.getColumnConstraints().add(col);
            }

            ScrollPane pane = new ScrollPane(gridPane);
            this.setCenter(pane);
            // Add the scroll pane to your scene or layout
        });
    }

    public void handleLoans(){
        bt[4].setOnAction(e->{
            try {
                this.setCenter(new LoanLayOut());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    public void handleReport(){
        bt[9].setOnAction(e->{
           ReportLayout reportLayout = new ReportLayout();
        });
    }
    public void handleCar(){
        bt[7].setOnAction(e->{
            this.setCenter(carLayout);
        });

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
        stage.setTitle("Security Check");
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
            boolean managerWithLogin = LoginDAO.isManagerWithLogin(Integer.parseInt(id.getText()), passwordField.getText());
            if(managerWithLogin) {
                this.setCenter(employeeLayout);
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

}
