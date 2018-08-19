package view.changing;

import helper.Colors;
import helper.Warning;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import view.BasicView;
import view.MainHolder;
import view.VIEWS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashBoard extends Pane implements BasicView{
    private MainHolder parent;
    private VIEWS previousView = null;
    private Warning warning;

    public DashBoard(MainHolder parent){
        this.parent = parent;
        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {
        double width = Measurement.WIDTH;
        double height =  Measurement.HEIGHT;

        // Stylesheet
        getStylesheets().add(getClass().getClassLoader().getResource("dashboard.css").toExternalForm());
        setMaxSize(width,height);
        getStyleClass().add("debug");

        // Position
        double middleX = helper.Measurement.SCREEN_WIDTH / 2;
        double middleY = helper.Measurement.SCREEN_HEIGHT / 2;
        relocate(middleX  - width / 2, middleY - height / 2);
    }

    @Override
    public void generateView() {
        getChildren().addAll(previousView(), calendarView(), entertainmentView(), quickView(), moneyView());
    }

    /**
     * Click to bring back the most recent used view. If none, gives user a warning.
     *
     * Height: 50
     * Width: MAX
     * Location: 0, 0
     *
     * @return HBox to click to change view
     */
    private HBox previousView() {
        final boolean hasPrevious = previousView != null;

        // Container
        String mainText = "Previous: ";
        Label mainLabel = generateLabel(mainText, Colors.LIGHT_GRAY,
                                        "robotoMedium", "preventBorderTouch");

        String sideText = hasPrevious ? previousView.toString() : "None";
        Color sideColor = hasPrevious ? previousView.color : Colors.LIGHT_GRAY;
        Label sideLabel = generateLabel(sideText, sideColor, "robotoThin");

        HBox container = (HBox) generateContainer(new HBox(mainLabel, sideLabel),
                                                  Measurement.WIDTH, 50, 0, 0);

        container.setOnMouseClicked(e -> {
            if (hasPrevious) {
                parent.changeView(previousView);
            } else {
                double x = e.getX() + getLayoutX();
                double y = e.getY() + getLayoutY();
                warning = new Warning("No previous view has been selected", 350, x, y);
                parent.getChildren().add(warning);
            }
        });

        return container;
    }

    /**
     * Height: 105
     * Width: 300
     * Location: 0, 55
     *
     * @return VBox to click to change view
     */
    private VBox calendarView() {
        VIEWS view = VIEWS.CALENDAR;

        // Container
        Label mainLabel = generateLabel(view.toString(), view.color,
                                        "robotoMedium", "preventBorderTouch");

        String sideText = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd"));
        Label sideLabel = generateLabel(sideText, Colors.LIGHT_GRAY,
                                        "robotoThin", "preventBorderTouch");

        VBox container = (VBox) generateContainer(new VBox(mainLabel, sideLabel), 300, 105, 0, 55);
        container.setOnMouseClicked(e -> parent.changeView(view));

        return container;
    }

    /**
     * Height: 50
     * Width: 145
     * Location: 305, 110
     *
     * @return Pane to change view
     */
    private Pane quickView() {
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
     *
     * @return Pane to change view
     */
    private Pane entertainmentView() {
        VIEWS view = VIEWS.ENTERTAINMENT;

        // Content
        Label mainLabel = generateLabel(view.toString(), view.color, "robotoThin", "preventBorderTouch");

        return generateContainer(new Pane(mainLabel), 395, 50, 305, 55);
    }

    /**
     * Height: 50
     * Width: 450
     * Location: 0, 165
     *
     * @return Pane to change view
     */
    private Pane moneyView() {
        VIEWS view = VIEWS.MONEY;

        // Content
        Label mainLabel = generateLabel(view.toString(), view.color,
                                        "robotoThin", "preventBorderTouch");

        return generateContainer(new Pane(mainLabel), 450, 50, 0, 165);
    }

    /**
     * Container for label of what clicking the container does.
     * @param pane What container is being used. Should be used with (new VBox(), ...)
     * @param width Width of container.
     * @param height Height of container.
     * @param x Position x
     * @param y Position y
     * @return Newly designed container, doesn't create a new object but modifies
     */
    private Pane generateContainer(Pane pane, double width, double height, double x, double y) {
        pane.setPrefSize(width, height);
        pane.relocate(x, y);
        pane.getStyleClass().add("container");

        return pane;
    }

    /**
     * Creates a label for container
     * @param text Text for container
     * @param color Text color
     * @param cssClasses Any css style classes
     * @return New object for label
     */
    private Label generateLabel(String text, Color color, String... cssClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(cssClasses);
        label.setTextFill(color);

        return label;
    }

    /**
     * Changes the previous container ({@link #previousView()} to allow quick go back.
     * @param view Most recently used view (other than dashboard)
     */
    public void setPreviousView(VIEWS view) {
        previousView = view;
        // Update the previous view container
        getChildren().clear();
        generateView();
    }

    private static class Measurement{
        final static double WIDTH = 700;
        final static double HEIGHT = 215;
    }
}
