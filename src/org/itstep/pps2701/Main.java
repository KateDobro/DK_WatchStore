package org.itstep.pps2701;


import org.itstep.pps2701.view.MainFrame;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            Utils.connectionToDb();
            new MainFrame();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
