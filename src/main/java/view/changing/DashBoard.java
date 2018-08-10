package view.changing;

import javafx.scene.Node;
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
        generateView();
    }

    @Override
    public void generateView() {
        // Previous goes on top

        getChildren().addAll();
    }

    private void generatePreviousContainer(){

    }

    private void generateContainer(){

    }

    public void setPreviousView(VIEWS previousView) {
        this.previousView = previousView;
    }
}
