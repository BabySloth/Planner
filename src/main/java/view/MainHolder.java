package view;

import helper.Measurement;
import javafx.scene.layout.StackPane;
import view.BasicView;
import view.VIEWS;
import view.calendar.CalendarView;
import view.changing.DashBoard;

public class MainHolder extends StackPane implements BasicView {
    private final String STYLESHEET = getClass().getResource("MainHolderDesign.css").toExternalForm();
    private DashBoard dashBoard;
    private CalendarView calendarView;
    private VIEWS currentView;

    /**
     * First view is always the views.dashboard
     */
    public MainHolder(){
        getStylesheets().add(STYLESHEET);
        setPrefSize(Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        createViews();
        generateView();
    }

    @Override
    public void generateView() {
        // First view is always the dashboard
        currentView = VIEWS.DASHBOARD;
        getChildren().add(dashBoard);
    }

    private void createViews(){
        dashBoard = new DashBoard(this);
        calendarView = new CalendarView();
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
            getChildren().clear();
        }

        // Add the new view to the screen
        switch(newView){
            case CALENDAR:
                getChildren().add(calendarView);
                break;
            case DASHBOARD:
                getChildren().add(dashBoard);
                break;
        }
    }
}
