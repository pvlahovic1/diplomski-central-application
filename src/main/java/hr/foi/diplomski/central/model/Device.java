package hr.foi.diplomski.central.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "device")
@Data
public class Device {

    @Id
    @GeneratedValue(generator = "device_id_seq")
    @Column(name = "id_device")
    private Integer id;

    @Column(name = "device_name")
    private String deviceName;

    @ManyToOne
    @JoinColumn(name = "id_beacon", referencedColumnName = "id_beacon")
    private Beacon beacon;
}
