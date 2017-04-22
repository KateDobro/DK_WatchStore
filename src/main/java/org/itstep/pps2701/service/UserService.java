package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.UserRepository;
import org.itstep.pps2701.dao.WatchRepository;
import org.itstep.pps2701.dto.UserWrapper;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.USER_ROLE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    @Inject
    private UserRepository userRepository;
    /**
     * Метод добавления записи о пользователе в БД
     * @param userWrapper данные пользователя для сохранения
     * @return  List<UserWrapper> из БД с добавленной записью, для перерисовки таблицы
     * @throws SQLException
     */
    public List<UserWrapper> create(UserWrapper userWrapper) throws SQLException {
        if(Utils.isActiv()) {
            String query = "INSERT INTO watch_store.users (date_open, login, password, role) values (?, ?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
            ps.setTimestamp(1, userWrapper.getDateOpen());
            ps.setString(2, userWrapper.getLogin());
            ps.setString(3, userWrapper.getPassword());
            ps.setString(4, String.valueOf(userWrapper.getRole()));
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    public List<UserWrapper> findAllActive()
    {
        List<UserWrapper> result = new ArrayList<>();

        List<User> all = userRepository.findAllActive();
        for(User item : all)
        {
            result.add(new UserWrapper(item));
        }

        return result;
    }

    /**
     * Метод получения записи из БД по ИД
     * @param id - записи в таблице которую необходимо получить из БД
     * @return UserWrapper
     * @throws SQLException
     */
    public UserWrapper getUserById(int id) throws SQLException{
        UserWrapper userWrapper = null;

        if(Utils.isActiv()) {
            String request = "SELECT * FROM watch_store.users where id = \'" + id + "\' LIMIT 1";

            Statement statement = Utils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            if (resultSet.next()) userWrapper = parseUserItem(resultSet);

            statement.close();
        }

        return userWrapper;
    }

    /**
     * Метод обновления данных записи пользователя
     * @param userWrapper данные о котором будут обновлены
     * @return
     * @throws SQLException
     */
    public List<UserWrapper> update(UserWrapper userWrapper) throws SQLException{
        if(Utils.isActiv()) {
            String updateRequest = "UPDATE watch_store.users SET "
                    + "login = \'" + userWrapper.getLogin()
                    + "\', password = \'" + userWrapper.getPassword()
                    + "\', role = \'" + String.valueOf(userWrapper.getRole())
                    + "\' WHERE id = \'" + userWrapper.getId() + "\';";

            PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    /**
     * "Удаление" записи о пользователе, заполнение поля date_close
     * @param id
     * @return
     * @throws SQLException
     */
    public List<UserWrapper> remove(int id) throws SQLException{
        if(Utils.isActiv()) {
            String request = "UPDATE watch_store.users SET "
                    + "date_close = \'" + new Timestamp(System.currentTimeMillis())
                    + "\' WHERE id = \'" + id + "\';";
            PreparedStatement ps = Utils.getConnection().prepareStatement(request);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    /**
     *  Парсинг записи из бд в обьект "Пользователь"
     * @param resultSet - результат запроса к БД
     * @return
     * @throws SQLException
     */
    private UserWrapper parseUserItem(ResultSet resultSet) throws SQLException{
        return new UserWrapper(
                resultSet.getInt("id"),
                resultSet.getTimestamp("date_open"),
                resultSet.getTimestamp("date_close"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                USER_ROLE.getUser_role(resultSet.getString("role")));
    }
}
