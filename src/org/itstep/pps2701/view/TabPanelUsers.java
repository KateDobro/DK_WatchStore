package org.itstep.pps2701.view;

import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.User_role;
import org.itstep.pps2701.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

// Вкладка пользователей
public class TabPanelUsers extends JPanel{
    private static JLabel lblLogin = new JLabel("Логин");
    private static JLabel lblPassword = new JLabel("Пароль");
    private static JLabel lblRole = new JLabel("Роль");

    private JTable usersTable;                      // таблица с данными о пользователях
    private JPanel tabPanelUsers;                   // панель с элементами вкладки "Пользователи"
    private MainFrame parentFrame;                  // родительское окно
    private UserService userService = new UserService(); // действия производимые с пользователями

    /**
     * Конструктор содержимого вкладки "Пользователи"
     * @param parentFrame родительское окно для вывода диалоговых окон
     */
    public TabPanelUsers(JTabbedPane tabbedPane, MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        buildTabPanelUsers();
        tabbedPane.addTab("Пользователи", tabPanelUsers);
    }

    /**
     * Наполнение панели tabPanelUsers содержимым
     */
    private void buildTabPanelUsers() {
        tabPanelUsers = new JPanel(new BorderLayout(5, 5));

//        получение данных из БД и доавление их в таблицу для вывод в клиенте
        try { usersTable = new JTable(tableModelBuider(userService.read()));      // + таблица пользователей заполняется по модели
        } catch (SQLException ex) {
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage()); // перехват ошибки при построении таблицы и получении данных из БД
        }

        tabPanelUsers.add(new JScrollPane(usersTable), BorderLayout.CENTER); // + таблицу с данными по центру в панель содержимого вкладки "Пользователи"

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        JButton addBtn = new JButton("Добавить");
        addBtn.addActionListener(b -> createAddDialog());
        JButton editBtn = new JButton("Редактировать выбранную запись");
        editBtn.addActionListener(b -> createEditDialog(usersTable.getSelectedRow()));
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        tabPanelUsers.add(btnPanel, BorderLayout.SOUTH);    // + панель кнопок в панель Пользователей
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление пользователя", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JPanel insertDialogPanel = new JPanel();
        insertDialogPanel.setLayout(new GridLayout(7,1));

        insertDialogPanel.add(lblLogin);
        JTextField txtFieldLogin = new JTextField(25);
        txtFieldLogin.setToolTipText(lblLogin.getText());
        insertDialogPanel.add(txtFieldLogin);

        insertDialogPanel.add(lblPassword);
        JPasswordField pswdField = new JPasswordField(25);
        pswdField.setEchoChar('*');
        insertDialogPanel.add(pswdField);

        insertDialogPanel.add(lblRole);
        JComboBox comboBoxUserRole = new JComboBox<>(User_role.values());
        comboBoxUserRole.setSize(25,5);
        insertDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener( b -> {
            try{
                User user = new User();
                // если поля логина и пароля не пустые
                if(!"".equals(txtFieldLogin.getText()) && !"".equals(String.valueOf(pswdField.getPassword()))){
                    user.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    user.setLogin(txtFieldLogin.getText());
                    user.setPassword(String.valueOf(pswdField.getPassword()));
                    user.setRole((User_role)comboBoxUserRole.getSelectedItem());

                    List<User> userList = userService.create(user); // вызов метода создания пользователя

                    usersTable.setModel(tableModelBuider(userList)); // перестройка данных в таблице
                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте правильно ввода данных");
                }

            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> addDialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        insertDialogPanel.add(btnPanel, "south");
        addDialog.add(insertDialogPanel);
        addDialog.pack();
        addDialog.setVisible(true);
    }

    /**
     * создание диалогового окна редактирования записи пользователя
     * @param selectedRow номер выбранного ряда таблицы
     */
    private void createEditDialog(int selectedRow) {
        JDialog editDialog = new JDialog(parentFrame, "Редактирование пользователя", true);
        editDialog.setLocationRelativeTo(parentFrame);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));

        int id = (Integer) usersTable.getValueAt(selectedRow, 0);

        User user = null;
        try{ user = userService.getUserById(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        editDialogPanel.add(lblLogin);
        JTextField txtFieldLogin = new JTextField(user.getLogin(), 25);
        txtFieldLogin.setToolTipText(lblLogin.getText());
        editDialogPanel.add(txtFieldLogin);

        editDialogPanel.add(lblPassword);
        JPasswordField pswdField = new JPasswordField(user.getPassword(), 25);
        pswdField.setEchoChar('*');
        editDialogPanel.add(pswdField);

        editDialogPanel.add(lblRole);
        JComboBox comboBoxUserRole = new JComboBox<>(User_role.values());
        comboBoxUserRole.setSelectedItem(user.getRole());
        comboBoxUserRole.setSize(25,5);
        editDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton updateBtn = new JButton("Сохранить");
        updateBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    User userFin = null;
                    if(!txtFieldLogin.getText().trim().isEmpty() && pswdField.getPassword().length>0) {
                        userFin = new User();
                        userFin.setId(id);
                        userFin.setLogin(txtFieldLogin.getText());
                        userFin.setPassword(String.valueOf(pswdField.getPassword()));
                        userFin.setRole((User_role) comboBoxUserRole.getSelectedItem());

                        List<User> userList = userService.update(userFin); // вызов метода обновления данных пользователя + перестройка данных в таблице
                        usersTable.setModel(tableModelBuider(userList));
                        editDialog.dispose();
                    } else {
                        parentFrame.createErrorDialog("Проверьте правильно ввода данных");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        JButton removeBtn = new JButton("Удалить");
        removeBtn.addActionListener(b -> {
            try{
                List<User> userList = userService.remove(id);
                usersTable.setModel(tableModelBuider(userList));
                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> editDialog.dispose());

        btnPanel.add(updateBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(cancelBtn);

        editDialogPanel.add(btnPanel, "south");
        editDialog.add(editDialogPanel);
        editDialog.pack();
        editDialog.setVisible(true);
    }

    /**
     * Построитель таблицы пользователей
     * @param usersList
     * @return
     */
    private DefaultTableModel tableModelBuider(List<User> usersList) {
        // шапка таблицы пользователей
        String[] tableHeader = {"id", "Дата создания записи", "Дата закрытия записи", "Логин", "Пароль", "Роль"};
        // заполнение модели таблицы по умолчанию данными с определенной ранее шапкой таблицы
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);
        // конвертация каждого элемента списка в обьект для добавления в модель таблицы
        for(User user: usersList) {
            tableModel.addRow(user.toObject());
        }
        return tableModel;
    }

}
