package org.itstep.pps2701.interfaces;

import org.itstep.pps2701.models.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by DK-HOME on 10.04.2017.
 */
public interface ISession{
    ResultSet getData(String query) throws SQLException;
}
