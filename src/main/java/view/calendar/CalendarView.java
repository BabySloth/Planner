package view.calendar;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import view.BasicView;


/**
 * Include view since Calendar is already a class in java.util.Calendar
 */
public class CalendarView extends HBox implements BasicView {
    private static CalendarView self = null;

    private CalendarView(){

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

    }
}
