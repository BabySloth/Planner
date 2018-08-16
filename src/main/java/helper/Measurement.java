package helper;

public class Measurement{
    public static double SCREEN_WIDTH = 1280;
    public static double SCREEN_HEIGHT = 800;

    public static class DashBoard{
        public static double WIDTH = 700;
        public static double HEIGHT = 215;
        public static double PREVIOUS_WIDTH = WIDTH;
        public static double PREVIOUS_HEIGHT = 50;
        public static double REGULAR_HEIGHT = HEIGHT - PREVIOUS_HEIGHT;
    }

    public static class SideBoard{
        public static double EXPAND_WIDTH = 0;
        public static double HIDDEN_WIDTH = 10;
    }

    public static class Warning{
        public static double HEIGHT = 25;
    }
}
