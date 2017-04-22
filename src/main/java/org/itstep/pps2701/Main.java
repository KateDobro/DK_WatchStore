package org.itstep.pps2701;

import org.itstep.pps2701.view.MainFrame;


public class Main {

    public static void main(String[] args) {
        try {


            new MainFrame(); // создание главного окна
        } catch (Exception ex) {
            //MainFrame mainFrame = new MainFrame();
            ex.printStackTrace();
            //mainFrame.createErrorDialog(ex.getMessage());
        }

        //todo
        //Utils.disconnect();
    }
}
