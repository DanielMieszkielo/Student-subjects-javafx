package com.library.DAO;

import com.library.DatabaseManager;
import com.library.Helpers;
import com.library.Models.Teacher;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                "added_date DATE  DEFAULT (date('now', 'localtime')))";
    }

    @Override
    public Teacher parse(ResultSet resultSet) throws SQLException {
        Teacher teacher = new Teacher(resultSet.getInt("id"));
        teacher.setAddedDate(resultSet.getString("added_date"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
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
    public ArrayList<Teacher> get(HashMap<String, String> params) throws SQLException {
        String query = "SELECT * FROM teacher";
        query += Helpers.prepare_query_params(params);
        return this.parseMultiple(DatabaseManager.getInstance().executeQuery(query));
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
                + ",faculty='" + obj.getFaculty() + "'"
                + " WHERE id=" + obj.getId();
        DatabaseManager.getInstance().executeUpdate(query);
    }

    @Override
    public int create(Teacher obj) throws SQLException {
        String query = "INSERT INTO 'teacher' (first_name,last_name,faculty) VALUES (" +
                "  '" + obj.getFirstName() + "'," +
                "  '" + obj.getLastName() + "'," +
                "  '" + obj.getFaculty() + "'" +
                ");";
        int id = DatabaseManager.getInstance().executeInsert(query);
        obj.setAddedDate(TeacherDAO.getInstance().get(id).getAddedDate());

        return id;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM teacher WHERE id=" + id;
        DatabaseManager.getInstance().executeUpdate(query);
    }
}
