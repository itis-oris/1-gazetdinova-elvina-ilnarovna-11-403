package com.oris_sem01.travelplanner.config;

import com.oris_sem01.travelplanner.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    public static Connection getConnection() throws SQLException {
        return ConnectionPool.getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Ошибка закрытия соединения: " + e.getMessage());
            }
        }
    }
}
