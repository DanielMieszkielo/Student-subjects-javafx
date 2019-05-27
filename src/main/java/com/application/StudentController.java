package com.application;

import com.library.DAO.StudentDAO;
import com.library.Models.Student;
import com.library.Models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StudentController {
    @FXML
    TableView<Student> studentTable;

    @FXML
    TableColumn<Student, Integer> idColumn;
    @FXML
    TableColumn<Student, String> firstNameColumn;
    @FXML
    TableColumn<Student, String> lastNameColumn;
    @FXML
    TableColumn<Student, String> peselColumn;

    @FXML
    TextField newFirstNameTextField;
    @FXML
    TextField newLastNameTextField;
    @FXML
    TextField newPeselTextField;
    @FXML
    Button addNewStudentButton;

    private ObservableList<Student> data;

    public void initialize() {
        try {
            data = FXCollections.observableArrayList(StudentDAO.getInstance().all());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setupTableColumns();
        studentTable.setItems(data);

        studentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );
        peselColumn.setCellValueFactory(
                new PropertyValueFactory<>("pesel")
        );

        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        peselColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addStudent() {
        if (newFirstNameTextField.getText().isEmpty() ||
            newLastNameTextField.getText().isEmpty() ||
            newPeselTextField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fields can't be empty.").show();
            return;
        }

        if (newPeselTextField.getText().length() != 11) {
            new Alert(Alert.AlertType.ERROR, "Pesel field must have 11 characters.").show();
            return;
        }
        Student newStudent;
        try {
             newStudent = Student.create(
                    newFirstNameTextField.getText(),
                    newLastNameTextField.getText(),
                    newPeselTextField.getText()
            );
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error while trying to add new student\n\n" + e.getMessage()).show();
            return;
        }

        data.add(newStudent);
        newFirstNameTextField.setText("");
        newLastNameTextField.setText("");
        newPeselTextField.setText("");
        new Alert(Alert.AlertType.INFORMATION, "Student added successfully");
    }

    public void goBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("MainView.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void firstNameEditCommit(TableColumn.CellEditEvent<Student, String> event) {
        Student s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setFirstName(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void lastNameEditCommit(TableColumn.CellEditEvent<Student, String> event) {
        Student s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setLastName(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void peselEditCommit(TableColumn.CellEditEvent<Student, String> event) {
        Student s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setPesel(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void deleteStudent() {
        ObservableList<Student> students = studentTable.getSelectionModel().getSelectedItems();
        if (students.size() == 0) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + students.size() + " row(s)?",
                ButtonType.YES, ButtonType.CANCEL
        );
        confirmation.showAndWait();
        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        for (Student student : students) {
            this.data.remove(student);
            try {
                Student.delete(student);
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR,
                        "Error occurred when tried to delete student\n\n" + e.getMessage()
                ).show();
            }
        }
    }

    public void showStudentClasses(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null){
            return;
        }

        try {
            StudentClassController.setStudentId(selectedStudent.getId());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("StudentClassView.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR,
                    "Error occurred\n\n" + e.getMessage()
            ).show();
        }
    }
}
