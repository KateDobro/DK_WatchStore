package org.itstep.pps2701.entities;

import java.util.Date;

/**
 * Created by DK-HOME on 07.04.2017.
 */

// класс/сущность Производитель
public class Producer {

    private int id;             // служ.поле - идентификатор
    private Date dateOpen;      // служ.поле - штамп времени создания записи
    private Date dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private String name;        // название производителя
    private String country;     // страна производителя

    public Producer() {}

    // конструктор без идентификатора
    public Producer(Date dateOpen, Date dateClose, String name, String country) {
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

    public Date getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Date dateOpen) {
        this.dateOpen = dateOpen;
    }

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
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
        return "Producer{" +
//                "id=" + id +
//                ", dateOpen=" + dateOpen +
//                ", dateClose=" + dateClose +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
