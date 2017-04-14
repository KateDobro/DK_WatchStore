package org.itstep.pps2701.interfaces;

import org.itstep.pps2701.entities.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public interface UserInterface {

    // TODO: создание записи
    // чтение
    List<User> getData() throws SQLException;
    // TODO: редактирование записи
    // TODO: удаление записи CRUD
}
