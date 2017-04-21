package org.itstep.pps2701.service;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.entities.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducerService {

    public List<Producer> create(Producer producer) throws SQLException {
        // TODO:
        String query = "INSERT INTO watch_store.producers (date_open, name, country) values (?, ?, ?)";

        PreparedStatement ps = Utils.getConnection().prepareStatement(query);
        ps.setTimestamp(1, producer.getDateOpen());
        ps.setString(2, producer.getName());
        ps.setString(3, producer.getCountry());
        ps.executeUpdate();
        ps.close();

        return read();
    }


    public List<Producer> read() throws SQLException {
        List<Producer> producerList = new ArrayList<>();
//        String request = "SELECT * FROM watch_store.producers WHERE producers.date_close IS NOT NULL"; // запрос для вывода записей которые не помечены как удаленные
        String request = "SELECT * FROM watch_store.producers";
        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        while (resultSet.next()){
            producerList.add(parseProducerItem(resultSet));
        }
        statement.close();
        return producerList;
    }

    public Producer getProducerById(int id) throws SQLException{
        String request = "SELECT * FROM watch_store.producers where id = \'" + id + "\' LIMIT 1";

        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        Producer producer = null;
        if(resultSet.next()) producer = parseProducerItem(resultSet);

        statement.close();

        return producer;
    }

    public List<Producer> update(Producer producer) throws SQLException{
        String updateRequest = "UPDATE watch_store.producers SET "
                                + "name = \'" + producer.getName()
                                + "\', country = \'" + producer.getCountry()
                                + "\' WHERE id = \'" + producer.getId() + "\';";

        PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
        ps.executeUpdate();
        ps.close();

        return read();
    }

    public List<Producer> remove(int id) throws SQLException{
        String request = "UPDATE watch_store.producers SET "
                                + "date_close = \'" + new Timestamp(System.currentTimeMillis())
                                + "\' WHERE id = \'" + id + "\';";

        PreparedStatement ps = Utils.getConnection().prepareStatement(request);
        ps.executeUpdate();
        ps.close();

        return read();
    }

    private Producer parseProducerItem(ResultSet resultSet) throws SQLException{
        return new Producer(
                resultSet.getInt("id"),
                resultSet.getTimestamp("date_open"),
                resultSet.getTimestamp("date_close"),
                resultSet.getString("name"),
                resultSet.getString("country")
                );
    }

    public List<Object> getProducerNames() throws SQLException {
        List<Object> producerNamesList = new ArrayList<>();
        String request = "SELECT id, name FROM watch_store.producers";
        Statement statement = Utils.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(request);

        while (resultSet.next()){
            Producer producer = new Producer();
            producer.setId(resultSet.getInt("id"));
            producer.setName(resultSet.getString("name"));
            producerNamesList.add(new Object[]{
                                    producer.getId(),
                                    producer.getName()}
                                    );
        }
        statement.close();
        return producerNamesList;
    }
}
