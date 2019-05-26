package com.library.DAO;

import com.library.DatabaseManager;
import com.library.Helpers;
import com.library.Models.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TeacherDAO extends ModelDAO<Teacher> {
    private static TeacherDAO instance = new TeacherDAO();
    public static TeacherDAO getInstance() { return instance; }

    @Override
    public String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS teacher(" +
                "id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "first_name VARCHAR(30)     NOT NULL," +
                "last_name  VARCHAR(30)     NOT NULL," +
                "faculty    VARCHAR(30)     ," +
                "pesel      CHARACTER(11)   NOT NULL UNIQUE)";
    }

    @Override
    public Teacher parse(ResultSet resultSet) throws SQLException {
        Teacher teacher = new Teacher(resultSet.getInt("id"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
        teacher.setPesel(resultSet.getString("pesel"));
        teacher.setFaculty(resultSet.getString("faculty"));
        return teacher;
    }

    @Override
    public ArrayList<Teacher> parseMultiple(ResultSet resultSet) throws SQLException {
        ArrayList<Teacher> teachers = new ArrayList<>();
        while (resultSet.next()) {
            teachers.add(this.parse(resultSet));
        }
        return teachers;
    }

    @Override
    public Teacher get(HashMap<String, String> params) throws SQLException {
        String query = "SELECT * FROM teacher";
        query += Helpers.prepare_query_params(params);
        return this.parse(DatabaseManager.getInstance().executeQuery(query));
    }

    public Teacher get(int id) throws SQLException {
        String query = "SELECT * FROM teacher WHERE id=" + id;
        return this.parse(DatabaseManager.getInstance().executeQuery(query));
    }

    @Override
    public ArrayList<Teacher> all() throws SQLException {
        String query = "SELECT * FROM teacher";
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query);

        return this.parseMultiple(resultSet);
    }

    @Override
    public void update(Teacher obj) throws SQLException {
        String query = "UPDATE teacher SET"
                + " first_name='" + obj.getFirstName() + "'"
                + ",last_name='" + obj.getLastName() + "'"
                + ",pesel='" + obj.getPesel() + "'"
                + ",faculty='" + obj.getFaculty() + "'"
                + " WHERE id=" + obj.getId();
        DatabaseManager.getInstance().executeUpdate(query);
    }

    @Override
    public int create(Teacher obj) throws SQLException {
        String query = "INSERT INTO 'teacher' (first_name,last_name,faculty,pesel) VALUES (\n" +
                "  '" + obj.getFirstName() + "',\n" +
                "  '" + obj.getLastName() + "',\n" +
                "  '" + obj.getFaculty() + "',\n" +
                "  '" + obj.getPesel() + "'\n" +
                ");";
        int id = DatabaseManager.getInstance().executeInsert(query);
        if (id == -1) {
            throw new SQLException("Error while trying to CREATE");
        }
        return id;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM teacher WHERE id=" + id;
        DatabaseManager.getInstance().executeUpdate(query);
    }
}
