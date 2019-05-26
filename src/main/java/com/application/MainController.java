package com.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    public Button studentButton;

    public void studentButtonClicked(ActionEvent event) {
        this.changeView(event,"StudentView.fxml");
    }

    public void teacherButtonClicked(ActionEvent event) {
        this.changeView(event,"TeacherView.fxml");
    }

    private void changeView(ActionEvent event, String name) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(name)));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
