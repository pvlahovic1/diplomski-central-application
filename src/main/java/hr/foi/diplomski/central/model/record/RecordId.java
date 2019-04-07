package hr.foi.diplomski.central.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Sensor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class RecordId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_sensor", referencedColumnName = "id_sensor")
    @JsonBackReference
    private Sensor sensor;

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "id_beacon", referencedColumnName = "id_beacon"))
    @JsonBackReference
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

}
