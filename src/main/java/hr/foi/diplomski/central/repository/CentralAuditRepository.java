package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.CentralAudit;
import hr.foi.diplomski.central.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentralAuditRepository extends JpaRepository<CentralAudit, Long> {

    void deleteAllByBeacon(Beacon beacon);

    void deleteAllBySensor(Sensor sensor);

}
