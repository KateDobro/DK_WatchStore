package org.itstep.pps2701.dto;

import lombok.Data;
import org.itstep.pps2701.DateUtils.DateUtils;
import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.enums.Watch_type;
import org.itstep.pps2701.service.ObjectInterface;

import java.util.Date;

@Data
public class WatchWrapper implements ObjectWrapper<Watch>, ObjectInterface {
    private String id;
    private int quantity;
    private float price;
    private String trademark;
    private Watch_type type;
    private ProducerWrapper producerWrapper = null;
    private UserWrapper userWrapper = null;
    private Date dateOpen;
    private Date dateClose;

    public WatchWrapper() {}

    public WatchWrapper(Watch item) {
        toWrapper(item);
    }

    public WatchWrapper(String id, int quantity, float price, String trademark, Watch_type type, ProducerWrapper producerWrapper, Date dateOpen, Date dateClose) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.trademark = trademark;
        this.type = type;
        this.producerWrapper = producerWrapper;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
    }

    @Override
    public void toWrapper(Watch item) {
        if (item != null){

            if (item.getId() != null)
                id = String.valueOf(item.getId());

            quantity = item.getQuantity();
            price = item.getPrice();
            trademark = item.getTrademark();
            type = item.getType();

            if(item.getProducer() != null)
                producerWrapper = new ProducerWrapper(item.getProducer());
            if(item.getUser() != null)
                userWrapper = new UserWrapper(item.getUser());

            dateOpen = item.getDateOpen();
            dateClose = item.getDateClose();
//            dateOpen = DateUtils.getDateTimeFormat(item.getDateOpen());
//            dateClose = DateUtils.getDateTimeFormat(item.getDateClose());
        }
    }

    @Override
    public Watch fromWrapper() {
        Watch item = new Watch();
        try { item.setId(Long.parseLong(id)); } catch (Exception ex) {}

        item.setQuantity(quantity);
        item.setPrice(price);
        item.setType(type);
        item.setProducer(producerWrapper.fromWrapper());
        item.setUser(userWrapper.fromWrapper());
        item.setDateOpen(dateOpen);
        item.setDateClose(dateClose);

        return item;
    }

    @Override
    public String toString() {
        return "WatchWrapper{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trademark=" + trademark +
                ", type=" + type +
                ", producer=" + producerWrapper.getName() +
                ", dateOpen=" + dateOpen + "" +
                ", dateClose=" + dateClose +
                '}';
    }

    @Override
    public Object[] toObject() {
        return new Object[]{
                getId(),
                getQuantity(),
                getPrice(),
                getTrademark(),
                getType(),
                getProducerWrapper(),
                getUserWrapper(),
                getDateOpen(),
                getDateClose()
        };
    }
}
