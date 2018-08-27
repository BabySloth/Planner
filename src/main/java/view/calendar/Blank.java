package view.calendar;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Blank extends Rectangle {
    public Blank(double width, double height){
        setWidth(width);
        setHeight(height);
        setFill(Color.TRANSPARENT);
    }
}
