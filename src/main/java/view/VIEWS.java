package view;

import helper.Colors;
import javafx.scene.paint.Color;

/**
 * Names of views that can be shown
 */
public enum VIEWS {
    DASHBOARD(Colors.LIGHT_GRAY),
    CALENDAR(Colors.ORANGE),
    MONEY(Colors.LIGHT_GREEN),
    QUICK(Colors.LIGHT_BLUE),
    ENTERTAINMENT(Colors.LIGHT_RED);

    public final Color color;

    VIEWS(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        switch (this) {
            case MONEY:
                return "MONEY MANAGEMENT";
            default:
                return super.toString();
        }
    }
}
