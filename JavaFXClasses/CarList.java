package JavaFXClasses;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CarList extends BorderPane {
    public CarList(){
        this.setPrefSize(200,200);
    }

    public void info(String main, int info){
        Text title = new Text(main);
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD,20));
        Text information = new Text(info + "");
        information.setFill(Color.WHITE);
        information.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,50));
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(title,information);
        this.setCenter(vBox);
    }
}