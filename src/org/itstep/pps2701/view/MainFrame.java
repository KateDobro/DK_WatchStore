package org.itstep.pps2701.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private static final String MAIN_FRAME_TITLE = "DK_Java_CourseProject"; // заголовок главного окна

    //конструктор главного окна
    public MainFrame() throws HeadlessException {
        super(MAIN_FRAME_TITLE);                       // имя главного окна, задается константой
        setLocation(250,250);                    // координаты создания окна
        setSize(600,400);                 // размеры окна по умолчанию
        setJMenuBar(createMenuBar());                  // панель меню в окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // действие при закрытии окна

        // + содержимое во вкладки основной панели
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        TabPanelUsers tabPanelUsers = new TabPanelUsers(tabbedPane, this);
        TabPanelProducers tabPanelProducers = new TabPanelProducers(tabbedPane, this);
//        new TabPanelWatches(tabbedPane, this);    // панель содержимого вкладки "Часы"

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
     * Создание диалогового окна о возникшей ошибке
     * @param errorMessage текст ошибки
     */
    public void createErrorDialog(String errorMessage){
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
