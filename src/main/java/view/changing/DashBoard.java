package view.changing;

import helper.Fonts;
import helper.Measurement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import view.BasicView;
import view.VIEWS;

import java.time.LocalDate;

public class DashBoard extends VBox implements BasicView {
    private final StackPane parent;
    private VIEWS previousView;

    public DashBoard(StackPane parent){
        this.parent = parent;
        previousView = null;  // First time launching app doesn't have a previous
        generateView();

    }

    @Override
    public void generateView() {
        // Center
        parent.setAlignment(Pos.CENTER);

        // Size
        setMaxSize(Measurement.DashBoard.WIDTH, Measurement.DashBoard.HEIGHT);

        // CSS
        getStylesheets().addAll(Fonts.Stylesheet.ROBOTO,
                                getClass().getClassLoader().getResource("dashboard.css").toExternalForm());

        // Previous goes on top
        //if(previousView != null)
        getChildren().add(generatePreviousContainer()); //TODO: fix and indent

        // List of all options
        HBox regularContent = new HBox(calendarChange());
        StackPane contents = new StackPane(regularContent, contentBackground());
        ScrollPane regularHolder = new ScrollPane(contents);

        // Design
        regularHolder.getStyleClass().add("ScrollPane");
        regularHolder.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        regularHolder.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        getChildren().add(regularHolder);
    }

    private StackPane generatePreviousContainer(){
        StackPane holder = new StackPane();
        holder.setPrefSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);

        //Label label = new Label(String.format("Previous: %s", previousView.toString()));
        Label label = new Label("Previous: ");  //TODO replace
        label.setMinSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);
        label.setMaxSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);
        label.setStyle(Fonts.Style.getRoboto(40) + "-fx-background-color: pink;");
        holder.getChildren().add(label);

        return holder;
    }

    private HBox calendarChange(){
        VIEWS view = VIEWS.CALENDAR;
        LocalDate now = LocalDate.now();
        HBox container = generateContainer();

        // Content
        Label name = horizontalRegularLabel(view.toString(), 40);
        String dateText = String.format("%s %d", now.getMonth().toString().substring(0, 3), now.getDayOfMonth());
        Label date = horizontalRegularLabel(dateText, 40);

        container.getChildren().addAll(name, date);
        return container;
    }

    private HBox generateContainer(){
        HBox holder = new HBox();

        // Design
        holder.setPrefSize(Measurement.DashBoard.REGULAR_WIDTH, Measurement.DashBoard.REGULAR_HEIGHT);
        holder.setStyle("-fx-background-color: yellow;");
        holder.setPadding(new Insets(0, 10, 0, 10));

        return holder;
    }

    private Label horizontalRegularLabel(String text, double width){
        Label label = new Label();

        // Text
        StringBuilder displayText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            displayText.append(text.charAt(i)).append("\n");
        }
        label.setText(displayText.toString());

        // Design
        label.setStyle(Fonts.Style.getRoboto(30));
        label.setPrefSize(width, Measurement.DashBoard.REGULAR_HEIGHT);
        label.setAlignment(Pos.TOP_LEFT);

        return label;
    }

    private Pane contentBackground(){
        Pane background = new Pane();

        return background;
    }

    public void setPreviousView(VIEWS previousView) {
        this.previousView = previousView;
    }
}
