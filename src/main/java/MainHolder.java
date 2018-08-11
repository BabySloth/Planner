import javafx.scene.layout.StackPane;
import view.BasicView;
import view.VIEWS;
import view.calendar.CalendarView;
import view.changing.DashBoard;

class MainHolder extends StackPane implements BasicView {
    private final String STYLESHEET = getClass().getResource("MainHolderDesign.css").toExternalForm();
    private DashBoard dashBoard;
    private CalendarView calendarView;
    private VIEWS currentView;

    /**
     * First view is always the views.dashboard
     */
    MainHolder(){
        getStylesheets().add(STYLESHEET);

        createViews();
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
