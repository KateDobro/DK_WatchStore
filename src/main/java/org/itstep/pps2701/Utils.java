package org.itstep.pps2701;

import java.sql.*;

public class Utils {
    private static final String DB_NAME = "watch_store";      // имя базы данных
    private static final String DB_ADDRESS = "localhost:3306";// адрес БД (хост + порт)
    private static final String DB_LOGIN = "root";            // имя пользователя
    private static final String DB_PASSWORD = "root";         // пароль
    private static final String DB_URL = "jdbc:mysql://" + DB_ADDRESS + "/" + DB_NAME + "?autoReconnect=true&useSSL=false"; // URL БД

    private static Connection connection=null;

//    Соединение с БД
    static {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD);
        }catch(SQLException ex) { ex.printStackTrace(); }
    }

    // закрытие соединения с БД
    public static void disconnect() {
        try{
            if(connection!=null)
                connection.close();connection.isClosed();
        }catch(SQLException ex) { ex.printStackTrace(); }
    }

    // получение соединения с БД
    public static boolean isActiv() {
        boolean res=false;

        try{
            if(connection!=null)
                res=!connection.isClosed();
        }catch(SQLException ex) { ex.printStackTrace(); }

        return res;
    }

    // получение соединения с БД
    public static Connection getConnection() {
        return connection;
    }


}
