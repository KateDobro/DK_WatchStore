package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.enums.User_role;
import org.itstep.pps2701.view.tableModels.DBTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;

// Вкладка пользователей
public class TabPanelUsers extends JPanel{
    private DBTableModel dbTableModel;      // модель вывода данных пользователей
    private JPanel tabPanelUsers;           // панель с элементами вкладки "Пользователи"
    private JDialog insertDialog;           // диалог добавлениния записи пользователя
    private MainFrame parentFrame;            // родительское окно

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
        tabPanelUsers = new JPanel();                   // + панель содержимого вкладки "Пользователи"
        dbTableModel = new DBTableModel(false); // + модель таблицы для отображения содержимого
        // TODO: ПЕРЕДЕЛАТЬ МЕТОД
//        getUsersData();

        JPanel btnPanel = new JPanel(); // + панель управляющих кнопок вкладки "Пользователи"
        JButton btnAdd = new JButton("Добавить запись"); // + кнопка "Добавить"
        btnAdd.addActionListener(b -> { /* + действие по нажатию на кнопку */
            insertNewUser();   // + запись пользоветеля в бд
        });

        btnPanel.add(btnAdd);           // + кнопку "Добавить" в панель кнопок
        tabPanelUsers.add(btnPanel, "north");    // + панель кнопок в панель Пользователей
    }


    private void insertNewUser() {
        insertDialog = new JDialog(parentFrame, "Добавление пользователя", true);
        insertDialog.setLocationRelativeTo(parentFrame);

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
        JButton btnSave = new JButton("Добавить");
        btnSave.addActionListener( s ->
                addItemToDB(txtFieldLogin.getText(),
                            String.valueOf(pswdField.getPassword()),
                            comboBoxUserRole.getSelectedItem()));

        JButton btnCancel = new JButton("Отмена");
        btnCancel.addActionListener(b ->
                insertDialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        insertDialogPanel.add(btnPanel, "south");
        insertDialog.add(insertDialogPanel);
        insertDialog.pack();
        insertDialog.setVisible(true);
    }

    // вынести метод в общий интерфейс (параметры:
    //                                      - обьект, который будет добавляться
    //                                      - строковый запрос)
    private void addItemToDB(String login, String password, Object role) {
        try{
            String query = "INSERT INTO watch_store.users (login, password, role) values (?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, String.valueOf(role));
            ps.executeUpdate();
            ps.close();

            insertDialog.dispose();
//            getUsersData();

        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            // вызов метода создания диалога в родительском элементе
            parentFrame.callErrorDialog(ex.getMessage());
        }
    }

}
