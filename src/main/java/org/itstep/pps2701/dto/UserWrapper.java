package org.itstep.pps2701.dto;

import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.USER_ROLE;
import org.itstep.pps2701.service.ObjectInterface;

import java.sql.Timestamp;

/**
 * Created by DK-HOME on 07.04.2017.
 */
// класс/сущность Пользователь
public class UserWrapper implements ObjectInterface {

    private int id;             // служ.поле - идентификатор
    private Timestamp dateOpen;      // служ.поле - штамп времени создания записи
    private Timestamp dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private String login;       // логин пользователя
    private String password;    // пароль пользователя
    private USER_ROLE role;     // роль пользователя

    public UserWrapper() {}

    public UserWrapper(User item) {

    }

    public UserWrapper(int id, Timestamp dateOpen, Timestamp dateClose, String login, String password, USER_ROLE role) {
        this.id = id;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Timestamp dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Timestamp getDateClose() {
        return dateClose;
    }

    public void setDateClose(Timestamp dateClose) {
        this.dateClose = dateClose;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserWrapper{" +
                "id=" + id +
                ", dateOpen=" + dateOpen +
                ", dateClose=" + dateClose +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public Object[] toObject() {
        return new Object[]{
                getId(),
                getDateOpen(),
                getDateClose(),
                getLogin(),
                getPassword(), // можно зашифровать
                getRole()};
    }
}
