package org.itstep.pps2701.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.itstep.pps2701.enums.WATCH_TYPE;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "watch")
public class Watch implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    private Long id;

    @Column
    private String trademark;

    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;

    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;

    @Column
    private Integer quantity;

    @Column
    private Float price;

    @Enumerated(EnumType.STRING)
    @Column
    private WATCH_TYPE type;
}
