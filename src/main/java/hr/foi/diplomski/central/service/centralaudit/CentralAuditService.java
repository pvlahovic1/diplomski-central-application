package hr.foi.diplomski.central.service.centralaudit;

import hr.foi.diplomski.central.model.AuditType;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Sensor;

public interface CentralAuditService {

    void saveAudit(AuditType auditType, Sensor sensor, Beacon beacon, String description);

    void saveAudit(AuditType auditType, Beacon beacon, String description);

    void saveAudit(AuditType auditType, Sensor sensor, String description);

    void saveAudit(Long auditTypeId, Sensor sensor, Beacon beacon, String description);

    void saveAudit(Long auditTypeId, Beacon beacon, String description);

    void saveAudit(Long auditTypeId, Sensor sensor, String description);

    void deleteAuditByBeacon(Beacon beacon);

    void delteAuditBySensor(Sensor sensor);

}
