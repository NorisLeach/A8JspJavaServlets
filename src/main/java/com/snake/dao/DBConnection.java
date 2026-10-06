package com.snake.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/snake_db?useSSL=false&serverTimezone=UTC";
    private static final String USER =  "root";
    private static final String PASSWORD =  "Norisayana1968";

    public static Connection getConnection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (ClassNotFoundException e) {
            throw new SQLException("Driver-ul MySQL nu a fost găsit în proiect!", e);
        }
    }
}
