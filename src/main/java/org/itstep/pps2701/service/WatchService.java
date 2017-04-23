package org.itstep.pps2701.service;

import com.google.inject.Inject;
import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dao.WatchRepository;
import org.itstep.pps2701.dto.WatchWrapper;
import org.itstep.pps2701.entities.Watch;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class WatchService {

    @Inject
    private WatchRepository watchRepository;

    public List<WatchWrapper> create(WatchWrapper wrapper) throws SQLException {
        if(Utils.isActiv()) {
            if(wrapper != null){
                Watch watch = wrapper.fromWrapper();
                watchRepository.save(watch);
                // TODO: установка пользователя создавшего запись
            }
        }
        return findAllActive();
    }

    public List<WatchWrapper> findAllActive(){
        List<WatchWrapper> result = new ArrayList<>();

        List<Watch> all = watchRepository.findAllActive();
        for(Watch item : all){
            result.add(new WatchWrapper(item));
        }
        return result;
    }

    public WatchWrapper read(String id) throws SQLException{
        WatchWrapper watchWrapper = null;
        if(Utils.isActiv()) {
            if(id != null){
                Watch watch = watchRepository.getOne(Long.parseLong(id));
                watchWrapper = new WatchWrapper(watch);
            }
        }
        return watchWrapper;
    }

    public List<WatchWrapper> update(WatchWrapper wrapper) throws SQLException{
        if(Utils.isActiv()) {
            if(wrapper.getId() != null){
                Watch watch = watchRepository.getOne(Long.parseLong(wrapper.getId()));
                watch.setQuantity(wrapper.getQuantity());
                watch.setPrice(wrapper.getPrice());
                watch.setTrademark(wrapper.getTrademark());
                watch.setType(wrapper.getType());
                watch.setProducer(wrapper.getProducerWrapper().fromWrapper());

            }

        }

        return findAllActive();
    }

    public List<WatchWrapper> delete(String id) throws SQLException{
        if(Utils.isActiv()) {
            if(id != null){
                Watch watch = watchRepository.getOne(Long.parseLong(id));
                watch.setDateClose(new Date());

                watchRepository.save(watch);
//                watchRepository.delete(watch);
            }
        }
        return findAllActive();
    }

}
