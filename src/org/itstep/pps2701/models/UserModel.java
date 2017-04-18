package org.itstep.pps2701.models;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.User_role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    // TODO : метод добавления записи в таблицу
    public List<User> create(User user) throws SQLException {
        String query = "INSERT INTO watch_store.users (date_open, login, password, role) values (?, ?, ?, ?)";

        PreparedStatement ps = Utils.getConnection().prepareStatement(query);
        ps.setTimestamp(1, user.getDateOpen());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getPassword());
        ps.setString(4, String.valueOf(user.getRole()));
        ps.executeUpdate();
        ps.close();

        List<User> userList = read();

        return userList;
    }

    /**
     * Данные из бд в список
     * @return List<User>
     * @throws SQLException
     */
    // TODO: метод чтения данных из таблицы
    public List<User> read() throws SQLException {

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
                resultSet.getTimestamp("date_open"),
                resultSet.getTimestamp("date_close"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                User_role.getUser_role(resultSet.getString("role")));
    }

// TODO: метод редактирования данных из таблицы


// TODO: метод удаления данных из таблицы



}
