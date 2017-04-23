package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.UserRepository;
import org.itstep.pps2701.dto.UserWrapper;
import org.itstep.pps2701.entities.User;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
            if(userWrapper != null){
                User user = userWrapper.fromWrapper();
                // TODO: проверка на наличие пользователя с такими же данными в бд
                userRepository.save(user);
            }
        }
        return findAllActive();
    }

    /**
     * Поиск всех "активных" записей, где нет "Даты закрытия"
     * @return список записей из бд
     */
    public List<UserWrapper> findAllActive() {
        List<UserWrapper> result = new ArrayList<>();

        List<User> all = userRepository.findAllActive();
        for(User item : all) {
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
    public UserWrapper read(String id) throws SQLException{
        UserWrapper userWrapper = null;

        if(Utils.isActiv()) {
            if(id != null){
                User user = userRepository.getOne(Long.parseLong(id));
                userWrapper = new UserWrapper(user);
            }
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
            if(userWrapper.getId() != null){
            User user = userRepository.getOne(Long.parseLong(userWrapper.getId()));
                user.setLogin(userWrapper.getLogin());
                user.setPassword(userWrapper.getPassword());
                user.setRole(userWrapper.getRole());
                userRepository.save(user);
            }
        }
        return findAllActive();
    }

    /**
     * "Удаление" записи о пользователе, заполнение поля date_close
     * @param id wrapper
     * @return
     * @throws SQLException
     */
    public List<UserWrapper> delete(String id) throws SQLException{
        if(Utils.isActiv()) {
            if(id != null){
                User user = userRepository.getOne(Long.parseLong(id));
                user.setDateClose(new Date());

                userRepository.save(user);
//            userRepository.delete(user); // удаляет полностью запись в базе
            }
        }
        return findAllActive();
    }


    // TODO: метод для логирования пользователя:
    // 1 - отправка введенных данныъ
    // 2 - проверка по пришедшим данным о наличии такой записи в бд
    // 3 - разрешение на вход
    // userRepository.findByLogin(String login)
}
