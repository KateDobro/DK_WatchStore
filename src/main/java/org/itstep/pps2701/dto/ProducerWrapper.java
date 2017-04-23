package org.itstep.pps2701.dto;

import lombok.Data;
import org.itstep.pps2701.DateUtils.DateUtils;
import org.itstep.pps2701.entities.Producer;
import org.itstep.pps2701.service.ObjectInterface;

import java.util.Date;


@Data
public class ProducerWrapper implements ObjectWrapper<Producer>, ObjectInterface {

    private long id;
    private String name;
    private String country;
    private Date dateOpen;
    private Date dateClose;

    public ProducerWrapper() {}


    public ProducerWrapper(Producer item) {
        toWrapper(item);
    }

    public ProducerWrapper(long id, String name, String country, Date dateOpen, Date dateClose) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
    }

    @Override
    public void toWrapper(Producer item) {
        if (item != null){

            if (item.getId() != null)
                id = item.getId();
            name = item.getName();
            country = item.getCountry();
            dateOpen = item.getDateOpen();
            dateClose = item.getDateClose();

//            dateOpen = DateUtils.getDateTimeFormat(item.getDateOpen());
//            dateClose = DateUtils.getDateTimeFormat(item.getDateClose());
        }
    }

    @Override
    public Producer fromWrapper() {
        Producer producer = new Producer();
        try { producer.setId(id); } catch (Exception ex) {}

        producer.setName(name);
        producer.setCountry(country);
        producer.setDateOpen(dateOpen);
        producer.setDateClose(dateClose);

        return producer;
    }

    @Override
    public String toString() {
        return "ProducerWrapper{" +
                "id=" + id +
                ", dateOpen=" + dateOpen +
                ", dateClose=" + dateClose +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public Object[] toObject() {
        return new Object[]{
                getId(),
                getDateOpen(),
                getDateClose(),
                getName(),
                getCountry()};
    }
}
