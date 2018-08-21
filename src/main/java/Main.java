import helper.Measurement;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainHolder;
import view.calendar.CalendarView;
import view.changing.DashBoard;

import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

        //Shutdown means write data
        //Runtime.getRuntime().addShutdownHook(null);


    }
}
