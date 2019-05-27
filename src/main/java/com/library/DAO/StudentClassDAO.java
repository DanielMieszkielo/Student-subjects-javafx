package com.library.DAO;

import com.library.DatabaseManager;
import com.library.Helpers;
import com.library.Models.Class;
import com.library.Models.StudentClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentClassDAO extends ModelDAO<StudentClass> {
    private static StudentClassDAO instance = new StudentClassDAO();
    public static StudentClassDAO getInstance() { return instance; }

    @Override
    public String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS student_class(" +
                "id         INTEGER     PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "studentId  integer     NOT NULL," +
                "classId    integer     NOT NULL," +
                "foreign key(studentId) references student(id)," +
                "foreign key(classId) references class(id))";
    }

    @Override
    public StudentClass parse(ResultSet resultSet) throws SQLException {
        StudentClass studentClass = new StudentClass(resultSet.getInt("id"));
        studentClass.setClassObj(resultSet.getInt("classId"));
        studentClass.setStudentId(resultSet.getInt("studentId"));
        return studentClass;
    }

    @Override
    public ArrayList<StudentClass> parseMultiple(ResultSet resultSet) throws SQLException {
        ArrayList<StudentClass> studentClasses = new ArrayList<>();
        while (resultSet.next()) {
            studentClasses.add(this.parse(resultSet));
        }
        return studentClasses;
    }

    @Override
    public ArrayList<StudentClass> get(HashMap<String, String> params) throws SQLException {
        String query = "SELECT * FROM student_class";
        query += Helpers.prepare_query_params(params);
        return this.parseMultiple(DatabaseManager.getInstance().executeQuery(query));
    }

    public StudentClass get(int id) throws SQLException {
        String query = "SELECT * FROM student_class WHERE id=" + id;
        return this.parse(DatabaseManager.getInstance().executeQuery(query));
    }

    @Override
    public ArrayList<StudentClass> all() throws SQLException {
        String query = "SELECT * FROM student_class";
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query);

        return this.parseMultiple(resultSet);
    }

    public ArrayList<Class> classesByStudentId(int studentId) throws SQLException {
        String query = "SELECT c.id, c.name, c.teacherId FROM student_class " +
                "JOIN class c on student_class.classId = c.id WHERE studentId=" + studentId;
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query);
        ClassDAO classDAO = new ClassDAO();

        return classDAO.parseMultiple(resultSet);
    }

    @Override
    public void update(StudentClass obj) throws SQLException {
        String query = "UPDATE student_class SET"
                + " id='" + obj.getId() + "'"
                + ",studentId='" + obj.getStudent().getId() + "'"
                + ",classId='" + obj.getClassObj().getId() + "'"
                + " WHERE id=" + obj.getId();
        DatabaseManager.getInstance().executeUpdate(query);
    }

    @Override
    public int create(StudentClass obj) throws SQLException {
        String query = "INSERT INTO student_class (studentId,classId) VALUES (\n" +
                "  '" + obj.getStudent().getId() + "'," +
                "  '" + obj.getClassObj().getId() + "'" +
                ");";
        int id = DatabaseManager.getInstance().executeInsert(query);
        if (id == -1) {
            throw new SQLException("Error while trying to CREATE");
        }
        return id;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM student_class WHERE id=" + id;
        DatabaseManager.getInstance().executeUpdate(query);
    }
}
