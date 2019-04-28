package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.CentralAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentralAuditRepository extends JpaRepository<CentralAudit, Long> {
}
