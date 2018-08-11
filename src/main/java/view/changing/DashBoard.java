package view.changing;

import helper.Fonts;
import helper.Measurement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import view.BasicView;
import view.VIEWS;

public class DashBoard extends VBox implements BasicView {
    private final StackPane parent;
    private VIEWS previousView;

    public DashBoard(StackPane parent){
        this.parent = parent;
        previousView = null;  // First time launching app doesn't have a previous
        generateView();

    }

    @Override
    public void generateView() {
        // Center
        parent.setAlignment(Pos.CENTER);

        // Size
        setMaxSize(Measurement.DashBoard.WIDTH, Measurement.DashBoard.HEIGHT);

        // CSS
        getStylesheets().addAll(Fonts.Stylesheet.ROBOTO,
                                getClass().getClassLoader().getResource("dashboard.css").toExternalForm());

        // Previous goes on top
        //if(previousView != null)
            getChildren().add(generatePreviousContainer());

        getChildren().addAll();
    }

    private StackPane generatePreviousContainer(){
        StackPane holder = new StackPane();
        holder.setPrefSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);

        //Label label = new Label(String.format("Previous: %s", previousView.toString()));
        Label label = new Label("Previous: ");  //TODO replace
        label.setMinSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);
        label.setMaxSize(Measurement.DashBoard.PREVIOUS_WIDTH, Measurement.DashBoard.PREVIOUS_HEIGHT);
        label.setStyle(Fonts.Style.getRoboto(40) + "-fx-background-color: pink;");
        holder.getChildren().add(label);

        return holder;
    }

    private void generateContainer(){

    }

    public void setPreviousView(VIEWS previousView) {
        this.previousView = previousView;
    }
}
