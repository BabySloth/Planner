import helper.Measurement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainHolder;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        // Generate the root
        Scene scene = new Scene(new MainHolder(), Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
