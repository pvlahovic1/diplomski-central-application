package hr.foi.diplomski.central.model.record;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "record")
public class Record {

    @EmbeddedId
    private RecordId recordId;

    @Column(name = "distance")
    private Double distance;

    public Record() {
    }

    public Record(RecordId recordId, Double distance) {
        this.recordId = recordId;
        this.distance = distance;
    }

    public RecordId getRecordId() {
        return recordId;
    }

    public void setRecordId(RecordId recordId) {
        this.recordId = recordId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
