package hr.foi.diplomski.central.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "central_audit")
@NoArgsConstructor
@AllArgsConstructor
public class CentralAudit {

    public static final String ROOM_ENTER_MESSAGE = "Beacon je ušao u prostoriju";
    public static final String ROOM_EXIT_MESSAGE = "Beacon je napustio prostoriju";


    @Id
    @GeneratedValue(generator = "central_audit_id_central_audit_seq")
    @Column(name = "id_central_audit")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_sensor", referencedColumnName = "id_sensor")
    @JsonBackReference
    private Sensor sensor;

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "id_beacon", referencedColumnName = "id_beacon"))
    @JsonBackReference
    private Beacon beacon;

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "id_central_user", referencedColumnName = "id_central_user"))
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "id_audit_type", referencedColumnName = "id_audit_type"))
    @JsonBackReference
    private AuditType auditType;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "description")
    private String description;

}
