package com.application;

import com.library.Models.Teacher;
import com.library.DAO.TeacherDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TeacherController {

    @FXML
    TableView<Teacher> teacherTable;

    @FXML
    TableColumn<Teacher, Integer> idColumn;
    @FXML
    TableColumn<Teacher, String> firstNameColumn;
    @FXML
    TableColumn<Teacher, String> lastNameColumn;
    @FXML
    TableColumn<Teacher, String> peselColumn;
    @FXML
    TableColumn<Teacher, String> facultyColumn;

    @FXML
    TextField newFirstNameTextField;
    @FXML
    TextField newLastNameTextField;
    @FXML
    TextField newPeselTextField;
    @FXML
    TextField newFacultyTextField;
    @FXML
    Button addNewTeacherButton;

    @FXML
    Button deleteTeacherButton;

    private ObservableList<Teacher> data;

    public void initialize() {
        try {
            data = FXCollections.observableArrayList(TeacherDAO.getInstance().all());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setupTableColumns();
        teacherTable.setItems(data);

        teacherTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        facultyColumn.setCellValueFactory(
                new PropertyValueFactory<>("faculty")
        );

        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        peselColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void addTeacher() {
        if (newFirstNameTextField.getText().isEmpty() ||
            newLastNameTextField.getText().isEmpty() ||
            newPeselTextField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fields except faculty can't be empty.").show();
            return;
        }

        if (newPeselTextField.getText().length() != 11) {
            new Alert(Alert.AlertType.ERROR, "Pesel field must have 11 characters.").show();
            return;
        }
        Teacher newTeacher;
        try {
             newTeacher = Teacher.create(
                    newFirstNameTextField.getText(),
                    newLastNameTextField.getText(),
                    newPeselTextField.getText(),
                     newFacultyTextField.getText()
                     );
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error while trying to add new teacher\n\n" + e.getMessage()).show();
            return;
        }

        data.add(newTeacher);
        newFirstNameTextField.setText("");
        newLastNameTextField.setText("");
        newPeselTextField.setText("");
        newFacultyTextField.setText("");
        new Alert(Alert.AlertType.INFORMATION, "Teacher added successfully");
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

    public void firstNameEditCommit(TableColumn.CellEditEvent<Teacher, String> event) {
        Teacher s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setFirstName(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void lastNameEditCommit(TableColumn.CellEditEvent<Teacher, String> event) {
        Teacher s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setLastName(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void peselEditCommit(TableColumn.CellEditEvent<Teacher, String> event) {
        Teacher s = event.getTableView().getItems().get(event.getTablePosition().getRow());
        s.setPesel(event.getNewValue());
        try {
            s.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void deleteTeacher() {
        ObservableList<Teacher> teachers = teacherTable.getSelectionModel().getSelectedItems();
        if (teachers.size() == 0) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + teachers.size() + " row(s)?",
                ButtonType.YES, ButtonType.CANCEL
        );
        confirmation.showAndWait();
        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        for (Teacher teacher : teachers) {
            this.data.remove(teacher);
            try {
                Teacher.delete(teacher);
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR,
                        "Error occurred when tried to delete teacher\n\n" + e.getMessage()
                ).show();
            }
        }
    }
}
