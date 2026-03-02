package com.smartcare.smartcaredesktop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/smartcare";
    private static final String USER = "postgres";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to PostgreSQL ✅");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
