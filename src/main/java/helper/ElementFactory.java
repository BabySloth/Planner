package helper;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory
 * Generates Nodes
 */
public class ElementFactory {
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

    /**
     * Does not accept color since you can't change TextField text color by code (only through css)
     * @param defaultText
     * @param promptText
     * @param width
     * @param height
     * @param styles
     * @return
     */
    public static TextField textField(String defaultText, String promptText, double width, double height, String... styles){
        TextField textField = new TextField(defaultText);
        textField.setPromptText(promptText);
        resize(textField, width, height);
        textField.getStyleClass().addAll(styles);
        return textField;
    }

    public static TextArea textArea(String text, String prompt, double width, double height, String... styles){
        TextArea textArea = new TextArea(text);
        textArea.setWrapText(true);
        textArea.setPromptText(prompt);
        resize(textArea, width, height);
        textArea.getStyleClass().addAll(styles);
        return textArea;
    }

    public static ComboBox<String> comboBox(double width, double height, List<String> choices, String... styles){
        ComboBox<String> control = new ComboBox<>(FXCollections.observableArrayList(choices));
        resize(control, width, height);
        control.getStyleClass().addAll(styles);
        control.getSelectionModel().selectFirst();

        return control;
    }

    /**
     * Generates a button to decrement something
     * @param color Color of text
     * @param style Styleclasses to add
     * @return New button
     */
    public static Button decrementButton(double side, Color color, String... style) {
        return ElementFactory.button("<", color, side, side, style);
    }

    /**
     * Generates a button to increment something
     * @param color Color of text
     * @param style Styleclass to add
     * @return New button
     */
    public static Button incrementButton(double side, Color color, String... style){
        return ElementFactory.button(">", color, side, side, style);
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

    public static HBox hBox(double width, double height, Node... children){
        HBox pane = new HBox(children);
        resize(pane, width, height);
        return pane;
    }

    public static VBox vBox(double width, double height, Node... children){
        VBox pane = new VBox(children);
        resize(pane, width, height);
        return pane;
    }

    // TODO: Refactor this out such that it is only private static void
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
