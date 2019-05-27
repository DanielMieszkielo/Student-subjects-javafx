package com.application;

import com.library.DAO.ClassDAO;
import com.library.DAO.TeacherDAO;
import com.library.Models.Class;
import com.library.Models.Teacher;
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

public class ClassController {

    @FXML
    TableView<Class> classTable;

    @FXML
    TableColumn<Class, Integer> idColumn;
    @FXML
    TableColumn<Class, String> nameColumn;
    @FXML
    TableColumn<Class, String> teacherColumn;

    @FXML
    TextField newNameTextField;
    @FXML
    ChoiceBox newTeacherChoiceBox;
    @FXML
    Button addNewClassButton;

    @FXML
    Button deleteClassButton;

    private ObservableList<Class> data;
    private ObservableList<Teacher> teachers;

    public void initialize() {
        try {
            data = FXCollections.observableArrayList(ClassDAO.getInstance().all());
            teachers = FXCollections.observableArrayList(TeacherDAO.getInstance().all());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setupTableColumns();
        classTable.setItems(data);

        newTeacherChoiceBox.setItems(teachers);

        classTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        teacherColumn.setCellValueFactory(
                new PropertyValueFactory<>("teacher")
        );

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        teacherColumn.setCellFactory(ChoiceBoxTableCell.<Teacher, String>forTableColumn(teachers));
    }

    public void addClass() {
        if (newNameTextField.getText().isEmpty() ||
            newTeacherChoiceBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Fields can't be empty.").show();
            return;
        }

        Class newClass;
        try {
            newClass = Class.create(
                    newNameTextField.getText(),
                    ((Teacher)newTeacherChoiceBox.getValue()).getId()
            );
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error while trying to add new class\n\n" + e.getMessage()).show();
            return;
        }

        data.add(newClass);
        newNameTextField.setText("");
        newTeacherChoiceBox.setValue(null);
        new Alert(Alert.AlertType.INFORMATION, "Class added successfully");
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

    public void nameEditCommit(TableColumn.CellEditEvent<Class, String> event) {
        Class classObj = event.getTableView().getItems().get(event.getTablePosition().getRow());
        classObj.setName(event.getNewValue());
        try {
            classObj.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void teacherEditCommit(TableColumn.CellEditEvent<Class, Teacher> event) {
        Class classObj = event.getTableView().getItems().get(event.getTablePosition().getRow());
        try {
            classObj.setTeacherId(event.getNewValue().getId());
            classObj.save();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while trying to save changes\n\n" + e.getMessage());
        }
    }

    public void deleteClass() {
        ObservableList<Class> classes = classTable.getSelectionModel().getSelectedItems();
        if (classes.size() == 0) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + classes.size() + " row(s)?",
                ButtonType.YES, ButtonType.CANCEL
        );
        confirmation.showAndWait();
        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        for (Class classObj : classes) {
            this.data.remove(classObj);
            try {
                Class.delete(classObj);
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR,
                        "Error occurred when tried to delete class\n\n" + e.getMessage()
                ).show();
            }
        }
    }
}
