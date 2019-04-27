package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "beacon")
@Data
public class Beacon {

    @Id
    @GeneratedValue(generator = "beacon_id_seq")
    @Column(name = "id_beacon")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "major")
    private Integer major;

    @Column(name = "minor")
    private Integer minor;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_beacon")
    @JsonManagedReference
    @ToString.Exclude
    private List<Device> devices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_central_user", referencedColumnName = "id_central_user")
    @ToString.Exclude
    private User user;

}
