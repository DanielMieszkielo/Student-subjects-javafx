package com.library.DAO;

import com.library.DatabaseManager;
import com.library.Helpers;
import com.library.Models.Class;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassDAO extends ModelDAO<Class> {
    private static ClassDAO instance = new ClassDAO();
    public static ClassDAO getInstance() { return instance; }

    @Override
    public String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS class(" +
                "id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "name       VARCHAR(30)     NOT NULL," +
                "teacherId  integer         NOT NULL," +
                "foreign key(teacherId) references teacher(id))";
    }

    @Override
    public Class parse(ResultSet resultSet) throws SQLException {
        Class classObj = new Class(resultSet.getInt("id"));
        classObj.setName(resultSet.getString("name"));
        classObj.setTeacherId(resultSet.getInt("teacherId"));
        return classObj;
    }

    @Override
    public ArrayList<Class> parseMultiple(ResultSet resultSet) throws SQLException {
        ArrayList<Class> classes = new ArrayList<>();
        while (resultSet.next()) {
            classes.add(this.parse(resultSet));
        }
        return classes;
    }

    @Override
    public ArrayList<Class> get(HashMap<String, String> params) throws SQLException {
        String query = "SELECT * FROM class";
        query += Helpers.prepare_query_params(params);
        return this.parseMultiple(DatabaseManager.getInstance().executeQuery(query));
    }

    public Class get(int id) throws SQLException {
        String query = "SELECT * FROM class WHERE id=" + id;
        return this.parse(DatabaseManager.getInstance().executeQuery(query));
    }

    @Override
    public ArrayList<Class> all() throws SQLException {
        String query = "SELECT * FROM class";
        ResultSet resultSet = DatabaseManager.getInstance().executeQuery(query);

        return this.parseMultiple(resultSet);
    }

    @Override
    public void update(Class obj) throws SQLException {
        String query = "UPDATE class SET"
                + " name='" + obj.getName() + "'"
                + ",teacherId='" + obj.getTeacher().getId() + "'"
                + " WHERE id=" + obj.getId();
        DatabaseManager.getInstance().executeUpdate(query);
    }

    @Override
    public int create(Class obj) throws SQLException {
        String query = "INSERT INTO class (name,teacherId) VALUES (\n" +
                "  '" + obj.getName() + "'," +
                "  '" + obj.getTeacher().getId() + "'" +
                ");";
        int id = DatabaseManager.getInstance().executeInsert(query);
        if (id == -1) {
            throw new SQLException("Error while trying to CREATE");
        }
        return id;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM class WHERE id=" + id;
        DatabaseManager.getInstance().executeUpdate(query);
    }
}
