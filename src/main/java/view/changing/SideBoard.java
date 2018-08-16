package view.changing;

import helper.Colors;
import helper.Measurement;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.BasicView;
import view.MainHolder;
import view.VIEWS;

public class SideBoard extends HBox implements BasicView {
    private final VIEWS activeView;
    private ExpanderType expanderType = ExpanderType.HIDDEN;
    private boolean calledOnce;

    public SideBoard(VIEWS activeView){
        this.activeView = activeView;

        updateView();

        calledOnce = true;
    }

    private void updateView(){
        getChildren().clear();

        generateView();
        setMainDesign();
    }

    @Override
    public void setMainDesign() {
        // Do not want to keep adding style classes
        if(!calledOnce){
            getStylesheets().add(getClass().getClassLoader().getResource("sideboard.css").toExternalForm());
            setPrefHeight(helper.Measurement.SCREEN_HEIGHT);
        }

        double width = expanderType == ExpanderType.EXTENDED ? Measurement.EXPAND_WIDTH : Measurement.HIDDEN_WIDTH;
        setMinWidth(width);  // Force the size
    }

    @Override
    public void generateView() {
        // Main content
        if(expanderType == ExpanderType.EXTENDED){
            getChildren().add(0, new MainContent());
        }

        // Label for expanding and hiding view
        Label expanderLabel = generateExpanderLabel();

        getChildren().add(expanderLabel);
    }

    private Label generateExpanderLabel(){
        Label label = new Label();

        // Style
        label.setMinSize(Measurement.HIDDEN_WIDTH, helper.Measurement.SCREEN_HEIGHT);
        label.setTextFill(Colors.LIGHT_GRAY);
        label.getStyleClass().add("expanderLabel");

        // Content
        label.setText(expanderType == ExpanderType.EXTENDED ? "<" : ">");

        // Function
        label.setOnMouseClicked(e -> {
            if(expanderType == ExpanderType.EXTENDED)
                expanderType = ExpanderType.HIDDEN;
            else
                expanderType = ExpanderType.EXTENDED;

            updateView();
        });
        return label;
    }

    private class MainContent extends ScrollPane implements BasicView{
        MainContent(){
            setMainDesign();
            generateView();
        }

        @Override
        public void setMainDesign() {
            getStyleClass().add("mainContent");
            setPrefSize(SideBoard.Measurement.CONTENT_WIDTH, helper.Measurement.SCREEN_HEIGHT);
            setHbarPolicy(ScrollBarPolicy.NEVER);
            setVbarPolicy(ScrollBarPolicy.NEVER);
        }

        @Override
        public void generateView() {
            VBox mainHolder = new VBox();

            // Change to another view
            for(VIEWS view : VIEWS.values()){
                mainHolder.getChildren().addAll(generateChangeViewer(view), generateInvisibleBreak());
            }

            // Horizontal line
            mainHolder.getChildren().add(generateBreak());

            setContent(mainHolder);
        }

        private Label generateChangeViewer(VIEWS view){
            Label label = new Label(view.toString());
            label.getStyleClass().add("changeViewLabel");
            label.setTextFill(view.color);
            label.setPrefWidth(SideBoard.Measurement.CONTENT_WIDTH);
            if(view == activeView)
                label.setBackground(new Background(new BackgroundFill(Colors.DARKEST_GRAY, null, null)));
            else
                label.setBackground(new Background(new BackgroundFill(Colors.DARK_GRAY, null, null)));

            label.setOnMouseClicked(e -> MainHolder.getInstance().changeView(view));
            return label;
        }

        private VBox generateBreak(){
            Rectangle visible = new Rectangle(SideBoard.Measurement.CONTENT_BREAK, 5, Color.BLACK);
            return new VBox(generateInvisibleBreak(), visible, generateInvisibleBreak());
        }

        private Rectangle generateInvisibleBreak(){
            return new Rectangle(1, 5, Color.TRANSPARENT);
        }
    }

    private enum ExpanderType{
        EXTENDED, HIDDEN;
    }

    private class Measurement{
        final static double EXPAND_WIDTH = 0;
        final static double HIDDEN_WIDTH = 15;
        final static double CONTENT_WIDTH = 300;
        final static double CONTENT_BREAK = 150;
    }
}
