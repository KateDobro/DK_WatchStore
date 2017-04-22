package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.ProducerRepository;
import org.itstep.pps2701.dto.ProducerWrapper;
import org.itstep.pps2701.entities.Producer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducerService {

    @Inject
    private ProducerRepository producerRepository;

    public List<ProducerWrapper> create(ProducerWrapper producerWrapper) throws SQLException {
        // TODO:
        if(Utils.isActiv()) {
            String query = "INSERT INTO watch_store.producers (date_open, name, country) values (?, ?, ?)";

            PreparedStatement ps = Utils.getConnection().prepareStatement(query);
            ps.setTimestamp(1, producerWrapper.getDateOpen());
            ps.setString(2, producerWrapper.getName());
            ps.setString(3, producerWrapper.getCountry());
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }


    public List<ProducerWrapper> findAllActive()
    {
        List<ProducerWrapper> result = new ArrayList<>();

        List<Producer> all = producerRepository.findAllActive();
        for(Producer item : all)
        {
            result.add(new ProducerWrapper(item));
        }

        return result;
    }

    public ProducerWrapper getProducerById(int id) throws SQLException{
        ProducerWrapper producerWrapper = null;

        if(Utils.isActiv()) {
            String request = "SELECT * FROM watch_store.producers where id = \'" + id + "\' LIMIT 1";

            Statement statement = Utils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            if (resultSet.next()) producerWrapper = parseProducerItem(resultSet);

            statement.close();
        }

        return producerWrapper;
    }

    public List<ProducerWrapper> update(ProducerWrapper producerWrapper) throws SQLException{
        if(Utils.isActiv()) {
            String updateRequest = "UPDATE watch_store.producers SET "
                    + "name = \'" + producerWrapper.getName()
                    + "\', country = \'" + producerWrapper.getCountry()
                    + "\' WHERE id = \'" + producerWrapper.getId() + "\';";

            PreparedStatement ps = Utils.getConnection().prepareStatement(updateRequest);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    public List<ProducerWrapper> remove(int id) throws SQLException{
        if(Utils.isActiv()) {
            String request = "UPDATE watch_store.producers SET "
                    + "date_close = \'" + new Timestamp(System.currentTimeMillis())
                    + "\' WHERE id = \'" + id + "\';";

            PreparedStatement ps = Utils.getConnection().prepareStatement(request);
            ps.executeUpdate();
            ps.close();
        }

        return findAllActive();
    }

    private ProducerWrapper parseProducerItem(ResultSet resultSet) throws SQLException{
        return new ProducerWrapper(
                resultSet.getInt("id"),
                resultSet.getTimestamp("date_open"),
                resultSet.getTimestamp("date_close"),
                resultSet.getString("name"),
                resultSet.getString("country")
                );
    }

    public List<Object> getProducerNames() throws SQLException {
        List<Object> producerNamesList = new ArrayList<>();

        if(Utils.isActiv()) {
            String request = "SELECT id, name FROM watch_store.producers";
            Statement statement = Utils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(request);

            while (resultSet.next()) {
                ProducerWrapper producerWrapper = new ProducerWrapper();
                producerWrapper.setId(resultSet.getInt("id"));
                producerWrapper.setName(resultSet.getString("name"));
                producerNamesList.add(new Object[]{
                        producerWrapper.getId(),
                        producerWrapper.getName()}
                );
            }
            statement.close();
        }
        return producerNamesList;
    }
}
