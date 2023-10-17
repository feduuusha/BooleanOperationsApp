package com.fyodor.booleanframe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoolApplication extends Application {
    @Override
    public void start(Stage Stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BoolApplication.class.getResource("entryScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage.setTitle("Boolean Operations by Fyodor Voropaev");
        Stage.setScene(scene);
        Stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}