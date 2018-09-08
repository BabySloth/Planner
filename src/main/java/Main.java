import helper.Measurement;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainHolder;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(MainHolder.getInstance(), Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(Event::consume);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
