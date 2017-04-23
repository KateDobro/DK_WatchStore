package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dto.UserWrapper;
import org.itstep.pps2701.enums.User_role;
import org.itstep.pps2701.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
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
//        получение данных из БД и доавление их в таблицу для вывод в клиенте
        usersTable = new JTable(tableModelBuider(userService.findAllActive())); // + таблица пользователей заполняется по модели

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать выбранную запись");

        addBtn.addActionListener(b -> createAddDialog());
        editBtn.addActionListener(b -> createEditDialog(usersTable.getSelectedRow()));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));    // + панель управляющих кнопок вкладки "Пользователи"
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);

        tabPanelUsers = new JPanel(new BorderLayout(5, 5));
        tabPanelUsers.add(new JScrollPane(usersTable), BorderLayout.CENTER);    // + таблицу с данными по центру в панель содержимого вкладки "Пользователи"
        tabPanelUsers.add(btnPanel, BorderLayout.SOUTH);                        // + панель кнопок в панель Пользователей
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление пользователя", true);
        addDialog.setLocationRelativeTo(addDialog.getOwner());

        JTextField txtFieldLogin = new JTextField(25);
        JPasswordField passField = new JPasswordField(25);
        JComboBox comboBoxUserRole = new JComboBox<>(User_role.values());

        txtFieldLogin.setToolTipText(lblLogin.getText());
        passField.setToolTipText(lblPassword.getText());

        passField.setEchoChar('*');
        comboBoxUserRole.setSize(25,5);

        JPanel insertDialogPanel = new JPanel(new GridLayout(7,1));
        insertDialogPanel.add(lblLogin);
        insertDialogPanel.add(txtFieldLogin);
        insertDialogPanel.add(lblPassword);
        insertDialogPanel.add(passField);
        insertDialogPanel.add(lblRole);
        insertDialogPanel.add(comboBoxUserRole);

        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener( b -> {
            try{
                UserWrapper userWrapper;
                // если поля логина и пароля не пустые
                if(!txtFieldLogin.getText().trim().isEmpty()
                        && !String.valueOf(passField.getPassword()).trim().isEmpty()){
                    userWrapper = new UserWrapper();
                    userWrapper.setDateOpen(new Date(System.currentTimeMillis()));
                    userWrapper.setLogin(txtFieldLogin.getText().trim());
                    userWrapper.setPassword(String.valueOf(passField.getPassword()).trim());
                    userWrapper.setRole((User_role)comboBoxUserRole.getSelectedItem());

                    List<UserWrapper> userWrapperList = userService.create(userWrapper); // вызов метода создания пользователя

                    usersTable.setModel(tableModelBuider(userWrapperList)); // перестройка данных в таблице

                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> addDialog.dispose());

        JPanel btnPanel = new JPanel();
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

        String id = String.valueOf(usersTable.getValueAt(selectedRow, 0));

        UserWrapper userWrapper = null;
        try{
            userWrapper = userService.read(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        JTextField txtFieldLogin = new JTextField(25);
        JPasswordField pswdField = new JPasswordField(25);
        JComboBox comboBoxUserRole = new JComboBox<>(User_role.values());

        txtFieldLogin.setText(userWrapper.getLogin());
        pswdField.setText(userWrapper.getPassword());
        comboBoxUserRole.setSelectedItem(userWrapper.getRole());

        pswdField.setEchoChar('*');
        comboBoxUserRole.setSize(25,5);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));
        editDialogPanel.add(lblLogin);
        editDialogPanel.add(txtFieldLogin);
        editDialogPanel.add(lblPassword);
        editDialogPanel.add(pswdField);
        editDialogPanel.add(lblRole);
        editDialogPanel.add(comboBoxUserRole);

        JButton updateBtn = new JButton("Сохранить");
        JButton removeBtn = new JButton("Удалить");
        JButton cancelBtn = new JButton("Отмена");

        updateBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    UserWrapper userWrapperFin;
                    if(!txtFieldLogin.getText().trim().isEmpty()
                            && pswdField.getPassword().length>0) {

                        userWrapperFin = new UserWrapper();
                        userWrapperFin.setId(id);
                        userWrapperFin.setLogin(txtFieldLogin.getText());
                        userWrapperFin.setPassword(String.valueOf(pswdField.getPassword()));
                        userWrapperFin.setRole((User_role) comboBoxUserRole.getSelectedItem());

                        List<UserWrapper> userWrapperList = userService.update(userWrapperFin); // вызов метода обновления данных пользователя + перестройка данных в таблице

                        usersTable.setModel(tableModelBuider(userWrapperList));

                        editDialog.dispose();
                    } else {
                        parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        removeBtn.addActionListener(b -> {
            try{
                List<UserWrapper> userWrapperList = userService.delete(id);

                usersTable.setModel(tableModelBuider(userWrapperList));

                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> editDialog.dispose());

        JPanel btnPanel = new JPanel();
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
        String[] tableHeader = {
                "id",
                "Логин",
                "Пароль",
                "Роль",
                "Дата создания записи",
                "Дата закрытия записи"
        };
        // заполнение модели таблицы по умолчанию данными с определенной ранее шапкой таблицы
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);
        // конвертация каждого элемента списка в обьект для добавления в модель таблицы
        for(UserWrapper userWrapper : usersList) {
            tableModel.addRow(userWrapper.toObject());
        }
        return tableModel;
    }
}
