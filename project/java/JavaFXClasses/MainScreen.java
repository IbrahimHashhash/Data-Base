package JavaFXClasses;

import ERDClasses.Employee;
import SqlClass.DatabaseConnection;
import SqlClass.EmployeeDAO;
import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.sql.SQLException;


public class MainScreen extends Application {
    private static ImageView backgroundImage;
    private static Employee currentEmpolyee = new Employee();
    private static Stage stage1;
    EmployeeDAO employeeDAO = new EmployeeDAO(DatabaseConnection.getConnection());
    public MainScreen() throws SQLException {
    }

    public static void main(String[] args){
        launch();
    }
    private final BorderPane root = new BorderPane();
    private static SidePane sidePane;
    Login login = new Login();
    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(login,600,450);
        scene.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        login.getBt3().setOnAction(e->{
            stage.close();
        });
        login.getBt1().setOnAction(e->{
            try {
                handleLogin(login.getID(),login.getPasswordField(),stage);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void handleLogin(TextField ID,TextField passwordField,Stage stage) throws SQLException {
            if(ID.getText().isEmpty() || passwordField.getText().isEmpty()){
                login.getLabel().setText("All Fields are required");
                return;
            }
            if(login.getLoginDAO().selectLogin(Integer.parseInt(ID.getText()), passwordField.getText())){
                stage1 = new Stage();
                currentEmpolyee = employeeDAO.getEmployeeById(Integer.parseInt(ID.getText()));
                stage.close();
                sidePane = new SidePane();
                Scene scene1 = new Scene(sidePane);
                scene1.getStylesheets().add(getClass().getResource("/tableView.css").toExternalForm());
                stage1.setScene(scene1);
                stage1.setMaximized(true);
                stage1.show();

            }else{
                login.getLabel().setText("Password doesn't exist");
            }
    }

    public static Stage getStage1() {
        return stage1;
    }

    public static Employee getEmpolyee(){
        return currentEmpolyee;
    }

}