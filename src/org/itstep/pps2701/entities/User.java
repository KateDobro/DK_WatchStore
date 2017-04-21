package org.itstep.pps2701.entities;

import org.itstep.pps2701.enums.User_role;
import org.itstep.pps2701.service.ObjectInterface;

import java.sql.Timestamp;

/**
 * Created by DK-HOME on 07.04.2017.
 */
// класс/сущность Пользователь
public class User implements ObjectInterface {

    private int id;             // служ.поле - идентификатор
    private Timestamp dateOpen;      // служ.поле - штамп времени создания записи
    private Timestamp dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private String login;       // логин пользователя
    private String password;    // пароль пользователя
    private User_role role;     // роль пользователя

    public User() {}

    public User(int id, Timestamp dateOpen, Timestamp dateClose, String login, String password, User_role role) {
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

    public User_role getRole() {
        return role;
    }

    public void setRole(User_role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
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
