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

    @Override
    public Student parse(ResultSet resultSet) throws SQLException {
        Student student = new Student(resultSet.getInt("id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setPesel(resultSet.getString("pesel"));
        return student;
    }

    @Override
    public ArrayList<Student> parseMultiple(ResultSet resultSet) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            students.add(this.parse(resultSet));
        }
        return students;
    }

    @Override
    public ArrayList<Student> get(HashMap<String, String> params) throws SQLException {
        String query = "SELECT * FROM student";
        query += Helpers.prepare_query_params(params);
        ResultSet rs = DatabaseManager.getInstance().executeQuery(query);

        return this.parseMultiple(rs);
    }

    public Student get(int id) throws SQLException {
        String query = "SELECT * FROM student WHERE id=" + id;
        return this.parse(DatabaseManager.getInstance().executeQuery(query));
    }

    @Override
    public ArrayList<Student> all() throws SQLException {
        String query = "SELECT * FROM student";
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query);

        return this.parseMultiple(resultSet);
    }

    @Override
    public void update(Student student) throws SQLException {
        String query = "UPDATE student SET"
                + " first_name='" + student.getFirstName() + "'"
                + ",last_name='" + student.getLastName() + "'"
                + ",pesel='" + student.getPesel() + "'"
                + " WHERE id=" + student.getId();
        DatabaseManager.getInstance().executeUpdate(query);
    }

    @Override
    public int create(Student student) throws SQLException {
        String query = "INSERT INTO 'student' (first_name,last_name,pesel) VALUES (\n" +
                "  '" + student.getFirstName() + "',\n" +
                "  '" + student.getLastName() + "',\n" +
                "  '" + student.getPesel() + "'\n" +
                ");";
        int id = DatabaseManager.getInstance().executeInsert(query);
        if (id == -1) {
            throw new SQLException("Error while trying to CREATE");
        }
        return id;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM student WHERE id=" + id;
        DatabaseManager.getInstance().executeUpdate(query);
    }
}
