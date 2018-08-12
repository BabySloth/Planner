import helper.Measurement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.MainHolder;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        // Generate the root
        StackPane root = new MainHolder();

        Scene scene = new Scene(root, Measurement.SCREEN_WIDTH, Measurement.SCREEN_HEIGHT);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
