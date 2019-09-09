package hr.foi.diplomski.central.service.centralaudit;

import hr.foi.diplomski.central.model.AuditType;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.CentralAudit;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.AuditTypeRepository;
import hr.foi.diplomski.central.repository.CentralAuditRepository;
import hr.foi.diplomski.central.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentralAuditServiceIml implements CentralAuditService {

    private final CentralAuditRepository centralAuditRepository;
    private final AuditTypeRepository auditTypeRepository;
    private final UserService userService;

    @Override
    public void saveAudit(AuditType auditType, Sensor sensor, Beacon beacon, String description) {
        centralAuditRepository.save(new CentralAudit(0L, sensor, beacon, userService.findCurrentUser(), auditType,
                LocalDateTime.now(), description));
    }

    @Override
    public void saveAudit(AuditType auditType, Beacon beacon, String description) {
        centralAuditRepository.save(new CentralAudit(0L, null, beacon, userService.findCurrentUser(), auditType,
                LocalDateTime.now(), description));
    }

    @Override
    public void saveAudit(AuditType auditType, Sensor sensor, String description) {
        centralAuditRepository.save(new CentralAudit(0L, sensor, null, userService.findCurrentUser(), auditType,
                LocalDateTime.now(), description));
    }

    @Override
    public void saveAudit(Long auditTypeId, Sensor sensor, Beacon beacon, String description) {
        AuditType auditType = auditTypeRepository.findById(auditTypeId).orElse(null);

        if (auditType != null) {
            saveAudit(auditType, sensor, beacon, description);
        } else {
            log.warn(String.format("Audit type with id %s doesnt exist", auditTypeId));
        }
    }

    @Override
    public void saveAudit(Long auditTypeId, Beacon beacon, String description) {
        AuditType auditType = auditTypeRepository.findById(auditTypeId).orElse(null);

        if (auditType != null) {
            saveAudit(auditType, beacon, description);
        } else {
            log.warn(String.format("Audit type with id %s doesnt exist", auditTypeId));
        }
    }

    @Override
    public void saveAudit(Long auditTypeId, Sensor sensor, String description) {
        AuditType auditType = auditTypeRepository.findById(auditTypeId).orElse(null);

        if (auditType != null) {
            saveAudit(auditType, sensor, description);
        } else {
            log.warn(String.format("Audit type with id %s doesnt exist", auditTypeId));
        }
    }

    @Override
    public void deleteAuditByBeacon(Beacon beacon) {
        centralAuditRepository.deleteAllByBeacon(beacon);
    }

    @Override
    public void delteAuditBySensor(Sensor sensor) {
        centralAuditRepository.deleteAllBySensor(sensor);
    }


}
