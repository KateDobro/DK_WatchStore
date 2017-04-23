package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.WatchRepository;
import org.itstep.pps2701.dto.ProducerWrapper;
import org.itstep.pps2701.dto.WatchWrapper;
import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.enums.Watch_type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WatchService {

    @Inject
    private WatchRepository watchRepository;

    public List<WatchWrapper> create(WatchWrapper watchWrapper) throws SQLException {
        if(Utils.isActiv()) {
            String query = "INSERT INTO watch_store.watch (date_open, quantity, price, trademark, type) values (?, ?, ?, ?, ?)";
//        String query = "INSERT INTO watch_store.watchWrapper (date_open, quantity, price, trademark, type, id_producer) values (?, ?, ?, ?, ?, ?)";
//        String query = "INSERT INTO watch_store.watchWrapper (date_open, quantity, price, trademark, type, id_producer, id_user) values (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
            ps.setDate(1, (java.sql.Date)watchWrapper.getDateOpen());
            ps.setInt(2, watchWrapper.getQuantity());
            ps.setFloat(3, watchWrapper.getPrice());
            ps.setString(4, watchWrapper.getTrademark());
            ps.setString(5, String.valueOf(watchWrapper.getType()));
            ps.setLong(6, watchWrapper.getProducerWrapper().getId());// FOREIGN KEY - id_producer
//        ps.setInt(6, UserWrapper.id); // FOREIGN KEY - текущий пользователь, что создал запись(не реализовано) //TODO: ДОБАВЛЕНИЕ в поле юзер_логин - логн пользователя, создавшего запись
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }


    public List<WatchWrapper> findAllActive()
    {
        List<WatchWrapper> result = new ArrayList<>();

        List<Watch> all = watchRepository.findAllActive();
        for(Watch item : all)
        {
            result.add(new WatchWrapper(item));
        }

        return result;
    }

    public WatchWrapper getWatchById(int id) throws SQLException{
        WatchWrapper watchWrapper = null;

        if(Utils.isActiv()) {
            String request = "SELECT * FROM watch_store.watch where id = \'" + id + "\' LIMIT 1";

            Statement statement = Utils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            if (resultSet.next()) watchWrapper = parseWatchItem(resultSet);

            statement.close();
        }
        return watchWrapper;
    }

    public List<WatchWrapper> update(WatchWrapper watchWrapper) throws SQLException{
        if(Utils.isActiv()) {
            String updateRequest = "UPDATE watch_store.watch SET "
                    + "quantity = \'" + watchWrapper.getQuantity()
                    + "\', price = \'" + watchWrapper.getPrice()
                    + "\', trademark = \'" + watchWrapper.getTrademark()
                    + "\', type = \'" + String.valueOf(watchWrapper.getType())
                    + "\' WHERE id = \'" + watchWrapper.getId() + "\';";

            PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    public List<WatchWrapper> remove(int id) throws SQLException{
        if(Utils.isActiv()) {
            String request = "UPDATE watch_store.watch SET "
                    + "date_close = \'" + new java.sql.Date(System.currentTimeMillis())
                    + "\' WHERE id = \'" + id + "\';";

            PreparedStatement ps = Utils.getConnection().prepareStatement(request);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    private WatchWrapper parseWatchItem(ResultSet resultSet) throws SQLException{
        ProducerWrapper producerWrapper = new ProducerWrapper();
        producerWrapper.setId(resultSet.getLong("id_producer"));
        return new WatchWrapper(
                resultSet.getLong("id"),
                resultSet.getInt("quantity"),
                resultSet.getFloat("price"),
                resultSet.getString("trademark"),
                Watch_type.getWatch_type(resultSet.getString("type")),
                producerWrapper,
                resultSet.getDate("date_open"),
                resultSet.getDate("date_close")
                );
    }
}
