package view.calendar;

import helper.Colors;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.BasicView;

public class EventDisplay extends StackPane implements BasicView {
    private final Event event;
    private final boolean needsTitle;
    private final boolean isContinuation;
    private final int cellWraps;
    private final double width;
    private final double height;

    EventDisplay(Event event, boolean needsTitle, boolean isContinuation, int cellWraps) {
        this.event = event;
        this.needsTitle = needsTitle;
        this.isContinuation = isContinuation;
        this.cellWraps = cellWraps;

        // Sizing
        height = 20;
        width = isContinuation ? 130 : 120;

        setMinSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);

        setAlignment(Pos.TOP_LEFT);
        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {
        if(!isContinuation){
            setStyle("-fx-padding: 0px 5px 0px 5px");  // Makes it so it doesn't touch another an adjacent EventDisplay
        }
    }

    @Override
    public void generateView() {
        // Background
        getChildren().add(generateBackground());

        // Text if needed
        if(needsTitle) {
            getChildren().add(generateTitle());
            this.toFront();  // Make text show throughout wrapping of text
        }else{
            this.toBack();
        }
    }

    private Rectangle generateBackground(){
        return new Rectangle(width, height, Color.TRANSPARENT);
    }

    private Label generateTitle(){
        Label label = new Label(" " + event.getTitle());
        // Force size
        // Label can extend as far as the event spans
        double specialWidth = width + (cellWraps - 1) * 130;
        label.setMinSize(specialWidth, height);
        label.setPrefSize(specialWidth, height);
        label.setMaxSize(specialWidth, height);
        label.setWrapText(false);

        label.setTextFill(Colors.DARK_GRAY);

        label.setBackground(new Background(new BackgroundFill(event.getColor(), null, null)));

        return label;
    }
}
