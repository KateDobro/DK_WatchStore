package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dto.UserWrapper;
import org.itstep.pps2701.enums.USER_ROLE;
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

    private UserService userService; // действия производимые с пользователями

    /**
     * Конструктор содержимого вкладки "Пользователи"
     * @param parentFrame родительское окно для вывода диалоговых окон
     */
    public TabPanelUsers(JTabbedPane tabbedPane, MainFrame parentFrame) {
        userService= Utils.getInjector().getInstance(UserService.class);
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
        usersTable = new JTable(tableModelBuider(userService.findAllActive()));      // + таблица пользователей заполняется по модели

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
        JComboBox comboBoxUserRole = new JComboBox<>(USER_ROLE.values());
        comboBoxUserRole.setSize(25,5);
        insertDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener( b -> {
            try{
                UserWrapper userWrapper = new UserWrapper();
                // если поля логина и пароля не пустые
                if(!"".equals(txtFieldLogin.getText()) && !"".equals(String.valueOf(pswdField.getPassword()))){
                    userWrapper.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    userWrapper.setLogin(txtFieldLogin.getText());
                    userWrapper.setPassword(String.valueOf(pswdField.getPassword()));
                    userWrapper.setRole((USER_ROLE)comboBoxUserRole.getSelectedItem());

                    List<UserWrapper> userWrapperList = userService.create(userWrapper); // вызов метода создания пользователя

                    usersTable.setModel(tableModelBuider(userWrapperList)); // перестройка данных в таблице
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

        UserWrapper userWrapper = null;
        try{ userWrapper = userService.getUserById(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        editDialogPanel.add(lblLogin);
        JTextField txtFieldLogin = new JTextField(userWrapper.getLogin(), 25);
        txtFieldLogin.setToolTipText(lblLogin.getText());
        editDialogPanel.add(txtFieldLogin);

        editDialogPanel.add(lblPassword);
        JPasswordField pswdField = new JPasswordField(userWrapper.getPassword(), 25);
        pswdField.setEchoChar('*');
        editDialogPanel.add(pswdField);

        editDialogPanel.add(lblRole);
        JComboBox comboBoxUserRole = new JComboBox<>(USER_ROLE.values());
        comboBoxUserRole.setSelectedItem(userWrapper.getRole());
        comboBoxUserRole.setSize(25,5);
        editDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton updateBtn = new JButton("Сохранить");
        updateBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    UserWrapper userWrapperFin = null;
                    if(!txtFieldLogin.getText().trim().isEmpty() && pswdField.getPassword().length>0) {
                        userWrapperFin = new UserWrapper();
                        userWrapperFin.setId(id);
                        userWrapperFin.setLogin(txtFieldLogin.getText());
                        userWrapperFin.setPassword(String.valueOf(pswdField.getPassword()));
                        userWrapperFin.setRole((USER_ROLE) comboBoxUserRole.getSelectedItem());

                        List<UserWrapper> userWrapperList = userService.update(userWrapperFin); // вызов метода обновления данных пользователя + перестройка данных в таблице
                        usersTable.setModel(tableModelBuider(userWrapperList));
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
                List<UserWrapper> userWrapperList = userService.remove(id);
                usersTable.setModel(tableModelBuider(userWrapperList));
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
    private DefaultTableModel tableModelBuider(List<UserWrapper> usersList) {
        // шапка таблицы пользователей
        String[] tableHeader = {"id", "Дата создания записи", "Дата закрытия записи", "Логин", "Пароль", "Роль"};
        // заполнение модели таблицы по умолчанию данными с определенной ранее шапкой таблицы
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);
        // конвертация каждого элемента списка в обьект для добавления в модель таблицы
        for(UserWrapper userWrapper : usersList) {
            tableModel.addRow(userWrapper.toObject());
        }
        return tableModel;
    }

}
