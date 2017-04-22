package org.itstep.pps2701.dto;

import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.enums.WATCH_TYPE;
import org.itstep.pps2701.service.ObjectInterface;

import java.sql.Timestamp;

// класс/сущность Часы
public class WatchWrapper implements ObjectInterface {

    private int id;             // служ.поле - идентификатор
    private Timestamp dateOpen;      // служ.поле - штамп времени создания записи
    private Timestamp dateClose;     // служ.поле - штамп времени закрытия/"удаления" записи
    private int quantity;       // кол-во часов
    private double price;       // цена единицы
    private String trademark;   // торговая марка
    private WATCH_TYPE type;    // тип часов "Механические" или "Кварцевые"
    private int idProducer;     // ид производителя

    public WatchWrapper() {}

    public WatchWrapper(Watch item) {

    }

    public WatchWrapper(int id, Timestamp dateOpen, Timestamp dateClose, int quantity, double price, String trademark, WATCH_TYPE type, int idProducer) {
        this.id = id;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.quantity = quantity;
        this.price = price;
        this.trademark = trademark;
        this.type = type;
        this.idProducer = idProducer;
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

    public WATCH_TYPE getType() {
        return type;
    }

    public void setType(WATCH_TYPE type) {
        this.type = type;
    }

    public int getIdProducer() {
        return idProducer;
    }

    public void setIdProducer(int idProducer) {
        this.idProducer = idProducer;
    }

    @Override
    public String toString() {
        return "WatchWrapper{" +
                "id=" + id +
                ", dateOpen=" + dateOpen +
                ", dateClose=" + dateClose +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trademark='" + trademark + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public Object[] toObject() {
        return new Object[]{
                getId(),
                getDateOpen(),
                getDateClose(),
                getQuantity(),
                getPrice(), // можно зашифровать
                getTrademark(),
                getType()};
    }

}
