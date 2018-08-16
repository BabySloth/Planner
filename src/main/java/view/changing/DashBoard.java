package view.changing;

import helper.Colors;
import helper.Measurement;
import helper.Warning;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import view.BasicView;
import view.MainHolder;
import view.VIEWS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashBoard extends Pane implements BasicView {
    private static DashBoard self = null;
    private VIEWS previousView = null;
    private boolean containsWarning = false;
    private Warning warning;

    private DashBoard(){
        setMainDesign();
        generateView();
    }

    public static DashBoard getInstance(){
        if(self == null){
            self = new DashBoard();
        }
        return self;
    }

    @Override
    public void setMainDesign() {
        double width = Measurement.DashBoard.WIDTH;
        double height =  Measurement.DashBoard.HEIGHT;

        // Stylesheet
        getStylesheets().add(getClass().getClassLoader().getResource("dashboard.css").toExternalForm());
        setMaxSize(width,height);
        getStyleClass().add("debug");

        // Position
        double middleX = Measurement.SCREEN_WIDTH / 2;
        double middleY = Measurement.SCREEN_HEIGHT / 2;
        relocate(middleX  - width / 2, middleY - height / 2);
    }

    @Override
    public void generateView() {
        getChildren().addAll(previousView(), calendarView(), entertainmentView(), quickView(), moneyView());
    }

    /**
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
                                                  Measurement.DashBoard.WIDTH, 50, 0, 0);

        container.setOnMouseClicked(e -> {
            MainHolder parent = MainHolder.getInstance();

            if (hasPrevious) {
                parent.changeView(previousView);
            } else {
                double x = e.getX() + getLayoutX();
                double y = e.getY() + getLayoutY();
                if(containsWarning)
                    parent.getChildren().remove(warning);
                warning = new Warning("No previous view has been selected", 350, x, y);
                parent.getChildren().add(warning);
                containsWarning = true;
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
        container.setOnMouseClicked(e -> {
            MainHolder.getInstance().changeView(view);
        });

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

    private Pane generateContainer(Pane pane, double width, double height, double x, double y) {
        pane.setPrefSize(width, height);
        pane.relocate(x, y);
        pane.getStyleClass().add("container");

        return pane;
    }

    private Label generateLabel(String text, Color color, String... cssClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(cssClasses);
        label.setTextFill(color);

        return label;
    }

    public void setPreviousView(VIEWS view) {
        previousView = view;
    }

    public void setContainsWarning(boolean containsWarning){
        this.containsWarning = containsWarning;
    }
}
