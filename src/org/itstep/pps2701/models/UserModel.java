package org.itstep.pps2701.models;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.entities.enums.User_role;
import org.itstep.pps2701.interfaces.UserInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserModel implements UserInterface {

    /**
     * Данные из бд в список
     * @return List<User>
     * @throws SQLException
     */
    @Override
    public List<User> getData() throws SQLException {
        List<User> userList = new ArrayList<>();
//        String request = "SELECT id, login, password, role FROM watch_store.users"; // текст запроса
        String request = "SELECT * FROM watch_store.users"; // текст запроса
        Statement statement = Utils.getConnection().createStatement();            // + оператор запроса
        ResultSet resultSet = statement.executeQuery(request); // выполнить запрос

        while (resultSet.next()){
            userList.add(parseUserItem(resultSet));
        }
        statement.close(); // закрываем оператор запроса
        return userList;
    }

    /**
     *  парсинг записи из бд в обьект "Пользователь"
     * @param resultSet - результат запроса к БД
     * @return
     * @throws SQLException
     */
    private User parseUserItem(ResultSet resultSet) throws SQLException{
        return new User(
                resultSet.getInt("id"),
                resultSet.getTimestamp("date_close"),
                resultSet.getTimestamp("date_open"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                User_role.valueOf(resultSet.getString("role")));
    }



}
