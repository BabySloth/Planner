package helper;

import javafx.scene.paint.Color;

public class Colors {
    public static Color LIGHT_GRAY = Color.web("F0EDEE", 1); // Text
    public static Color GRAY = Color.web("454545", 1);  // Background
    public static Color DARKEST_GRAY = Color.web("292929", 1); // Hover background
    public static Color DARK_GRAY = Color.web("3e3e3e", 1); // Node background
    public static Color LIGHT_BLUE = Color.web("30C5FF", 1);
    public static Color BLUE = Color.web("30C5FF", 1);
    public static Color LIGHT_GREEN = Color.web("53DD6C", 1);
    public static Color LIGHT_RED = Color.web("FF3E41", 1);
    public static Color YELLOW = Color.web("EFE821", 1);
    public static Color ORANGE = Color.web("F75C03", 1);
    public static Color DARK_RED = Color.web("C5283D", 1);
    public static Color PURPLE = Color.web("D854D4", 1);
    public static Color YELLOW_ORANGE= Color.web("FFBC42", 1);

    public static String toHex(Color color){
        return String.format( "#%02X%02X%02X",
                              (int)( color.getRed() * 255 ),
                              (int)( color.getGreen() * 255 ),
                              (int)( color.getBlue() * 255 ) );
    }
}