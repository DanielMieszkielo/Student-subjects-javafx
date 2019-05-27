package com.library.DAO;

import com.library.Models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ModelDAO<T> {
    public abstract String createTableSql();
    public abstract T parse(ResultSet resultSet) throws SQLException;
    public abstract ArrayList<T> parseMultiple(ResultSet resultSet) throws SQLException;
    public abstract ArrayList<T> get(HashMap<String, String> params) throws SQLException;
    public abstract ArrayList<T> all() throws SQLException;
    public abstract void update(T obj) throws SQLException;
    public abstract int create(T obj) throws SQLException;
    public abstract void delete(int id) throws SQLException;
}
