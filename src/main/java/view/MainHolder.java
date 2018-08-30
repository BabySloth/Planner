package view;

import helper.Measurement;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.calendar.AllEvents;
import view.calendar.CalendarView;
import view.calendar.DiskData;
import view.changing.DashBoard;
import view.changing.SideBoard;

import java.util.Arrays;

public class MainHolder extends Pane implements BasicView {
    private static MainHolder self = null;
    private final String STYLESHEET = getClass().getClassLoader().getResource("MainHolderDesign.css").toExternalForm();
    private DashBoard dashBoard;
    private CalendarView calendarView;
    private VIEWS currentView;
    private DiskData diskData = new DiskData();

    //Data
    private AllEvents calendarData = new AllEvents();

    /**
     * Singleton
     * @return Same instance
     */
    public static MainHolder getInstance() {
        if(self == null){
            self = new MainHolder();
        }
        return self;
    }

    /**
     * First view is always the views.dashboard
     */
    private MainHolder(){
        readData();
        prepareView();
        setMainDesign();
        generateView();
    }

    private void prepareView(){
        dashBoard = new DashBoard(this);
        calendarView = new CalendarView(this, calendarData);
    }

    private void readData(){
        diskData.calendarRead(calendarData);
    }

    public void writeData(){
        diskData.calendarWrite(calendarData);
    }

    @Override
    public void generateView() {
        // First view is always the dashboard
        currentView = VIEWS.DASHBOARD;
        getChildren().addAll(dashBoard, new SideBoard(VIEWS.DASHBOARD));
    }

    @Override
    public void setMainDesign() {
        getStylesheets().add(STYLESHEET);
        setPrefSize(Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        getStyleClass().add("mainHolder");
    }

    /**
     * Show a new view
     * @param newView New view to be shown
     */
    public void changeView(VIEWS newView){
        // Remove the current view if the new view is a different one
        if(newView == currentView){
            return;
        }else{
            dashBoard.setPreviousView(currentView);
            currentView = newView;
            getChildren().clear();
            getChildren().add(new SideBoard(newView));  // Sideboard is always shown
        }

        // Add the new view to the screen
        switch(newView){
            case DASHBOARD:
                getChildren().add(dashBoard);
                break;
            case CALENDAR:
                getChildren().add(calendarView);
                break;
        }
    }
}
