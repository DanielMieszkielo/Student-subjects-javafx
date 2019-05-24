package com.library;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, Worlld");
        Scene scene = new Scene(label);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}