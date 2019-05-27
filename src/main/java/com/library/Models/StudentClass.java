package com.library.Models;

import com.library.DAO.ClassDAO;
import com.library.DAO.StudentClassDAO;
import com.library.DAO.StudentDAO;

import java.sql.SQLException;

public class StudentClass extends BaseModel {
    private int id;
    private Student student;
    private Class classObj;

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setStudentId(int studentId) throws SQLException {
        this.student = StudentDAO.getInstance().get(studentId);
    }

    public void setClassObj(int classId) throws SQLException {
        this.classObj = ClassDAO.getInstance().get(classId);
    }

    public Student getStudent() {
        return student;
    }

    public Class getClassObj() {
        return classObj;
    }

    public StudentClass(int id) {
        this.id = id;
    }
    public StudentClass() {}

    @Override
    public void save() throws SQLException {
        StudentClassDAO.getInstance().update(this);
    }

    public static StudentClass create(int studentId, int classId) throws SQLException {
        StudentClass studentClass = new StudentClass();
        studentClass.setStudentId(studentId);
        studentClass.setClassObj(classId);

        studentClass.setId(StudentClassDAO.getInstance().create(studentClass));
        return studentClass;
    }

    public static void delete(StudentClass teacher) throws SQLException {
        StudentClassDAO.getInstance().delete(teacher.getId());
    }
}
