package org.itstep.pps2701.service;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.enums.Watch_type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WatchService {
    public List<Watch> create(Watch watch) throws SQLException {
        String query = "INSERT INTO watch_store.watch (date_open, quantity, price, trademark, type) values (?, ?, ?, ?, ?)";
//        String query = "INSERT INTO watch_store.watch (date_open, quantity, price, trademark, type, id_producer) values (?, ?, ?, ?, ?, ?)";
//        String query = "INSERT INTO watch_store.watch (date_open, quantity, price, trademark, type, id_producer, id_user) values (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = Utils.getConnection().prepareStatement(query);
        ps.setTimestamp(1, watch.getDateOpen());
        ps.setInt(2, watch.getQuantity());
        ps.setDouble(3, watch.getPrice());
        ps.setString(4, watch.getTrademark());
        ps.setString(5, String.valueOf(watch.getType()));
        ps.setInt(6, watch.getIdProducer());// FOREIGN KEY - id_producer
//        ps.setInt(6, User.id); // FOREIGN KEY - текущий пользователь, что создал запись(не реализовано) //TODO: ДОБАВЛЕНИЕ в поле юзер_логин - логн пользователя, создавшего запись
        ps.executeUpdate();
        ps.close();

        return read();
    }


    public List<Watch> read() throws SQLException {
        List<Watch> watchList = new ArrayList<>();
//        String request = "SELECT * FROM watch_store.watch WHERE producers.date_close IS NOT NULL"; // запрос для вывода записей которые не помечены как удаленные
        String request = "SELECT * FROM watch_store.watch";
        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        while (resultSet.next()){
            watchList.add(parseWatchItem(resultSet));
        }
        statement.close();
        return watchList;
    }

    public Watch getWatchById(int id) throws SQLException{
        String request = "SELECT * FROM watch_store.watch where id = \'" + id + "\' LIMIT 1";

        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        Watch watch = null;
        if(resultSet.next()) watch = parseWatchItem(resultSet);

        statement.close();

        return watch;
    }

    public List<Watch> update(Watch watch) throws SQLException{
        String updateRequest = "UPDATE watch_store.watch SET "
                                + "quantity = \'" + watch.getQuantity()
                                + "\', price = \'" + watch.getPrice()
                                + "\', trademark = \'" + watch.getTrademark()
                                + "\', type = \'" + String.valueOf(watch.getType())
                                + "\' WHERE id = \'" + watch.getId() + "\';";

        PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
        ps.executeUpdate();
        ps.close();

        return read();
    }

    public List<Watch> remove(int id) throws SQLException{
        String request = "UPDATE watch_store.watch SET "
                                + "date_close = \'" + new Timestamp(System.currentTimeMillis())
                                + "\' WHERE id = \'" + id + "\';";

        PreparedStatement ps = Utils.getConnection().prepareStatement(request);
        ps.executeUpdate();
        ps.close();

        return read();
    }

    private Watch parseWatchItem(ResultSet resultSet) throws SQLException{
        return new Watch(
                resultSet.getInt("id"),
                resultSet.getTimestamp("date_open"),
                resultSet.getTimestamp("date_close"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("price"),
                resultSet.getString("trademark"),
                Watch_type.getWatch_type(resultSet.getString("type")),
                resultSet.getInt("id_producer")
                );
    }
}
