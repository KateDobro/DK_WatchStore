package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.User;
import org.itstep.pps2701.entities.enums.User_role;
import org.itstep.pps2701.interfaces.TableBuilderInterface;
import org.itstep.pps2701.interfaces.UserInterface;
import org.itstep.pps2701.models.UserModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.util.List;

// Вкладка пользователей
public class TabPanelUsers implements TableBuilderInterface{
    private DefaultTableModel defaultTableModel;      // модель вывода данных пользователей
    private JTable usersTable;
    private JPanel tabPanelUsers;           // панель с элементами вкладки "Пользователи"
    private JButton btnAdd;
    private JDialog insertDialog;           // диалог добавлениния записи пользователя
    private MainFrame parentFrame;            // родительское окно

    UserInterface userInterface = new UserModel();

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
//        dbTableModel = new DBTableModel(true); // + модель таблицы для отображения содержимого

//         TODO: ПЕРЕДЕЛАТЬ МЕТОД

//        usersTable = new JTable(dbTableModel);      // + таблица пользователей заполняется по модели
        usersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // режим выбор записи - по одному
        tabPanelUsers.add(new JScrollPane(usersTable), BorderLayout.CENTER); // + таблицу с данными по центру в панель содержимого вкладки "Пользователи"

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        btnAdd = new JButton("Добавить запись"); // + кнопка "Добавить"
        btnAdd.addActionListener(b -> { /* + действие по нажатию на кнопку */
            insertNewUser();   // + запись пользоветеля в бд
        });

        btnPanel.add(btnAdd);                               // + кнопку "Добавить" в панель кнопок
        tabPanelUsers.add(btnPanel, BorderLayout.SOUTH);    // + панель кнопок в панель Пользователей
    }

//    private void getUsersData() {
//        try {
//            Statement statement = Utils.getConnection().createStatement();            // + оператор запроса
//            String query = "SELECT id, login, password, role FROM watch_store.users"; // текст запроса
//            ResultSet resultSet = statement.executeQuery(query);                      // выполнить запрос
//            dbTableModel.setDataSource(resultSet); // результат запроса в модель таблицы
//            statement.close(); // закрываем оператор запроса
//        } catch (Exception ex){
//            parentFrame.callErrorDialog(ex.getMessage());
//            System.out.println("SQLException: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

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
//        btnSave.addActionListener( s ->
//                addItemToDB(txtFieldLogin.getText(),
//                            String.valueOf(pswdField.getPassword()),
//                            comboBoxUserRole.getSelectedItem()));

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

//    @Override
//    public JTable tableBuider(List<User> usersList) {
//        String[] tableHeader = {"id",
//                "Дата создания записи",
//                "Дата закрытия записи",
//                "Логин",
//                "Пароль",
//                "Роль"};
//
//        defaultTableModel = new DefaultTableModel(tableHeader, 0);/*{
//            @Override
//            public boolean isCellEditable(int x, int y) {
//                return false;
//            }
//        };
//*/
//        for(User user: usersList) {
//            defaultTableModel.addRow(user.toObject());
//        }
//        return new JTable(defaultTableModel);
//    }

}
