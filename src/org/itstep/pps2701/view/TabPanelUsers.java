package org.itstep.pps2701.view;

import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.enums.User_role;
import org.itstep.pps2701.models.UserModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// Вкладка пользователей
public class TabPanelUsers extends JPanel{
    private JTable usersTable;                      // таблица с данными о пользователях
    private JPanel tabPanelUsers;                   // панель с элементами вкладки "Пользователи"
    private JButton btnAdd;                         // кнопка добавления новой записи в таблицу пользователей
    private MainFrame parentFrame;                  // родительское окно

    private UserModel userModel = new UserModel();            // интерфейс логики по действиям производимым с пользователями

    /**
     * Конструктор содержимого вкладки "Пользователи"
     * @param tabbedPane в эту вкладку главного окна будет + содержимое панели tabPanelUsers
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
        tabPanelUsers = new JPanel(new BorderLayout(5,5));  // + панель содержимого вкладки "Пользователи"

//        получение данных из БД и доавление их в таблицу для вывод в клиенте
        try {
            usersTable = new JTable(usersTableBuider(userModel.read()));      // + таблица пользователей заполняется по модели
        } catch (SQLException ex) {
            // перехват ошибки при построении таблицы и получении данных из БД
            parentFrame.callErrorDialog(ex.getMessage());
        }

        usersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // режим выбор записи - по одному
        tabPanelUsers.add(new JScrollPane(usersTable), BorderLayout.CENTER); // + таблицу с данными по центру в панель содержимого вкладки "Пользователи"

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        btnAdd = new JButton("Добавить запись"); // + кнопка "Добавить"
        btnAdd.addActionListener(b -> { /* + действие по нажатию на кнопку */
            createAddDialog();   // + запись пользоветеля в бд
        });

        btnPanel.add(btnAdd);                               // + кнопку "Добавить" в панель кнопок
        tabPanelUsers.add(btnPanel, BorderLayout.SOUTH);    // + панель кнопок в панель Пользователей
    }

    // создание диалога для добавления записи в бд
    public void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление пользователя", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JPanel insertDialogPanel = new JPanel();
        insertDialogPanel.setLayout(new GridLayout(7,1));

        insertDialogPanel.add(new JLabel("Логин:"));
        JTextField txtFieldLogin = new JTextField(25);
        txtFieldLogin.setToolTipText("Логин пользователя");
        insertDialogPanel.add(txtFieldLogin);

        insertDialogPanel.add(new JLabel("Пароль:"));
        JPasswordField pswdField = new JPasswordField(25);
        pswdField.setEchoChar('*');
        insertDialogPanel.add(pswdField);

        insertDialogPanel.add(new JLabel("Роль пользователя:"));
        JComboBox comboBoxUserRole = new JComboBox<>(User_role.values());
        comboBoxUserRole.setSize(25,5);
        insertDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Добавить");

        saveBtn.addActionListener(new AbstractAction() {
                                  @Override
                                  public void actionPerformed(ActionEvent e) {
                                    try{
                                        List<User> userList = new ArrayList<>(0);
                                        User user = new User();
                                        // если поля логина и пароля не пустые
                                        if(!"".equals(txtFieldLogin.getText()) && !"".equals(String.valueOf(pswdField.getPassword()))){
                                            user.setDateOpen(new Timestamp(System.currentTimeMillis()));
                                            user.setLogin(txtFieldLogin.getText());
                                            user.setPassword(String.valueOf(pswdField.getPassword()));
                                            user.setRole((User_role)comboBoxUserRole.getSelectedItem());

                                            userList = userModel.create(user); // вызов метода создания пользователя + перестройка данных в таблице
                                        } else {
                                            parentFrame.callErrorDialog("Проверьте правильно ввода данных");
                                        }
// TODO: что-то не то с перерисовкой таблицы
                                        usersTable.setModel(usersTableBuider(userList));
                                        addDialog.dispose();
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                        parentFrame.callErrorDialog(ex.getMessage());
                                    }
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
     * Построитель таблицы пользователей
     * @param usersList
     * @return
     */
    public DefaultTableModel usersTableBuider(List<User> usersList) {
        // шапка таблицы пользователей
        String[] tableHeader = {"id",
                "Дата создания записи",
                "Дата закрытия записи",
                "Логин",
                "Пароль",
                "Роль"};

        // заполнение модели таблицы по умолчанию данными с определенной ранее шапкой таблицы
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, usersList.size());

        // конвертация каждого элемента списка в обьект для добавления в модель таблицы
        for(User user: usersList) {
            tableModel.addRow(user.toObject());
        }
        return tableModel;
    }

}
