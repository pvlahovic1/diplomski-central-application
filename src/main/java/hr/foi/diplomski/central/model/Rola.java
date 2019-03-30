package hr.foi.diplomski.central.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rola")
@Data
public class Rola {

    @Id
    @Column(name = "id_rola")
    private Long id;

    @Column(name = "naziv")
    private String naziv;

}
