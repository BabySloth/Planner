package view.changing;

import helper.Colors;
import helper.Measurement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import view.BasicView;
import view.MainHolder;
import view.VIEWS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashBoard extends Pane implements BasicView {
    private final MainHolder parent;
    private VIEWS previousView = null;

    public DashBoard(MainHolder parent){
        this.parent = parent;

        //TODO: remove
        previousView = VIEWS.CALENDAR;

        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {
        // Stylesheet
        getStylesheets().add(getClass().getClassLoader().getResource("dashboard.css").toExternalForm());
        setMaxSize(Measurement.DashBoard.WIDTH , Measurement.DashBoard.HEIGHT);
        getStyleClass().add("debug");

        // Position
        parent.setAlignment(Pos.CENTER);
    }

    @Override
    public void generateView() {
        getChildren().addAll(previousView(), calendarView(), entertainmentView(), quickView(), moneyView());
    }

    /**
     * Height: 50
     * Width: MAX
     * Location: 0, 0
     * @return HBox to click to change view
     */
    private HBox previousView(){
        final boolean hasPrevious = previousView != null;

        // Container
        String mainText = "Previous: ";
        Label mainLabel = generateLabel(mainText, Colors.LIGHT_GRAY,
                                        "robotoMedium", "preventBorderTouch");

        String sideText = hasPrevious ? previousView.toString() : "None";
        Color sideColor = hasPrevious ? previousView.color : Colors.LIGHT_GRAY;
        Label sideLabel = generateLabel(sideText, sideColor, "robotoThin");

        return (HBox) generateContainer(new HBox(mainLabel, sideLabel), Measurement.DashBoard.WIDTH, 50, 0, 0);
    }

    /**
     * Height: 105
     * Width: 300
     * Location: 0, 55
     * @return VBox to click to change view
     */
    private VBox calendarView(){
        VIEWS view = VIEWS.CALENDAR;

        // Container
        Label mainLabel = generateLabel(view.toString(), view.color,
                                        "robotoMedium", "preventBorderTouch");

        String sideText = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd"));
        Label sideLabel = generateLabel(sideText, Colors.LIGHT_GRAY,
                                        "robotoThin", "preventBorderTouch");

        return (VBox) generateContainer(new VBox(mainLabel, sideLabel), 300, 105, 0, 55);
    }

    /**
     * Height: 50
     * Width: 145
     * Location: 305, 110
     * @return Pane to change view
     */
    private Pane quickView(){
        VIEWS view = VIEWS.QUICK;

        // Content
        Label mainLabel = generateLabel(view.toString(), view.color,
                                        "robotoThin", "preventBorderTouch");

        return generateContainer(new Pane(mainLabel), 145, 50, 305, 110);
    }

    /**
     * Height: 50
     * Width: 395
     * Location: 305, 55
     * @return
     */
    private Pane entertainmentView(){
        VIEWS view = VIEWS.ENTERTAINMENT;

        // Content
        Label mainLabel = generateLabel(view.toString(), view.color, "robotoThin", "preventBorderTouch");

        return generateContainer(new Pane(mainLabel), 395, 50, 305, 55);
    }

    /**
     * Height: 50
     * Width: 450
     * Location: 0, 165
     * @return
     */
    private Pane moneyView(){
        VIEWS view = VIEWS.MONEY;

        // Content
        Label mainLabel = generateLabel(view.toString(), view.color,
                                        "robotoThin", "preventBorderTouch");

        return generateContainer(new Pane(mainLabel), 450, 50, 0, 165);
    }




    private Pane generateContainer(Pane pane, double width, double height, double x, double y){
        pane.setPrefSize(width, height);
        pane.relocate(x, y);
        pane.getStyleClass().add("container");

        return pane;
    }

    private Label generateLabel(String text, Color color, String... cssClasses){
        Label label = new Label(text);
        label.getStyleClass().addAll(cssClasses);
        label.setTextFill(color);

        return label;
    }

    public void setPreviousView(VIEWS view){
        previousView = view;
    }
}
