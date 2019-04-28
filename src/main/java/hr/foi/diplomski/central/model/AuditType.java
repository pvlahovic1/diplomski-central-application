package hr.foi.diplomski.central.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "audit_type")
public class AuditType {

    @Id
    @GeneratedValue(generator = "audit_type_id_audit_type_seq")
    @Column(name = "id_audit_type")
    private Long id;

    @Column(name = "description")
    private String description;

}
