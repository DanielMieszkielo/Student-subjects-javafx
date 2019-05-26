package com.library.Models;

import com.library.DAO.StudentDAO;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;

public class Student extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;
    private String pesel;

    public Student(int id) {
        this.id = id;
    }
    public Student() {}

    public int getId() {
        return id;
    }
    private void setId(int id) { this.id = id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    @Override
    public void save() throws SQLException {
        StudentDAO.getInstance().update(this);
    }

    public static Student create(String firstName, String lastName, String pesel) throws SQLException {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setPesel(pesel);
        student.id = StudentDAO.getInstance().create(student);

        return student;
    }

    public static void delete(Student student) throws SQLException {
        StudentDAO.getInstance().delete(student.getId());
    }
}