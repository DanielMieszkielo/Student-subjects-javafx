package com.library.Models;

import com.library.DAO.TeacherDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;

public class Teacher extends BaseModel {
    private int id;
    private String firstName;
    private String lastName;
    private String faculty;
    private Date addedDate;

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

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(addedDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.addedDate = date;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public void save() throws SQLException {
        TeacherDAO.getInstance().update(this);
    }

    public static Teacher create(String firstName, String lastName, String faculty) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setFaculty(faculty);
        teacher.id = TeacherDAO.getInstance().create(teacher);

        return teacher;
    }

    public static void delete(Teacher teacher) throws SQLException {
        TeacherDAO.getInstance().delete(teacher.getId());
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName() + " @ " + this.getFaculty();
    }
}
