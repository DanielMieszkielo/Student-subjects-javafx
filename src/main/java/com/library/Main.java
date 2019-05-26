package com.library;

import com.library.DAO.StudentDAO;
import com.library.Models.Student;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
//        launch(args);
        DatabaseManager.getInstance().registerModel(StudentDAO.class);
        try {
            Student damian = StudentDAO.getInstance().get(1);
//            damian = Student.create("Damian", "Citci", "97092212358");
            damian.setLastName("Cifci");
            damian.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void start(Stage primaryStage) {

        Label label = new Label("Hello, Worlld");
        Scene scene = new Scene(label);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
