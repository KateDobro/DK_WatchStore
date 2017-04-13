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
    private MainFrame mainFrame;            // родительское окно

    /**
     * Конструктор содержимого вкладки "Пользователи"
     * @param tabbedPane в эту вкладку главного окна будет + содержимое панели tabPanelUsers
     * @param mainFrame родительское окно для вывода диалоговых окон
     */
    public TabPanelUsers(JTabbedPane tabbedPane, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildtabPanelUsers();
        tabbedPane.addTab("Пользователи", tabPanelUsers);
    }

    /**
     * Наполнение панели tabPanelUsers содержимым
     */
    private void buildtabPanelUsers() {
        tabPanelUsers = new JPanel();                   // + панель содержимого вкладки "Пользователи"
//        dbTableModel = new DBTableModel(false); // + модель таблицы для отображения содержимого
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
        insertDialog = new JDialog(mainFrame, "Добавление пользователя", true);
        insertDialog.setLocationRelativeTo(mainFrame);
//        insertDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
        JComboBox<User_role> comboBoxUserRole = new JComboBox<User_role>(User_role.values());
        comboBoxUserRole.setSize(25,5);
        insertDialogPanel.add(comboBoxUserRole);

        JPanel btnPanel = new JPanel();
        JButton btnSaveUser = new JButton("Добавить");
        btnSaveUser.addActionListener( s ->
                addUserToDB(txtFieldLogin.getText(),
                            String.valueOf(pswdField.getPassword()),
                            comboBoxUserRole.getSelectedItem()));

        JButton btnCancel = new JButton("Отмена");
        btnCancel.addActionListener(b ->
                insertDialog.dispose());

        btnPanel.add(btnSaveUser);
        btnPanel.add(btnCancel);

        insertDialogPanel.add(btnPanel, "south");
        insertDialog.add(insertDialogPanel);
        insertDialog.pack();
        insertDialog.setVisible(true);
    }

    private void addUserToDB(String login, String password, Object role) {
        try{
            String query = "INSERT INTO watch_store.users (login, password, role) values (?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
//            ps.setString(3, String.valueOf(role));
            ps.executeUpdate();
            ps.close();

            insertDialog.dispose();
//            getUsersData();

        }catch(Exception ex){
//            System.out.println(ex);
            JDialog dialogError = new JDialog(mainFrame, "Error!!!", true);

            dialogError.setName("Ошибка!");
            dialogError.setLocationRelativeTo(this);
            dialogError.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialogError.setSize(100,150);

            JPanel panelError = new JPanel();
            BoxLayout boxLayout = new BoxLayout(panelError, BoxLayout.Y_AXIS);
            JLabel lblError = new JLabel();
            lblError.setIcon(new ImageIcon("images/danger.png"));
            panelError.add(lblError);
            panelError.add(new JLabel(ex.getMessage()));

            JPanel btnPanel = new JPanel();
            JButton btnOk = new JButton("OK");
            btnOk.addActionListener(
                    b -> dialogError.dispose());

            btnPanel.add(btnOk);
            panelError.add(btnPanel, "south");

            dialogError.add(panelError);
            dialogError.pack();
            dialogError.setVisible(true);
        }

    }





}
