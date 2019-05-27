package com.application;

import com.library.DAO.ClassDAO;
import com.library.DAO.StudentClassDAO;
import com.library.DAO.StudentDAO;
import com.library.Models.Class;
import com.library.Models.Student;
import com.library.Models.StudentClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class StudentClassController {

    private static Student student;

    public static void setStudentId(int id) throws SQLException {
        student = StudentDAO.getInstance().get(id);
    }

    public static Student getStudent() {
        return student;
    }

    @FXML
    Label headerText;

    @FXML
    TableView<Class> studentClassTable;

    @FXML
    TableColumn<Class, String> classColumn;
    @FXML
    TableColumn<Class, String> teacherColumn;

    @FXML
    public ChoiceBox newClassChoiceBox;
    @FXML
    Button addNewStudentClassButton;

    private ObservableList<Class> data;
    private ObservableList<Class> allClasses;

    public void initialize() {
        headerText.setText(student.getFirstName() + " " + student.getLastName() + " classes");

        try {
            data = FXCollections.observableArrayList(
                    StudentClassDAO.getInstance().classesByStudentId(getStudent().getId())
            );
            allClasses = FXCollections.observableArrayList(ClassDAO.getInstance().all());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setupTableColumns();
        studentClassTable.setItems(data);

        newClassChoiceBox.setItems(allClasses);

        studentClassTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupTableColumns() {
        classColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        teacherColumn.setCellValueFactory(
                new PropertyValueFactory<>("teacher")
        );
    }

    public void addStudentClass() {
        if (newClassChoiceBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "You must select class.").show();
            return;
        }

        Class selectedClass;
        try {
            selectedClass = ((Class)newClassChoiceBox.getValue());
            if (data.contains(selectedClass)) {
                new Alert(Alert.AlertType.ERROR, "Student is already enrolled to this class").show();
                return;
            }

            StudentClass.create(
                    this.getStudent().getId(),
                    selectedClass.getId()
            );
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error while trying to add new class\n\n" + e.getMessage()).show();
            return;
        }

        data.add(selectedClass);
        newClassChoiceBox.setValue(null);
        new Alert(Alert.AlertType.INFORMATION, "Class added successfully");
    }

    public void goBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("StudentView.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudentClass() {
        ObservableList<Class> selectedClasses = studentClassTable.getSelectionModel().getSelectedItems();
        if (selectedClasses.size() == 0) {
            return;
        }
        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + selectedClasses.size() + " row(s)?",
                ButtonType.YES, ButtonType.CANCEL
        );
        confirmation.showAndWait();
        if (confirmation.getResult() != ButtonType.YES) {
            return;
        }

        for (Class classObj : selectedClasses) {
            this.data.remove(classObj);
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("studentId", Integer.toString(student.getId()));
                params.put("classId", Integer.toString(classObj.getId()));

                for (StudentClass studentClass : StudentClassDAO.getInstance().get(params)) {
                    StudentClassDAO.getInstance().delete(studentClass.getId());
                }
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
