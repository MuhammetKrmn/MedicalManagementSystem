package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Kendi bilgilerine göre buraları kontrol et knk
    private static final String URL = "jdbc:postgresql://localhost:5432/medical_db";
    private static final String USER = "postgres";
    private static final String PASS = "CC77..es7583";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}