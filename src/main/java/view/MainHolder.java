package view;

import helper.Measurement;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import view.BasicView;
import view.VIEWS;
import view.calendar.CalendarView;
import view.changing.DashBoard;
import view.changing.SideBoard;

public class MainHolder extends Pane implements BasicView {
    private static MainHolder self = null;
    private final String STYLESHEET = getClass().getClassLoader().getResource("MainHolderDesign.css").toExternalForm();
    private DashBoard dashBoard;
    private CalendarView calendarView;
    private VIEWS currentView;

    /**
     * First view is always the views.dashboard
     */
    private MainHolder(){
        prepareView();
        setMainDesign();
        generateView();
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

    private void prepareView(){
        dashBoard = new DashBoard(this);
        calendarView = new CalendarView(this);
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

    public static MainHolder getInstance() {
        if(self == null){
            self = new MainHolder();
        }
        return self;
    }
}
