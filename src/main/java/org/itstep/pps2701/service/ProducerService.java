package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.ProducerRepository;
import org.itstep.pps2701.dto.ProducerWrapper;
import org.itstep.pps2701.entities.Producer;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ProducerService {

    @Inject
    private ProducerRepository producerRepository;

    public List<ProducerWrapper> create(ProducerWrapper wrapper) throws SQLException {
        if(Utils.isActiv()) {
            if(wrapper != null){
                Producer producer = wrapper.fromWrapper();
                // TODO: проверка на наличие производителя с такими же данными в бд
                producerRepository.save(producer);
            }
        }
        return findAllActive();
    }


    public List<ProducerWrapper> findAllActive() {
        List<ProducerWrapper> result = new ArrayList<>();

        List<Producer> all = producerRepository.findAllActive();
        for(Producer item : all) {
            result.add(new ProducerWrapper(item));
        }

        return result;
    }

    public ProducerWrapper read(String id) throws SQLException{
        ProducerWrapper producerWrapper = null;

        if(Utils.isActiv()) {
            if(id != null){
                Producer producer = producerRepository.getOne(Long.parseLong(id));
                producerWrapper = new ProducerWrapper(producer);
            }
        }
        return producerWrapper;
    }

    public List<ProducerWrapper> update(ProducerWrapper producerWrapper) throws SQLException{
        if(Utils.isActiv()) {
            if(producerWrapper.getId() != null){
                Producer producer = producerRepository.getOne(Long.parseLong(producerWrapper.getId()));
                producer.setName(producerWrapper.getName());
                producer.setCountry(producerWrapper.getCountry());
                producerRepository.save(producer);
            }
        }
        return findAllActive();
    }

    public List<ProducerWrapper> delete(String id) throws SQLException{
        if(Utils.isActiv()) {
            Producer producer = producerRepository.getOne(Long.parseLong(id));
            producer.setDateClose(new Date());
            producerRepository.save(producer);
        }
        return findAllActive();
    }


    // TODO:
    public List<Object> getProducerNames() throws SQLException {
        List<Object> producerNamesList = new ArrayList<>(producerRepository.findAllActive().size());

        if(Utils.isActiv()) {
            List<Producer> tmpList = producerRepository.findAllActive();
//            for(Producer p: tmpList){
//
//            }
        }
        return producerNamesList;
    }
}
