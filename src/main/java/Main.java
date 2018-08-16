import helper.Measurement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainHolder;
import view.calendar.CalendarView;
import view.changing.DashBoard;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        // Init singletons
        DashBoard.getInstance();
        CalendarView.getInstance();

        Scene scene = new Scene(MainHolder.getInstance(), Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
