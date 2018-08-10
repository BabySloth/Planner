import helper.Measurement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        // Generate the root
        StackPane root = new MainHolder();

        Scene scene = new Scene(root, Measurement.screen_wdith, Measurement.screen_height);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        System.out.println("Works");
        launch(args);
    }
}
