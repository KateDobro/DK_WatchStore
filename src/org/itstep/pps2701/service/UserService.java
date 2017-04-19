package org.itstep.pps2701.service;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.User_role;

import javax.jws.soap.SOAPBinding;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    /**
     * Метод добавления записи о пользователе в БД
     * @param user данные пользователя для сохранения
     * @return  List<User> из БД с добавленной записью, для перерисовки таблицы
     * @throws SQLException
     */
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
     * Получение данных из БД
     * @return List<User>
     * @throws SQLException
     */
    // TODO: метод чтения данных из таблицы
    public List<User> read() throws SQLException {
        List<User> userList = new ArrayList<>();
//        String request = "SELECT * FROM watch_store.users WHERE users.date_close IS NOT NULL"; // запрос для вывода записей которые не помечены как удаленные
        String request = "SELECT * FROM watch_store.users";             // текст запроса
        Statement statement = Utils.getConnection().createStatement();  // + оператор запроса
        ResultSet resultSet = statement.executeQuery(request);          // выполнить запрос

        while (resultSet.next()){
            userList.add(parseUserItem(resultSet));
        }
        statement.close(); // закрываем оператор запроса
        return userList;
    }

    /**
     * Метод получения записи из БД по ИД
     * @param id - записи в таблице которую необходимо получить из БД
     * @return User
     * @throws SQLException
     */
    public User getUserById(int id) throws SQLException{
        String request = "SELECT * FROM watch_store.users where id = \'" + id + "\'";
        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        User user = new User();
        while(resultSet.next()) {
            user = parseUserItem(resultSet);
        } // while

        statement.close();
        return user;
    }

    /**
     * Метод обновления данных записи пользователя
     * @param user данные о котором будут обновлены
     * @return
     * @throws SQLException
     */
    public List<User> update(User user) throws SQLException{
        String updateRequest = "UPDATE watch_store.users SET "
                                + "login = \'" + user.getLogin()
                                + "\', password = \'" + user.getPassword()
                                + "\', role = \'" + String.valueOf(user.getRole())
                                + "\' WHERE id = \'" + user.getId() + "\';";

        PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getPassword());
        ps.setString(4, String.valueOf(user.getRole()));
        ps.executeUpdate();
        ps.close();

        List <User> userList = read();

        return userList;
    }

    /**
     * "Удаление" записи о пользователе, заполнение поля date_close
     * @param id
     * @return
     * @throws SQLException
     */
    public List<User> removeUser(int id) throws SQLException{
        String request = "UPDATE watch_store.users SET "
                                + "date_close = \'" + new Timestamp(System.currentTimeMillis())
                                + "\' WHERE id = \'" + id + "\';";
        PreparedStatement ps = Utils.getConnection().prepareStatement(request);
        ps.executeUpdate();
        ps.close();

        List<User> userList = read();
        return userList;
    }

    /**
     *  Парсинг записи из бд в обьект "Пользователь"
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
}
