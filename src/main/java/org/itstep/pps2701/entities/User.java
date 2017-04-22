package org.itstep.pps2701.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.itstep.pps2701.enums.USER_ROLE;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;

    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;

    @Enumerated(EnumType.STRING)
    @Column
    private USER_ROLE role;
}
