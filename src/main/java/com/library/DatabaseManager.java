package com.library;

import com.library.DAO.ModelDAO;
import com.settings;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager ourInstance = new DatabaseManager();
    public static DatabaseManager getInstance() { return ourInstance; }

    private String fileName;

    private Connection connection;

    private String getUrl() {
        return settings.URL;
    }

    private String getFullUrl() {
        return "jdbc:sqlite:" + getUrl() + fileName;
    }

    private DatabaseManager() {
        this.createDatabase(settings.dbFileName);
    }

    private void createDatabase(String fileName) {
        this.fileName = fileName;

        try {
            DatabaseMetaData meta = this.getConnection().getMetaData();
            System.out.println("Driver name: " + meta.getDriverName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.getFullUrl());
                if (this.connection == null) {
                    throw new SQLException("Can't establish connection ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        return this.connection;
    }

    public void registerModels(List<Class<? extends ModelDAO>> models) throws SQLException {
        for (Class<? extends ModelDAO> model : models) {
            registerModel(model);
        }
    }

    public void registerModel(Class<? extends ModelDAO> model) throws SQLException {
        String sql = null;
        try {
            sql = ((ModelDAO)model.getMethod("getInstance").invoke(null)).createTableSql();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.tryToCreateTable(sql);
    }

    private void tryToCreateTable(String sql) throws SQLException {
        this.getConnection().createStatement().execute(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        ResultSet rs = this.getConnection().createStatement().executeQuery(sql);
        return rs;
    }

    public void executeUpdate(String sql) throws SQLException {
        this.getConnection().createStatement().executeUpdate(sql);
    }

    public int executeInsert(String sql) throws SQLException {
        PreparedStatement stmtInsert = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmtInsert.executeUpdate();

        ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
        return (int) generatedKeys.getLong(1);
    }
}
