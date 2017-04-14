package org.itstep.pps2701.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private static final String MAIN_FRAME_TITLE = "DK_Java_CourseProject"; // заголовок главного окна

//    private TabPanelUsers tabPanelUsers;          // панель содержимого вкладки "Пользователи"
//    private TabPanelWatches tabPanelWatches;      // панель содержимого вкладки "Часы"
//    private TabPanelProducers tabPanelProducers;  // панель содержимого вкладки "Производители"

    //конструктор главного окна
    public MainFrame() throws HeadlessException {
        super(MAIN_FRAME_TITLE);                       // имя главного окна, задается константой
        setLocation(500,250);                    // координаты создания окна
        setSize(600,500);                 // размеры окна по умолчанию
        setJMenuBar(createMenuBar());                  // панель меню в окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // действие при закрытии окна

        // + содержимое во вкладки основной панели
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        new TabPanelUsers(tabbedPane, this);
        new TabPanelWatches(tabbedPane, this);
//        tabProducersPanel = new TabProducersPanel(tabbedPane);

        getContentPane().add(tabbedPane);       // +
        setVisible(true);                       // делаем главное окно видимым
    }

    /**
     * Метод создания панели меню и заполнения ее элементами
     * @return JMenuBar menuBar возвращает готовую панель меню
     */
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();                 // + панель меню
        JMenu file = new JMenu("Файл");                 // + меню с именем ФАЙЛ

        JMenuItem exit = new JMenuItem("Выход");      // + элемент меню - ВЫХОД
        exit.addActionListener(e -> System.exit(0));// назначаем слушателю действия по клику на элемент - Закрыть окно

        file.add(exit);     // + кнопка выхода в элемент меню Файл
        menuBar.add(file);  // + меню со всеми элементами в панель меню

        return menuBar;
    }

    /**
     * получение всех записей пользователей в БД
     */
//    private void getUsersData() {
//        try {
//            // соединение с базой на сервере
//            Connection connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/watch_store?autoReconnect=true&useSSL=false", /* имя сервера */
//                "root", /* имя пользователя */
//                "root" /* пароль пользователя */);
//
//            // создать оператор запроса
//            Statement statement = connection.createStatement();
//            // Выполнить запрос
//            ResultSet resultSet = statement.executeQuery("SELECT id, login, password, role FROM watch_store.users");
//            // результат запроса в модель таблицы
//            dbTableModel.setDataSource(resultSet);
//            // закрываем оператор запроса
//            statement.close();
//        } catch (Exception ex){
//            System.out.println("SQLException: " + ex.getMessage());
//        }
//    }

    /**
     * Создание диалогового окна о возникшей ошибке
     * @param errorMessage текст ошибки
     */
    public void callErrorDialog(String errorMessage){
        JDialog dialogError = new JDialog(this, "Error!", true);

        dialogError.setName("Ошибка!");
        dialogError.setLocationRelativeTo(this);
        dialogError.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialogError.setSize(100,150);

        JPanel panelError = new JPanel();
//        BoxLayout boxLayout = new BoxLayout(panelError, BoxLayout.Y_AXIS);
//        panelError.setLayout(boxLayout);
        JLabel lblError = new JLabel();
        lblError.setIcon(new ImageIcon("images/danger.png"));
        panelError.add(lblError);
        panelError.add(new JLabel(errorMessage));

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
