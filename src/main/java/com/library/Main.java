package com.library;

import com.library.DAO.StudentDAO;
import com.library.Models.Student;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
//        launch(args);
        DatabaseManager.getInstance().registerModel(StudentDAO.class);
        Student s = new Student(1);
        s.setFirstName("Damian");
        s.setLastName("Ciftci");
        s.setPesel("97092212358");
        s.save();
        Student s1 = new Student(1);
        s1.setFirstName("Damian");
        s1.setLastName("Ciftci");
        s.setPesel("88092212358");
        s.save();
        System.out.println(s.getId());
    }

//    @Override
    public void start(Stage primaryStage) {

        Label label = new Label("Hello, Worlld");
        Scene scene = new Scene(label);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
