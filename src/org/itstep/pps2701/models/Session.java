package org.itstep.pps2701.models;

import java.sql.*;

public class Session extends SQLException implements ISession{

    private static final String DB_NAME = "watch_store";      // имя базы данных
    private static final String DB_ADRESS = "localhost:3306";   // порт соединения
    private static final String DB_LOGIN = "root";
    private static final String DB_PASSWORD = "123456";

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getDbAdress() {
        return DB_ADRESS;
    }

    public static String getDbLogin() {
        return DB_LOGIN;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public Session() {
//        try {
//            connectionToDb();
//        }catch (SQLException ex){
//            System.out.println(ex.getMessage());
//        }
    }

    /**
     * Метод проверки соединения с БД
     * @return Connection connect
     * @throws SQLException
     */
    private Connection connectionToDb() throws SQLException{
         Connection connect = DriverManager.getConnection(
                "jdbc:mysql://" + DB_ADRESS + "/" + DB_NAME + "?autoReconnect=true&useSSL=false", /*serverName*/
                 DB_LOGIN, /*login*/
                 DB_PASSWORD /*password*/);
        System.out.println("Connection successful!");
        return connect;
    }

    public ResultSet getData(String query) throws SQLException {
        Statement statement = connectionToDb().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        // распарсить полученный обьект в хэш таблицу и передать дальше
        // http://www.javaportal.ru/java/tutorial/tutorialJDBC/statement.html
//        ResultSetMetaData rsmd = resultSet.getMetaData();
//        int columns = rsmd.getColumnCount();
//        ArrayList list = new ArrayList(columns);
//        while (resultSet.next()){
//            HashMap row = new HashMap(columns);
//            for(int i=1; i<=columns; ++i){
//                row.put(rsmd.getColumnName(i),resultSet.getObject(i));
//            }
//            list.add(row);
//        }
//        resultSet.close();
//        statement.close();
//        connectionToDb().close();
        return resultSet;
    }
}
