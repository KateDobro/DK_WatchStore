package org.itstep.pps2701.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.itstep.pps2701.enums.Watch_type;

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
    private Integer quantity;

    @Column
    private Float price;

    @Column
    private String trademark;

    @Enumerated(EnumType.STRING)
    @Column
    private Watch_type type;

    @Column(name="id_producer")
    private Producer producer;

    @Column(name="id_user")
    private User user;

    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;

    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;
}
