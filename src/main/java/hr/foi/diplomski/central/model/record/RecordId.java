package hr.foi.diplomski.central.model.record;

import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Sensor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class RecordId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "beacon_id", referencedColumnName = "id"))
    private Beacon beacon;

    @Column(name = "record_date")
    private LocalDateTime recordDate;

    public RecordId() {
    }

    public RecordId(Sensor sensor, Beacon beacon, LocalDateTime recordDate) {
        this.sensor = sensor;
        this.beacon = beacon;
        this.recordDate = recordDate;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }
}
