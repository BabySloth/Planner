package view.changing;

import javafx.scene.layout.VBox;
import view.BasicView;
import view.VIEWS;

public class SideBoard extends VBox implements BasicView {
    private final VIEWS activeView;

    public SideBoard(VIEWS activeView){
        this.activeView = activeView;

        setMainDesign();
        generateView();
    }

    @Override
    public void setMainDesign() {

    }

    @Override
    public void generateView() {

    }
    
}
