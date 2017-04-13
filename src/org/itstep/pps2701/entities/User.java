package org.itstep.pps2701.entities;

import org.itstep.pps2701.entities.enums.User_role;

import java.util.Date;

/**
 * Created by DK-HOME on 07.04.2017.
 */
// класс/сущность Пользователь
public class User {

    private int id;             // служ.поле - идентификатор
    private Date dateOpen;      // служ.поле - штамп времени создания записи
    private Date dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private String login;       // логин пользователя
    private String password;    // пароль пользователя
    private User_role role;     // роль пользователя

    public User() {}

    // конструктор без идентификатора
    public User(Date dateOpen, Date dateClose, String login, String password, User_role role) {
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

    public Date getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Date dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
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
//                "id=" + id +
//                ", dateOpen=" + dateOpen +
//                ", dateClose=" + dateClose +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
