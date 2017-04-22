package org.itstep.pps2701.dto;

import org.itstep.pps2701.entities.Producer;
import org.itstep.pps2701.service.ObjectInterface;

import java.sql.Timestamp;

// класс/сущность Производитель
public class ProducerWrapper implements ObjectInterface {
    private int id;             // служ.поле - идентификатор
    private Timestamp dateOpen;      // служ.поле - штамп времени создания записи
    private Timestamp dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private String name;        // название производителя
    private String country;     // страна производителя

    public ProducerWrapper() {}

    public ProducerWrapper(Producer item) {

    }

    public ProducerWrapper(int id, Timestamp dateOpen, Timestamp dateClose, String name, String country) {
        this.id = id;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.name = name;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Timestamp dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Timestamp getDateClose() {
        return dateClose;
    }

    public void setDateClose(Timestamp dateClose) {
        this.dateClose = dateClose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
