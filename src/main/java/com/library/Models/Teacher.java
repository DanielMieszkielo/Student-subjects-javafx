package com.library.Models;

import com.library.DAO.TeacherDAO;

import java.sql.SQLException;

public class Teacher extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;
    private String pesel;
    private String faculty;

    public Teacher(int id) {
        this.id = id;
    }
    public Teacher() {}

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

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

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    @Override
    public void save() throws SQLException {
        TeacherDAO.getInstance().update(this);
    }

    public static Teacher create(String firstName, String lastName, String pesel, String faculty) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setPesel(pesel);
        teacher.setFaculty(faculty);
        teacher.id = TeacherDAO.getInstance().create(teacher);

        return teacher;
    }

    public static void delete(Teacher teacher) throws SQLException {
        TeacherDAO.getInstance().delete(teacher.getId());
    }
}
