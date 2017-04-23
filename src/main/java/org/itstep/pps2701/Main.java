package org.itstep.pps2701;

import org.itstep.pps2701.view.MainFrame;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new MainFrame());

        //todo
        //Utils.disconnect();
    }
}
