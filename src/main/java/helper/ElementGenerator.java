package helper;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * Factory
 * Generates Nodes
 */
public class ElementGenerator {
    public static Label label(String text, Color color, double width, double height, String... styles){
        Label label = new Label(text); // Leading space to prevent jumbled text
        label.setTextFill(color);
        resize(label, width, height);
        Arrays.stream(styles).forEach(label.getStyleClass()::add);
        return label;
    }

    public static Button button(String text, Color color, double width, double height, String... styles){
        Button button = new Button(text);
        resize(button, width, height);

        button.setPrefSize(width, height);
        button.setTextFill(color);
        Arrays.stream(styles).forEach(button.getStyleClass()::add);

        return button;
    }

    public static TextField textField(String defaultText, String promptText, Color color, double width, double height, String... styles){
        TextField textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        resize(textField, width, height);
        textField.getStyleClass().addAll(styles);
        return textField;
    }

    /**
     * Generates a button to decrement something
     * @param color Color of text
     * @param style Styleclasses to add
     * @return New button
     */
    public static Button decrementButton(double side, Color color, String... style) {
        return ElementGenerator.button("<", color, side, side, style);
    }

    /**
     * Generates a button to increment something
     * @param color Color of text
     * @param style Styleclass to add
     * @return New button
     */
    public static Button incrementButton(double side, Color color, String... style){
        return ElementGenerator.button(">", color, side, side, style);
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

    public static void resize(Region region, double width, double height){
        if(width != 0) {
            region.setMinWidth(width);
            region.setPrefWidth(width);
            region.setMaxWidth(width);
        }
        if(height != 0) {
            region.setMinHeight(height);
            region.setPrefHeight(height);
            region.setMaxHeight(height);
        }
    }
}
