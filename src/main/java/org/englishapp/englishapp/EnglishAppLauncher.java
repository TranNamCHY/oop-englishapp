package org.englishapp.englishapp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.englishapp.englishapp.utility.GoogleVoiceAPI;

public class EnglishAppLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EnglishAppLauncher.class.getResource("inital-ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("Dictionary App");
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                GoogleVoiceAPI.shutdownExecutorService();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}