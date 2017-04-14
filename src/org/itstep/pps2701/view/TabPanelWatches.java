package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.enums.Watch_type;
import org.itstep.pps2701.view.tableModels.DBTableModel;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;

// Вкладка "Часы"
public class TabPanelWatches extends JPanel{
    private DBTableModel dbTableModel;      // модель вывода данных
    private JPanel tabPanelWatches;         // панель с элементами вкладки "Часы"
    private JDialog insertDialog;           // диалог добавлениния записи пользователя
    private MainFrame parentFrame;          // родительское окно

    /**
     * Конструктор содержимого вкладки "Часы"
     * @param tabbedPane в эту вкладку главного окна будет + содержимое панели tabPanelWatches
     * @param parentFrame родительское окно для вывода диалоговых окон
     */
    public TabPanelWatches(JTabbedPane tabbedPane, MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        buildTabPanelWatches();
        tabbedPane.addTab("Часы", tabPanelWatches);
    }

    /**
     * Наполнение панели tabPanelWatches содержимым
     */
    private void buildTabPanelWatches() {
        tabPanelWatches = new JPanel();                // + панель содержимого вкладки "Часы"
        dbTableModel = new DBTableModel(false); // + модель таблицы для отображения содержимого
        // TODO: ПЕРЕДЕЛАТЬ МЕТОД
//        getUsersData();

        JPanel btnPanel = new JPanel(); // + панель управляющих кнопок вкладки "Пользователи"
        JButton btnAdd = new JButton("Добавить запись"); // + кнопка "Добавить"
        btnAdd.addActionListener(b -> { /* + действие по нажатию на кнопку */
            insertNewItem();   // + запись в бд
        });

        btnPanel.add(btnAdd);           // + кнопку "Добавить" в панель кнопок
        tabPanelWatches.add(btnPanel, "north");    // + панель кнопок в панель Пользователей
    }


    private void insertNewItem() {
        insertDialog = new JDialog(parentFrame, "Добавление записи", true);
        insertDialog.setLocationRelativeTo(parentFrame);
//        insertDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel insertDialogPanel = new JPanel();
        insertDialogPanel.setLayout(new GridLayout(7,1));

        // Добавление полей с данным о часах
//        private int quantity;       // кол-во часов
//        private double price;       // цена единицы
//        private String trademark;   // торговая марка
//        private Watch_type type;    // тип часов "Механические" или "Кварцевые"
//        ПРОИЗВОДИТЕЛЬ id_producer - ссылка на запись в таблице Производителей

        insertDialogPanel.add(new JLabel("Количество:"));
        JTextField txtFieldLogin = new JTextField(25);
        txtFieldLogin.setToolTipText("Количество");
        insertDialogPanel.add(txtFieldLogin);

        insertDialogPanel.add(new JLabel("Цена:"));
        JTextField txtFieldPrice = new JTextField(25);
        txtFieldLogin.setToolTipText("Цена");
        insertDialogPanel.add(txtFieldPrice);

        insertDialogPanel.add(new JLabel("Торговая марка:"));
        JTextField txtFieldTrademark = new JTextField(25);
        txtFieldLogin.setToolTipText("Торговая марка");
        insertDialogPanel.add(txtFieldTrademark);


        insertDialogPanel.add(new JLabel("Тип часов:"));
        JComboBox comboBoxWatchType = new JComboBox<>(Watch_type.values());
        comboBoxWatchType.setSize(25,5);
        insertDialogPanel.add(comboBoxWatchType);

        JPanel btnPanel = new JPanel();
        JButton btnSaveUser = new JButton("Добавить");
        btnSaveUser.addActionListener( s ->
                addItemToDB(/*txtFieldLogin.getText(),
                            String.valueOf(pswdField.getPassword()),
                            comboBoxWatchType.getSelectedItem())*/));

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

    // вынести метод в общий интерфейс (параметры:
    //                                      - обьект, который будет добавляться
    //                                      - строковый запрос)
    private void addItemToDB(/*String login, String password, Object role*/) {
        try{
            String query = "INSERT INTO watch_store.users (login, password, role) values (?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
//            ps.setString(1, login);
//            ps.setString(2, password);
//            ps.setString(3, String.valueOf(role));
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
