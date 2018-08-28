package view.calendar;

import javafx.scene.layout.*;
import view.BasicView;
import view.MainHolder;


/**
 * Include view since Calendar is already a class in java.util.Calendar
 */
public class CalendarView extends HBox implements BasicView{
    private final MainHolder parent;
    private boolean containsWarning = false;
    private AllEvents allEvents;

    private final CalendarContent calendarContent;
    private final EventManipulator eventManipulator;

    public CalendarView(MainHolder parent, AllEvents allEvents){
        this.parent = parent;
        this.allEvents = allEvents;
        calendarContent = new CalendarContent(this, allEvents);
        eventManipulator = new EventManipulator(this, allEvents);

        // Have reference to each other
        calendarContent.setEventManipulator(eventManipulator);
        eventManipulator.setCalendarContent(calendarContent);

        setMainDesign();
        generateView();
    }

    @Override
    public void generateView() {
        // View is divided into two section: left and right.

        // Left view
        getChildren().add(calendarContent);

        // Right view
        // Contains the event details and date details (range of date, amount of days until)
        getChildren().add(eventManipulator);
    }

    @Override
    public void setMainDesign() {
        getStylesheets().add(getClass().getClassLoader().getResource("calendarView.css").toExternalForm());
        final double starting_xcor =  helper.Measurement.STARTING_XCOR;
        relocate(starting_xcor, 0);
        setPrefSize(helper.Measurement.SCREEN_WIDTH - starting_xcor, helper.Measurement.SCREEN_HEIGHT);
    }
}
