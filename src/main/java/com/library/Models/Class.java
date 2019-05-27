package com.library.Models;

import com.library.DAO.ClassDAO;
import com.library.DAO.TeacherDAO;

import java.sql.SQLException;

public class Class extends BaseModel {
    private int id;
    private String name;
    private Teacher teacher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherId(int teacherId) throws SQLException {
        this.teacher = TeacherDAO.getInstance().get(teacherId);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Class(int id) {
        this.id = id;
    }
    public Class() {}

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public void save() throws SQLException {
        ClassDAO.getInstance().update(this);
    }

    public static Class create(String name, int teacherId) throws SQLException {
        Class classObj = new Class();
        classObj.setName(name);
        classObj.setTeacherId(teacherId);

        classObj.setId(ClassDAO.getInstance().create(classObj));
        return classObj;
    }

    public static void delete(Class classObj) throws SQLException {
        ClassDAO.getInstance().delete(classObj.getId());
    }

    @Override
    public String toString() {
        return this.getName() + " with " + this.getTeacher().getFirstName() + " " + this.getTeacher().getLastName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Class && getId() == ((Class)obj).getId();
    }
}
