package org.itstep.pps2701;

import java.sql.*;

public class Utils extends SQLException {
    private static final String DB_NAME = "watch_store";      // имя базы данных
    private static final String DB_ADDRESS = "localhost:3306";// адрес БД (хост + порт)
    private static final String DB_LOGIN = "root";            // имя пользователя
    private static final String DB_PASSWORD = "root";         // пароль
    private static final String DB_URL = "jdbc:mysql://" + DB_ADDRESS + "/" + DB_NAME + "?autoReconnect=true&useSSL=false"; // URL БД

    private static Connection connection;

//    Соединение с БД
    public static void connectionToDb() throws SQLException{
        connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
        System.out.println("Connection successful!");
    }

    // закрытие соединения с БД
    public static void disconnect() throws SQLException{
        connection.close();
    }

    // получение соединения с БД
    public static Connection getConnection() {
        return connection;
    }


}
