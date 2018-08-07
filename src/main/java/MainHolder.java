import javafx.scene.layout.StackPane;
import view.VIEWS;
import view.calendar.CalendarView;
import view.dashboard.DashBoard;

class MainHolder extends StackPane {
    private final String STYLESHEET = getClass().getResource("MainHolderDesign.css").toExternalForm();
    private final DashBoard dashBoard = new DashBoard();
    private final CalendarView calendarView = new CalendarView();
    private VIEWS currentView;

    /**
     * First view is always the views.dashboard
     */
    MainHolder(){
        getStylesheets().add(STYLESHEET);

        // First view is always the dashboard
        currentView = VIEWS.DASHBOARD;
        getChildren().add(dashBoard);
    }

    /**
     * Show a new view
     * @param newView New view to be shown
     */
    protected void changeView(VIEWS newView){
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
