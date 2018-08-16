package view.calendar;

import helper.Measurement;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.BasicView;


/**
 * Include view since Calendar is already a class in java.util.Calendar
 */
public class CalendarView extends HBox implements BasicView {
    private static CalendarView self = null;

    private CalendarView(){
        setMainDesign();
        generateView();
    }

    public static CalendarView getInstance(){
        if(self == null){
            self = new CalendarView();
        }
        return self;
    }

    @Override
    public void generateView() {

    }

    @Override
    public void setMainDesign() {
        final double starting_xcor =  helper.Measurement.STARTING_XCOR;
        relocate(starting_xcor, 0);
        setPrefSize(helper.Measurement.SCREEN_WIDTH - starting_xcor, helper.Measurement.SCREEN_HEIGHT);
        setStyle("-fx-background-color: yellow;");
    }

    private class Measurement{

    }
}
