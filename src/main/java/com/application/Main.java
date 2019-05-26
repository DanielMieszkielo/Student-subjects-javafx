package com.application;

import com.library.DAO.StudentDAO;
import com.library.DatabaseManager;
import com.library.Models.Student;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.settings;

import java.io.IOException;
import java.sql.SQLException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            DatabaseManager.getInstance().registerModels(settings.models);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to register models\n\n" +
                    "Error: " + e.getMessage());
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle(settings.appTitle);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
