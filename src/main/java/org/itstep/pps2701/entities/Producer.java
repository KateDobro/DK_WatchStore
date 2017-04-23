package org.itstep.pps2701.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "producers")
public class Producer implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String country;

    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;

    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;
}
