package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "device")
@Data
public class Device {

    @Id
    @GeneratedValue(generator = "device_id_seq")
    @Column(name = "id_device")
    private Long id;

    @Column(name = "device_name")
    private String deviceName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_beacon", referencedColumnName = "id_beacon")
    @JsonBackReference
    private Beacon beacon;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    User user;

}
