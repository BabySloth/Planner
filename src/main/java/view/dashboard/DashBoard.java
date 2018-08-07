package view.dashboard;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.BasicView;
import view.VIEWS;

public class DashBoard extends VBox implements BasicView {
    private final StackPane parent;
    private VIEWS previousView;

    public DashBoard(){
        parent = (StackPane) getParent();
        previousView = null;  // First time launching app doesn't have a previous
    }

    @Override
    public void generateView() {
        
    }

    public void setPreviousView(VIEWS previousView) {
        this.previousView = previousView;
    }
}
