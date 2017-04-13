package org.itstep.pps2701.view;

import org.itstep.pps2701.entities.enums.User_role;
import org.itstep.pps2701.interfaces.ISession;
import org.itstep.pps2701.models.Session;
import org.itstep.pps2701.view.tableModels.DBTableModel;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame{
    private JPanel tabPanelWatches, tabPanelProducers, tabPanelUsers;
    private DBTableModel DBTableModel; // модель создания таблицы
    private JTable tableWatches, tableProducers, tableUsers;

    public MainFrame() throws HeadlessException {
        super("DK_Java_CourseProject");
        setLocation(500,250);
        setSize(600,500);   // устанавливаем размеры окна по умолчанию
        setJMenuBar(createMenuBar());       // устанавливаем панель меню в окно
        setDefaultCloseOperation(super.EXIT_ON_CLOSE);      // действие при закрытии окна

        // добавляем вкладки в основную панель
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Пользователи", createTabUsers());     // добавляем вкладку Пользователи
        tabbedPane.addTab("Производители", createTabProducers()); // добавляем вкладку Производители
        tabbedPane.addTab("Часы", createTabWatches());   // добавляем вкладку Часы
        getContentPane().add(tabbedPane);

        setVisible(true);                                             // делаем главное окно видимым
    }

    /**
     *  Заполенение вкладки "Пользователи" содержимым
     * @return JPanel tabPanelUsers - содержимое вкладки "Пользователи"
     */
    private JPanel createTabUsers(){
        tabPanelUsers = new JPanel();
        tabPanelUsers.setLayout(new BoxLayout(tabPanelUsers, BoxLayout.Y_AXIS));
        DBTableModel = new DBTableModel(true);
        // полученние данных о пользователях из таблицы "watch_store.users"
        getUsersData();

        //Добавление кнопок
        // Создаем кнопку "Добавить"
        JPanel buttonPanel = new JPanel();
        // кнопка добавления записи
        JButton btnAdd = new JButton("Добавить запись");
        // добавили действие по нажатию на кнопку
        btnAdd.addActionListener(b -> {
//          добавление записи в бд
//            DBTableModel.addRow(new String[]{});
            insertNewUser();

        });
        buttonPanel.add(btnAdd); // добавили кнопку "Добавить" в панель кнопок

        // Создаем кнопку "Удалить"
//        JButton btnRemove = new JButton("Удалить запись");
//        // добавили действие по нажатию на кнопку
//        btnRemove.addActionListener(b -> {
//            // delete last element (count from 0)
////            int counter = dtm.getRowCount();
////            if(counter > 0) dtm.removeRow(counter - 1);
//        });
//        buttonPanel.add(btnRemove); // добавили кнопку "Удалить" в панель кнопок
//        добавляем панель кнопок в панель Вкладки "Пользователи" - прикрепленную к "югу"
        tabPanelUsers.add(buttonPanel, "south");

        // добавляем модель таблицы к панели вкладки "Пользователи"
        tableUsers = new JTable(DBTableModel);
        tabPanelUsers.add( new JScrollPane(tableUsers));
        return tabPanelUsers;
    }

    private JDialog insertDialog;

    private void insertNewUser() {
        insertDialog = new JDialog(
                this,
                "Добавление записи пользователя",
                true);
        insertDialog.setLocationRelativeTo(tableUsers);
        insertDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel insertDialogPanel = new JPanel();
//        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
//        insertDialogPanel.setLayout(flowLayout);
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
            // соединение с базой на сервере
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/watch_store?autoReconnect=true&useSSL=false", /* имя сервера */
                    "root", /* имя пользователя */
                    "root" /* пароль пользователя */);

            String query = "INSERT INTO watch_store.users (login, password, role) values (?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, String.valueOf(role));
            ps.executeUpdate();
            ps.close();

            insertDialog.dispose();
            getUsersData();

        }catch(Exception ex){
            System.out.println(ex);
            JDialog dialogError = new JDialog(this, "Ошибка!", true );
            dialogError.setLocationRelativeTo(this);
            dialogError.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

    /**
     * получение всех записей пользователей в БД
     */
    private void getUsersData() {
        try {
            // соединение с базой на сервере
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/watch_store?autoReconnect=true&useSSL=false", /* имя сервера */
                "root", /* имя пользователя */
                "root" /* пароль пользователя */);

            // создать оператор запроса
            Statement statement = connection.createStatement();
            // Выполнить запрос
            ResultSet resultSet = statement.executeQuery("SELECT id, login, password, role FROM watch_store.users");
            // результат запроса в модель таблицы
            DBTableModel.setDataSource(resultSet);
            // закрываем оператор запроса
            statement.close();
        } catch (Exception ex){
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    // вкладка с Часами
    private JPanel createTabWatches(){
        tabPanelWatches = new JPanel();


        return tabPanelWatches;
    }

    // вкладка с Производителями часов
    private JPanel createTabProducers(){
        tabPanelProducers = new JPanel();

        return tabPanelProducers;
    }


    /**
     * Метод создания панели меню и заполнения ее элементами
     * @return JMenuBar menuBar возвращает готовую панель меню
     */
    private JMenuBar createMenuBar(){

        JMenuBar menuBar = new JMenuBar();                  // создаем панель меню
        JMenu file = new JMenu("Файл");                 // создаем меню с именем ФАЙЛ

        JMenuItem exit = new JMenuItem("Выход");      // создаем элемент меню - ВЫХОД
        exit.addActionListener(e -> System.exit(0));// назначаем слушателю действия по клику на элемент - Закрыть окно

        file.add(exit); // добвляем кнопку выхода в меню Файл

        menuBar.add(file); // добавляем меню со всеми элементами в панель меню
        return menuBar;
    }

}
