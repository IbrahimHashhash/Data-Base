module com.example.databaseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.example.databaseproject to javafx.fxml;
    exports ERDClasses;
    opens ERDClasses to javafx.fxml;
    exports JavaFXClasses;
    opens JavaFXClasses to javafx.fxml;
    exports TableViews;
    opens TableViews to javafx.fxml;
    exports Layouts;
    opens Layouts to javafx.fxml;
    exports SqlClass;
    opens SqlClass to javafx.fxml;
}