package helper;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class ElementGenerator {
    public static Label label(String text, Color color, double width, double height, String... styles){
        Label label = new Label(text); // Leading space to prevent jumbled text
        label.setTextFill(color);
        label.setMaxSize(width, height);
        label.setPrefSize(width, height);
        label.setMinSize(width, height);
        Arrays.stream(styles).forEach(label.getStyleClass()::add);
        return label;
    }

    public static Pane pane(Pane container, double width, double height){
        container.setMinSize(width, height);
        container.setPrefSize(width, height);
        container.setMaxSize(width, height);

        return container;
    }

    public static Button button(String text, Color color, double width, double height, String... styles){
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setTextFill(color);
        Arrays.stream(styles).forEach(button.getStyleClass()::add);

        return button;
    }

    public static GridPane gridPane(int rowAmount, int columnAmount, double rowHeight, double columnWidth){
        GridPane pane = new GridPane();
        for(int i = 0; i < rowAmount; i++){
            pane.getRowConstraints().add(new RowConstraints(rowHeight));
        }
        for(int i = 0; i < columnAmount; i++){
            pane.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        return pane;
    }
}
