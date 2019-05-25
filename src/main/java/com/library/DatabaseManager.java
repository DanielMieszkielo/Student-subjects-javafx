package com.library;

import com.library.DAO.ModelDAO;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager ourInstance = new DatabaseManager();
    public static DatabaseManager getInstance() { return ourInstance; }

    private String url;

    private DatabaseManager() {
        this.createDatabase("db.sqlite");
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            if (connection == null) {
                throw new SQLException("Can't establish connection ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private void createDatabase(String filename) {
        this.url = "jdbc:sqlite:/home/sinma/" + filename;

        try {
            DatabaseMetaData meta = this.getConnection().getMetaData();
            System.out.println("Driver name: " + meta.getDriverName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        Statement stmt = this.getConnection().createStatement();
        stmt.execute(sql);
    }

    public ResultSet executeQuery(String sql, boolean result) {
        try (Connection conn = this.getConnection()) {
            if (result) {
                return conn.createStatement().executeQuery(sql);
            } else {
                conn.createStatement().executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try (Connection conn = this.getConnection()) {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
