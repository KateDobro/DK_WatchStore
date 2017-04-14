package org.itstep.pps2701;


import org.itstep.pps2701.view.MainFrame;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // создание главного окна
        try {
            Utils.connectionToDb(); // установка соединения с бд
            new MainFrame();
        } catch (SQLException ex) {
            MainFrame mainFrame = new MainFrame();
            System.out.println("ОШИБКА - MAIN");
            ex.printStackTrace();
            mainFrame.callErrorDialog(ex.getMessage());
        }
    }
}
