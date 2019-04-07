package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sensor")
@Data
public class Sensor {

    @Id
    @GeneratedValue(generator = "sensor_id_seq")
    @Column(name = "id_sensor")
    private Long id;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "beacon_data_purge_interval")
    private Integer beaconDataPurgeInterval;

    @Column(name = "beacon_data_send_interval")
    private Integer beaconDataSendInterval;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "id_room", referencedColumnName = "id_room")
    @JsonBackReference
    private Room room;

}
