package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(generator = "sensor_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "beacon_data_purge_interval")
    private Integer beaconDataPurgeInterval;

    @Column(name = "beacon_data_send_interval")
    private Integer beaconDataSendInterval;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @JsonBackReference
    private Room room;

    public Sensor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Integer getBeaconDataPurgeInterval() {
        return beaconDataPurgeInterval;
    }

    public void setBeaconDataPurgeInterval(Integer beaconDataPurgeInterval) {
        this.beaconDataPurgeInterval = beaconDataPurgeInterval;
    }

    public Integer getBeaconDataSendInterval() {
        return beaconDataSendInterval;
    }

    public void setBeaconDataSendInterval(Integer beaconDataSendInterval) {
        this.beaconDataSendInterval = beaconDataSendInterval;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
