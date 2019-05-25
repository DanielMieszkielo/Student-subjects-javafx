package com.library.DAO;

import com.library.DatabaseManager;
import com.library.Helpers;
import com.library.Models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentDAO extends ModelDAO<Student> {
    private static StudentDAO instance = new StudentDAO();
    public static StudentDAO getInstance() { return instance; }

    @Override
    public String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS student(" +
                "id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "first_name VARCHAR(30)     NOT NULL," +
                "last_name  VARCHAR(30)     NOT NULL," +
                "pesel      CHARACTER(11)   NOT NULL UNIQUE)";
    }

    private Student parseStudent(ResultSet resultSet) {
        Student student = null;
        try {
            student = new Student(resultSet.getInt("id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
            student.setPesel(resultSet.getString("pesel"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    private ArrayList<Student> parseStudents(ResultSet resultSet) {
        ArrayList<Student> students = new ArrayList<>();

        try {
            while (resultSet.next()) {
                students.add(this.parseStudent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public Student get(HashMap<String, String> params) {
        String query = "SELECT * FROM student";
        query += Helpers.prepare_query_params(params);
        ResultSet rs = DatabaseManager.getInstance().executeQuery(query, true);

        return this.parseStudent(rs);
    }

    @Override
    public ArrayList<Student> all() {
        String query = "SELECT * FROM student";
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query, true);

        return this.parseStudents(resultSet);
    }

    @Override
    public void update(Student student) {
        String query = "UPDATE student SET"
                + " first_name='" + student.getFirstName() + "'"
                + ",last_name='" + student.getLastName() + "'"
                + ",pesel='" + student.getPesel() + "'"
                + " WHERE id=" + student.getId();
        DatabaseManager.getInstance().executeQuery(query, false);
    }

    @Override
    public int create(Student student) {
        String query = "INSERT INTO 'student' (first_name,last_name,pesel) VALUES (\n" +
                "  '" + student.getFirstName() + "',\n" +
                "  '" + student.getLastName() + "',\n" +
                "  '" + student.getPesel() + "'\n" +
                ");";
        DatabaseManager.getInstance().executeQuery(query, false);

        HashMap<String, String> params = new HashMap<>();
        params.put("first_name", student.getFirstName());
        params.put("last_name", student.getLastName());
        params.put("pesel", student.getPesel());
        return this.get(params).getId();
    }
}
