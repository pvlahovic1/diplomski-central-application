package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

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

    @OneToOne(mappedBy = "beacon", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Device device;

    @Override
    public String toString() {
        return "Beacon{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                '}';
    }
}
