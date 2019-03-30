package hr.foi.diplomski.central.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "beacon")
@Data
public class Beacon {

    @Id
    @GeneratedValue(generator = "beacon_id_seq")
    @Column(name = "id_beacon")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "major")
    private Integer major;

    @Column(name = "minor")
    private Integer minor;

}
