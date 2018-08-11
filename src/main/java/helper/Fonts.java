package helper;

// Add s to end to prevent collision with Font
public class Fonts {
    public static class Stylesheet{
        public static String ROBOTO = "https://fonts.googleapis.com/css?family=Roboto";
    }

    public static class Style{
        private static String format = "-fx-font-family: %s; -fx-font-size: %d;";

        public static String getRoboto(int size){
            return String.format(format, "Roboto", size);
        }
    }
}
