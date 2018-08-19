package helper;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import view.BasicView;
import view.MainHolder;

public class Warning extends Pane implements BasicView {
    private String text;
    private double width;
    private final double height = Measurement.Warning.HEIGHT;
    private double posX;
    private double posY;

    public Warning(String text, double width, double posX, double posY) {
        this.text = text;
        this.width = width;
        this.posX = posX + width > Measurement.SCREEN_WIDTH ? Measurement.SCREEN_WIDTH - width : posX;
        this.posY = posY < height ? posY : posY - height;

        setMainDesign();
        generateView();
        removeSelf();
    }

    @Override
    public void setMainDesign() {
        getStylesheets().add(getClass().getClassLoader().getResource("warning.css").toExternalForm());
        setMaxSize(width, height);
        setMinSize(width, height);
        getStyleClass().add("container");

        relocate(posX, posY);
    }

    @Override
    public void generateView() {
        Label label = new Label(text);
        label.setTextFill(Colors.LIGHT_GRAY);
        label.getStyleClass().add("label");

        getChildren().add(label);
    }

    private void removeSelf() {
        setOnMouseClicked(e -> MainHolder.getInstance().getChildren().remove(this));
    }
}
