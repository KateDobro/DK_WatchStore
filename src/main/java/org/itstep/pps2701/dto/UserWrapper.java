package org.itstep.pps2701.dto;

import lombok.Data;
import org.itstep.pps2701.DateUtils.DateUtils;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.User_role;
import org.itstep.pps2701.service.ObjectInterface;

import java.util.Date;

@Data
public class UserWrapper implements ObjectWrapper<User>, ObjectInterface {

    private String id;
    private String login;
    private String password;
    private User_role role = User_role.USER;
    private Date dateOpen;
    private Date dateClose;

    public UserWrapper() {}

    public UserWrapper(User item) {
        toWrapper(item);
    }

    public UserWrapper(String id, String login, String password, User_role role, Date dateOpen, Date dateClose) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
    }

    @Override
    public void toWrapper(User item) {
        if (item != null){
            if (item.getId() != null)
                id = String.valueOf(item.getId());

            login = item.getLogin();
            password = item.getPassword();
            role = item.getRole();
            dateOpen = item.getDateOpen();
            dateClose = item.getDateClose();

//            dateOpen = DateUtils.getDateTimeFormat(item.getDateOpen());
//            dateClose = DateUtils.getDateTimeFormat(item.getDateClose());
        }
    }

    @Override
    public User fromWrapper() {
        User user = new User();
        try { user.setId(Long.parseLong(id)); } catch (Exception ex) {}

        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setDateOpen(dateOpen);
        user.setDateClose(dateClose);

        return user;
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
                getLogin(),
                getPassword(), // можно зашифровать
                getRole(),
                getDateOpen(),
                getDateClose(),
        };
    }
}
