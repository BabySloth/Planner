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
    private VIEWS currentView;

    /**
     * First view is always the views.dashboard
     */
    private MainHolder(){
        setMainDesign();
        generateView();
    }

    @Override
    public void generateView() {
        // First view is always the dashboard
        currentView = VIEWS.DASHBOARD;
        getChildren().addAll(DashBoard.getInstance(), new SideBoard(VIEWS.CALENDAR));
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
            DashBoard.getInstance().setPreviousView(currentView);
            currentView = newView;
            getChildren().clear();
            getChildren().add(new SideBoard(newView));  // Sideboard is always shown
        }

        // Add the new view to the screen
        switch(newView){
            case DASHBOARD:
                getChildren().add(DashBoard.getInstance());
                break;
            case CALENDAR:
                getChildren().add(CalendarView.getInstance());
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
