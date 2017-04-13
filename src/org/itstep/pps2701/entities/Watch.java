package org.itstep.pps2701.entities;

import org.itstep.pps2701.entities.enums.Watch_type;

import java.util.Date;

/**
 * Created by DK-HOME on 07.04.2017.
 */

// класс/сущность Часы
public class Watch {

    private int id;             // служ.поле - идентификатор
    private Date dateOpen;      // служ.поле - штамп времени создания записи
    private Date dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private int quantity;       // кол-во часов
    private double price;       // цена единицы
    private String trademark;   // торговая марка
    private Watch_type type;    // тип часов "Механические" или "Кварцевые"
    // private User user
    // private Producer producer

    public Watch() {}

    // конструктор без идентификатора
    public Watch(Date dateOpen, Date dateClose, int quantity, double price, String trademark, Watch_type type) {
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.quantity = quantity;
        this.price = price;
        this.trademark = trademark;
        this.type = type;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public Watch_type getType() {
        return type;
    }

    public void setType(Watch_type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Watch{" +
//                "id=" + id +
//                ", dateOpen=" + dateOpen +
//                ", dateClose=" + dateClose +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trademark='" + trademark + '\'' +
                ", type=" + type +
                '}';
    }
}
