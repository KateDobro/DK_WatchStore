package org.itstep.pps2701;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.cfg.Environment;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.Properties;

public class Utils {
    private static Connection connection = null;
    private static Injector injector;

//    Соединение с БД
    static {
        try {
            Properties properties = Environment.getProperties();

            connection = DriverManager.getConnection(
                    properties.getProperty(Environment.URL),
                    properties.getProperty(Environment.USER),
                    properties.getProperty(Environment.PASS)
            );

            //Liquibase init (db, migrate)
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("sql/main/changelog.xml",
                    new ClassLoaderResourceAccessor(),
                    database
                    );
            liquibase.update(new Contexts(), new LabelExpression());

            //Guice init
            injector = Guice.createInjector(
                    new MainBindModule(),
                    new JpaPersistModule("main_persist")
            );

            //JPA init
            PersistService persistService = Utils.injector.getInstance(PersistService.class);
            persistService.start();
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static Injector getInjector() {
        return injector;
    }

    // закрытие соединения с БД
    public static void disconnect() {
        try{
            if(connection!=null)
                connection.close();
            connection.isClosed();
        }catch(SQLException ex) { ex.printStackTrace(); }
    }

    // получение соединения с БД
    public static boolean isActiv() {
        boolean res=false;

        try{
            if(connection!=null)
                res=!connection.isClosed();
        }catch(SQLException ex) { ex.printStackTrace(); }

        return res;
    }

    // получение соединения с БД
    public static Connection getConnection() {
        return connection;
    }

}
