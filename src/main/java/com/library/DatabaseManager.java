package com.library;

import com.library.DAO.ModelDAO;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager ourInstance = new DatabaseManager();
    public static DatabaseManager getInstance() { return ourInstance; }

    private String fileName;

    public String getUrl() {
        return settings.URL;
    }

    public String getFullUrl() {
        return "jdbc:sqlite:" + getUrl() + fileName;
    }

    private DatabaseManager() {
        this.createDatabase("db.sqlite");
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.getFullUrl());
            if (connection == null) {
                throw new SQLException("Can't establish connection ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void registerModel(Class<? extends ModelDAO> model) {
        try {
            String sql = ((ModelDAO)model.getMethod("getInstance").invoke(null)).createTableSql();
            this.tryToCreateTable(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void tryToCreateTable(String sql) throws SQLException {
        try (Connection conn = this.getConnection()) {
            conn.createStatement().execute(sql);
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try (Connection conn = this.getConnection()) {
            return conn.createStatement().executeQuery(sql);
        }
    }

    public void executeUpdate(String sql) throws SQLException {
        try (Connection conn = this.getConnection()) {
            conn.createStatement().executeUpdate(sql);
        }
    }

    public int executeInsert(String sql) {
        try (Connection conn = this.getConnection()) {
            PreparedStatement stmtInsert = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmtInsert.executeUpdate();

            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            return (int) generatedKeys.getLong(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
